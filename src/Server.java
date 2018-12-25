import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static final int PORT = 1427;
	private ArrayList<ClientHandler> clients;
	
	public static void main(String[] args) {
		new Server(PORT);
	}
	
	public Server(int port) {
		clients = new ArrayList<ClientHandler>();
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
	
	public void sendToAll(Message msg) {
		for (ClientHandler client: clients) {
			client.send(msg);
		}
	}
	
	public void updateUserList() {
		String text = "";
		int size = clients.size();
		for (ClientHandler c: clients) {
			if (size-- != 1) {
				text += c.getUsername() + "/";
			} else {
				text += c.getUsername();
			}
		}
		sendToAll(new Message(Type.UPDATE, text));
	}
	
	public void removeClient(ClientHandler client) {
		clients.remove(client);
	}
	
	public ArrayList<ClientHandler> getClients() {
		return clients;
	}
}
