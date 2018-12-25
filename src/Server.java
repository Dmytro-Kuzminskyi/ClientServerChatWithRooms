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
	
	public void removeClient(ClientHandler client) {
		clients.remove(client);
	}
	
	public ArrayList<ClientHandler> getClients() {
		return clients;
	}
}
