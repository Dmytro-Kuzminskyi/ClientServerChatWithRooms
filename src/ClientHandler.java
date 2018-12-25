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
	
	
	public ClientHandler(Socket client, Server server) {
		try {
			this.server = server;
			this.client = client;
			this.username = "";
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
						if (message != null)
							send(prepareResponse(message));		
						server.updateUserList();
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	} 
	
	public void send(Message msg) {
		try {
			outputStream.writeObject(msg);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		server.removeClient(this);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
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
				ArrayList<ClientHandler> clients = server.getClients();
				String text = "";
				int size = clients.size();
				for (ClientHandler c: clients) {
					if (size-- != 1) {
						text += c.getUsername() + "/";
					} else {
						text += c.getUsername();
					}
				}
				System.out.println(text);
				return new Message(Type.LOGIN, text);
			} else {
				return new Message(Type.LOGIN, Response.ERROR);
			}
		} else if (msg.getType().equals(Type.EXIT.toString())) {
			close();
		}
		return null;
	}
}
