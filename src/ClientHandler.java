import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	private Server server;
	private Socket client;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private String username;
	private String currentRoom;
	
	public ClientHandler(Socket client, Server server) {
		try {
			this.server = server;
			this.client = client;
			this.username = "";
			this.currentRoom = server.getRooms().get(0);
			outputStream = new ObjectOutputStream(client.getOutputStream());
			inputStream = new ObjectInputStream(client.getInputStream());
		} catch (Exception e ) {
			this.close();
			e.printStackTrace();
		} 
	}

	@Override
	public void run() {
		Message message;
		try {
			while (true) {
				synchronized (inputStream) {
						message = (Message)inputStream.readObject();
						if (message != null) {
							int state = send(prepareResponse(message));
							server.update();
							if (state == 1)
								server.sendToAllWelcomeMsg(this);
							else if (state == 2) 
								server.sendTo(this, message);						
						}
					}
			}
		} catch (Exception e) {
			this.close();
			e.printStackTrace();
		} 
	} 
	
	public int send(Message msg) {
		if (msg != null) {
			try {
				outputStream.writeObject(msg);
				outputStream.flush();
				if (msg.getType().equals(Type.LOGIN.toString()) & msg.getText().equals(Response.OK.toString())) 
					return 1;
				if (msg.getType().equals(Type.CHAT.toString()) & msg.getText().equals(Response.OK.toString())) 
					return 2;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public void close(ClientHandler client) {
		try {
			if (server.sendToAllLeaveMsg(client) == 1) {
				outputStream.close();
				inputStream.close();
				client.close();
				server.removeClient(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			outputStream.close();
			inputStream.close();
			client.close();
			server.removeClient(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(String currentRoom) {
		this.currentRoom = currentRoom;
	}
	
	private Message prepareResponse(Message msg) {
		if (msg.getType().equals(Type.LOGIN.toString())) {
			boolean isSameUser = false;
			for (ClientHandler client : server.getClients()) {
				if (client.getUsername().equals(msg.getText()))
					isSameUser = true;
			}
			if (!isSameUser) {
				setUsername(msg.getText());
				System.out.println(client.getInetAddress().getHostAddress() + "@" + username + " connected to the server.");
				return new Message(Type.LOGIN, Response.OK, server.getRooms().get(0));
			} else {
				return new Message(Type.LOGIN, Response.ERROR);
			}
		} else if (msg.getType().equals(Type.EXIT.toString())) {
			this.close(this);
		} else if (msg.getType().equals(Type.CHAT.toString())) {
			return new Message (Type.CHAT, Response.OK, msg.getAddText());
		} else if (msg.getType().equals(Type.ADD_ROOM.toString())) {
			ArrayList<String> rooms = server.getRooms();
			for (String room: rooms) {
				if (msg.getText().equals(room)) 
					return new Message(Type.ADD_ROOM, Response.ERROR);
			}
			server.addRoom(msg.getText());
			return new Message(Type.ADD_ROOM, Response.OK, msg.getText());
		} else if (msg.getType().equals(Type.CHANGE_ROOM.toString())) {
			currentRoom = msg.getText();
			return new Message(Type.CHANGE_ROOM, Response.OK, currentRoom);
		}
		return null;
	}
}
