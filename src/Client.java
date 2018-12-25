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
	
	
	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		username = "";
		while (username.trim().isEmpty()) {
			username = JOptionPane.showInputDialog(null, "Enter username:");
		}
		try {
			client = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
			outputStream = new ObjectOutputStream(client.getOutputStream());
			inputStream = new ObjectInputStream(client.getInputStream());
			outputStream.writeObject(new Message(Type.LOGIN, username.trim()));
			outputStream.flush();
			while(true) {
				Message response = (Message)inputStream.readObject();
				action(response);
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Host not found!", "Error", JOptionPane.ERROR_MESSAGE);
		}  catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Host not available!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void action(Message response) throws Exception {
		if (response.getType().equals(Type.LOGIN.toString())) {
			if (response.getText().equals(Response.OK.toString())) {
				gui = new ClientGUI();
			} else {
				JOptionPane.showMessageDialog(null, "Username is not available!", "Error", JOptionPane.ERROR_MESSAGE);
				outputStream.writeObject(new Message(Type.EXIT, ""));
				outputStream.flush();
			}
		}
	}
}
