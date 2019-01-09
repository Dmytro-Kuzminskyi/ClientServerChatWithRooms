import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClientGUI extends JFrame {
	private DefaultListModel<String> modelUsers;
	private DefaultListModel<String> modelRooms;
	private String[] usernames;
	private String[] rooms;
	private JList<String> userList;
	private JScrollPane spaneUserList;
	private JScrollPane spaneChat;
	private JLabel userCountInfo;
	private JLabel userCount;
	private JLabel roomsLabel;
	private JList<String> roomList;
	private JScrollPane spaneRoomList;
	private JTextPane chatTextPane;
	private JLabel receiversInfo;
	private JButton sendButton;
	private JTextField userMessage;
	private JButton addRoomButton;
	
	public ClientGUI(Client client) {
		this.setTitle("Chat");
		this.setSize(500, 500);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		userCountInfo = new JLabel("  Users in chat:");
		userCountInfo.setBounds(350, 0, 100, 20);
		userCountInfo.setOpaque(true);
		userCountInfo.setBackground(Color.WHITE);
		userCountInfo.setVisible(true);		
		userCount = new JLabel("0");
		userCount.setBounds(435, 0, 59, 20);
		userCount.setOpaque(true);
		userCount.setBackground(Color.WHITE);
		userCount.setVisible(true);		
		modelUsers = new DefaultListModel<String>();
		roomsLabel = new JLabel("  Rooms:");
		roomsLabel.setBounds(350, 220, 144, 20);
		roomsLabel.setOpaque(true);
		roomsLabel.setBackground(Color.WHITE);
		roomsLabel.setVisible(true);
		userList = new JList<String>(modelUsers);
		userList.setBounds(350, 20, 144, 201);
		userList.setBackground(Color.WHITE);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		userList.setVisible(true);
		spaneUserList = new JScrollPane();
		spaneUserList.setVisible(true);
		spaneUserList.getViewport().add(userList);
		modelRooms = new DefaultListModel<String>();
		roomList = new JList<String>(modelRooms);
		roomList.setBounds(350, 240, 144, 231);
		roomList.setBackground(Color.WHITE);
		roomList.setLayoutOrientation(JList.VERTICAL);
		roomList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		roomList.setVisible(true);
		roomList.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				String selectedRoom = roomList.getSelectedValue();
				if (!selectedRoom.equals(client.getCurrentRoom())) {
					client.changeRoomRequest(selectedRoom);					
				}
				chatTextPane.requestFocus();
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
			
		});
		spaneRoomList = new JScrollPane();
		spaneRoomList.setVisible(true);
		spaneRoomList.getViewport().add(roomList);
		receiversInfo = new JLabel("Message to \"" + client.getCurrentRoom() + "\"");
		receiversInfo.setBounds(0, 431, 350, 20);
		receiversInfo.setOpaque(true);
		receiversInfo.setBackground(Color.WHITE);
		receiversInfo.setVisible(true);
		chatTextPane = new JTextPane();
		chatTextPane.setBounds(0, 20, 350, 400);
		chatTextPane.setVisible(true);
		chatTextPane.setEditable(false);
		spaneChat = new JScrollPane();
		spaneChat.setVisible(true);
		spaneChat.getViewport().add(chatTextPane);
		chatTextPane.setBorder(BorderFactory.createEtchedBorder());
		chatTextPane.setText("Welcome to the chat server!\nNow you are in General");
		addRoomButton = new JButton("Create room");
		addRoomButton.setBounds(100, 0, 120, 20);
		addRoomButton.setVisible(true);
		addRoomButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String roomName = "";
				while (roomName.trim().isEmpty()) {
					roomName = JOptionPane.showInputDialog(null, "Enter room name:");
					if (roomName.contains("/"))
						roomName = "";
				}
				client.addRoomRequest(roomName);
			}
			
		});
		sendButton = new JButton("Send");
		sendButton.setBounds(275, 451, 75, 20);
		sendButton.setEnabled(false);
		sendButton.setVisible(true);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (receiversInfo.getText().equals("Message to \"" + client.getCurrentRoom() + "\"")) {
					client.sendMessage(client.getCurrentRoom(), "@" + client.getUsername() + ": " + userMessage.getText());
					userMessage.setText("");
					sendButton.setEnabled(false);
				}
			}
			
		});
		userMessage = new JTextField();
		userMessage.setEditable(true);
		userMessage.setBounds(0, 451, 275, 20);
		userMessage.setVisible(true);
		userMessage.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (sendButton.isEnabled() & e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendButton.doClick(5);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (userMessage.getText().length() != 0 ) {
					sendButton.setEnabled(true);
				} else {
					sendButton.setEnabled(false);
				}
			}
			
		});
		addAllCompToFrame();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "Close application?", 
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					client.sendExitMessage();
			        System.exit(0);
				} 
			}
		});
	}
	
	public void setUsernames(String[] users) {
		this.usernames = users;
		modelUsers.removeAllElements();
		for (String user: usernames) {
			modelUsers.addElement("@" + user);
		}
		userCount.setText("" + modelUsers.size());
	}
	
	public void setRooms(String[] rooms) {
		this.rooms = rooms;
		modelRooms.removeAllElements();
		for (String room: this.rooms) {
			modelRooms.addElement(room);
		}
	}
	
	public void writeMessage(String message) {
		String text = chatTextPane.getText();
		text += "\n" + message;
		chatTextPane.setText(text);
	}
	
	public void changeRoomMessage(String selectedRoom) {
		String text = chatTextPane.getText();
		text += "\nNow you are in " + selectedRoom;
		chatTextPane.setText(text);
	}
	
	public void setReceivers(String text) {
		receiversInfo.setText("Message to \"" + text + "\"");
	}
	
 	private void addAllCompToFrame() {
		this.add(userCountInfo);
		this.add(userCount);
		this.add(chatTextPane);
		this.add(roomList);
		this.add(userList);
		this.add(receiversInfo);
		this.add(sendButton);
		this.add(userMessage);
		this.add(addRoomButton);
		this.add(roomsLabel);
	}
}
