import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class Client {
	private static final int SERVER_PORT = 1427;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket client;
	private String username;
	private ClientGUI gui;
	private String currentRoom;
	
	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		username = "";
		while (username.trim().isEmpty()) {
			username = JOptionPane.showInputDialog(null, "Enter username:");
			if (username.contains("/"))
				username = "";
		}
		try {
			client = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
			outputStream = new ObjectOutputStream(client.getOutputStream());
			inputStream = new ObjectInputStream(client.getInputStream());
			outputStream.writeObject(new Message(Type.LOGIN, username.trim()));
			outputStream.flush();
			while(true) {
				synchronized (inputStream) {
					Message response = (Message)inputStream.readObject();
					action(response);
				}
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Host not found!", "Error", JOptionPane.ERROR_MESSAGE);
		}  catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Host not available!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void sendExitMessage() {
		try {
			outputStream.writeObject(new Message(Type.EXIT));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void action(Message response) {
		if (response.getType().equals(Type.LOGIN.toString())) {
			if (response.getText().equals(Response.ERROR.toString())) {				 
				JOptionPane.showMessageDialog(null, "Username is not available!", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			} else if (response.getText().equals(Response.OK.toString())){
				setCurrentRoom(response.getAddText());
				gui = new ClientGUI(this);				
			}
		} else if (response.getType().equals(Type.UPDATE.toString())) {
			gui.setUsernames(response.getText().split("/"));
			gui.setRooms(response.getAddText().split("/"));
		} else if (response.getType().equals(Type.CHAT.toString())) {
			if (response.getText().equals(Response.OK.toString()))
				gui.writeMessage(response.getAddText());
		}
	}

	public String getUsername() {
		return username;
	}
	
	public String getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(String currentRoom) {
		this.currentRoom = currentRoom;
	}	
	
	public void sendMessage(String to, String text) {
		try {
			outputStream.writeObject(new Message(Type.CHAT, to, text));
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
