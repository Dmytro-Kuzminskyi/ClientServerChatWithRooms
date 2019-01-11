import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static final int PORT = 1427;
	private ArrayList<ClientHandler> clients;
	private ArrayList<String> rooms;
	
	public static void main(String[] args) {
		new Server(PORT);
	}
	
	public Server(int port) {
		clients = new ArrayList<ClientHandler>();		
		rooms = new ArrayList<>();
		rooms.add("General");
		Socket clientSocket = null;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("The server has been started!");
			while(true) {
				clientSocket = server.accept();
				ClientHandler client = new ClientHandler(clientSocket, this);
				clients.add(client);
				new Thread(client).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
				System.out.println("The server has been stopped!");
				server.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public void sendToAllWelcomeMsg(ClientHandler client) {
		for (ClientHandler c: clients) {
			if (c.getCurrentRoom().equals(rooms.get(0)) & !c.getUsername().equals(client.getUsername()))
				c.send(new Message(Type.CHAT, Response.OK, "@" + client.getUsername() + " has been connected to the server!"));
		}
	}
	
	public int sendToAllLeaveMsg(ClientHandler client ) {
		for (ClientHandler c: clients) {
			if (!c.getUsername().equals(client.getUsername()) & c.getCurrentRoom().equals(rooms.get(0)))
				c.send(new Message(Type.CHAT, Response.OK, "@" + client.getUsername() + " has been disconnected from the server!"));
			if (!c.getUsername().equals(client.getUsername())) {
				if (c.getCurrentRoom().equals(client.getCurrentRoom()) & !client.getCurrentRoom().equals(rooms.get(0)))
					c.send(new Message(Type.CHAT, Response.OK, "@" + client.getUsername() + " has left the room!"));
			}
		}
		return 1;
	}
	
	public void sendToAllRoomChangeMsg(ClientHandler client) {
		for (ClientHandler c: clients) {
			if (!c.getUsername().equals(client.getUsername()) & client.getOldRoom().equals(c.getCurrentRoom())) {
				c.send(new Message(Type.CHAT, Response.OK, "@" + client.getUsername() + " has left the room!"));
			} else if (!c.getUsername().equals(client.getUsername()) & client.getCurrentRoom().equals(c.getCurrentRoom())) {
				c.send(new Message(Type.CHAT, Response.OK, "@" + client.getUsername() + " has entered the room!"));
			}
		}
	}
	
	public void sendTo(ClientHandler client, Message msg) {
		String to = msg.getText();
		for (ClientHandler c: clients) {
			if (to.equals(c.getCurrentRoom()) & !c.getUsername().equals(client.getUsername())) {
				c.send(new Message(Type.CHAT, Response.OK, msg.getAddText()));
			}
		}
	}
	
	public void sendPrivateTo(ClientHandler client, Message msg) {
		String to = msg.getText();
		for (ClientHandler c: clients) {
			if (to.equals(c.getUsername())) 
				c.send(new Message(Type.PRIVATE, Response.OK, msg.getAddText()));
		}
	}
	
	public void update() {
		for (ClientHandler client: clients) {
			String[] updateInfo = getInfoForUpdate(client);
			client.send(new Message(Type.UPDATE, updateInfo[0], updateInfo[1], updateInfo[2]));
		}
	}
	
	private String[] getInfoForUpdate(ClientHandler client) {
		String usersInfoCurrentRoom = "";
		int userCountCurrentRoom = 0;
		for (ClientHandler c: clients) {
			if (c.getCurrentRoom().equals(client.getCurrentRoom())) {
				userCountCurrentRoom++;
			}
		}
		for (ClientHandler c: clients) {
			if (c.getCurrentRoom().equals(client.getCurrentRoom())) {
				if (userCountCurrentRoom-- != 1) {
					usersInfoCurrentRoom += c.getUsername() + "/";
				} else {
					usersInfoCurrentRoom += c.getUsername();
				}
			}
		}
		String allUsersInfo = "";
		int allUsersCount = clients.size();
		for (ClientHandler c: clients) {
			if (allUsersCount-- != 1) {
				allUsersInfo += c.getUsername() + "/";
			} else {
				allUsersInfo += c.getUsername();
			}
		}
		String roomsInfo = "";
		int roomCount = rooms.size();
		for (String room: rooms) {
			if (roomCount-- != 1) {
				roomsInfo += room + "/";
			} else {
				roomsInfo += room;
			}
		}
		return new String[] {allUsersInfo, usersInfoCurrentRoom, roomsInfo};
	}
	
	public void removeClient(ClientHandler client) {
		clients.remove(client);
	}
	
	public ArrayList<ClientHandler> getClients() {
		return clients;
	}
	
	public ArrayList<String> getRooms() {
		return rooms;
	}
	
	public void addRoom(String room) {
		rooms.add(room);
	}
}
