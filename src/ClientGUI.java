import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ClientGUI extends JFrame {
	private DefaultListModel<String> modelUsersCurrentRoom;
	private DefaultListModel<String> modelAllUsers;
	private DefaultListModel<String> modelRooms;
	private String[] allUsers;
	private String[] usersCurrentRoom;
	private String[] rooms;
	private JList<String> userListCurrentRoom;
	private JList<String> allUserList;
	private JLabel userCountInfo;
	private JLabel userCountCurrentRoom;
	private JLabel allUsersCount;
	private JLabel roomsLabel;
	private JList<String> roomList;
	private JScrollPane spaneRoomList;
	private JTextPane chatTextPane;
	private JLabel receiversInfo;
	private JButton sendButton;
	private JTextField userMessage;
	private JButton addRoomButton;
	private JButton switchUserList;
	private JButton resetPrivateMsgButton;
	private JButton cleanTextPane;
	
	public ClientGUI(Client client) {
		this.setTitle("Your nickname: " + client.getUsername());
		this.setSize(500, 500);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		userCountInfo = new JLabel("  Users in room:");
		userCountInfo.setBounds(350, 0, 95, 20);
		userCountInfo.setOpaque(true);
		userCountInfo.setBackground(Color.WHITE);
		userCountInfo.setVisible(true);	
		allUsersCount = new JLabel("0");
		allUsersCount.setBounds(445, 0, 25, 20);
		allUsersCount.setOpaque(true);
		allUsersCount.setBackground(Color.WHITE);
		userCountCurrentRoom = new JLabel("0");
		userCountCurrentRoom.setBounds(445, 0, 25, 20);
		userCountCurrentRoom.setOpaque(true);
		userCountCurrentRoom.setBackground(Color.WHITE);
		userCountCurrentRoom.setVisible(true);		
		modelUsersCurrentRoom = new DefaultListModel<String>();
		modelAllUsers = new DefaultListModel<String>();
		roomsLabel = new JLabel("  Rooms:");
		roomsLabel.setBounds(350, 320, 144, 20);
		roomsLabel.setOpaque(true);
		roomsLabel.setBackground(Color.WHITE);
		roomsLabel.setVisible(true);
		allUserList = new JList<String>(modelAllUsers); 
		allUserList.setBounds(350, 20, 144, 300);
		allUserList.setBackground(Color.WHITE);
		allUserList.setLayoutOrientation(JList.VERTICAL);
		allUserList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		allUserList.setVisible(false);
		allUserList.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				String selectedUser = allUserList.getSelectedValue();
				if (!selectedUser.equals("@" + client.getUsername())) {
					setReceivers(selectedUser);
				}
				chatTextPane.requestFocus();
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
			
		});
		userListCurrentRoom = new JList<String>(modelUsersCurrentRoom);
		userListCurrentRoom.setBounds(350, 20, 144, 300);
		userListCurrentRoom.setBackground(Color.WHITE);
		userListCurrentRoom.setLayoutOrientation(JList.VERTICAL);
		userListCurrentRoom.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		userListCurrentRoom.setVisible(true);
		userListCurrentRoom.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				String selectedUser = userListCurrentRoom.getSelectedValue();
				if (!selectedUser.equals("@" + client.getUsername())) {
					setReceivers(selectedUser);
				}
				chatTextPane.requestFocus();
			}

			@Override
			public void focusLost(FocusEvent e) {				
			}
			
		});
		modelRooms = new DefaultListModel<String>();
		roomList = new JList<String>(modelRooms);
		roomList.setBounds(350, 340, 144, 131);
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
		spaneRoomList = new JScrollPane(roomList);
		spaneRoomList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		receiversInfo = new JLabel("Message to \"" + client.getCurrentRoom() + "\"");
		receiversInfo.setBounds(0, 431, 200, 20);
		receiversInfo.setOpaque(true);
		receiversInfo.setBackground(Color.WHITE);
		receiversInfo.setVisible(true);
		resetPrivateMsgButton = new JButton("X");
		resetPrivateMsgButton.setBounds(201, 431, 20, 20);
		resetPrivateMsgButton.setVisible(false);
		resetPrivateMsgButton.setMargin(new Insets(1, 1, 1, 1));
		resetPrivateMsgButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setReceivers("General");
			}});
		chatTextPane = new JTextPane();
		chatTextPane.setBounds(0, 20, 350, 411);
		chatTextPane.setVisible(true);
		chatTextPane.setEditable(false);
		chatTextPane.setBorder(BorderFactory.createEtchedBorder());
		chatTextPane.setText("Welcome to the chat server!\nNow you are in General");
		addRoomButton = new JButton("Create public room");
		addRoomButton.setBounds(0, 0, 120, 20);
		addRoomButton.setMargin(new Insets(1, 1, 1, 1));
		addRoomButton.setVisible(true);
		addRoomButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String roomName = "";
				while (roomName.trim().isEmpty()) {
					roomName = JOptionPane.showInputDialog(null, "Enter room name:");
					if (roomName.contains("/") | roomName.contains(" ") | roomName.contains("@") | roomName.length() > 15)
						roomName = "";
				}
				client.addRoomRequest(roomName);
			}
			
		});
		sendButton = new JButton("Send");
		sendButton.setBounds(290, 451, 60, 20);
		sendButton.setMargin(new Insets(1, 1, 1, 1));
		sendButton.setEnabled(false);
		sendButton.setVisible(true);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!receiversInfo.getText().contains("@")) {
					client.sendMessage(client.getCurrentRoom(), "@" + client.getUsername() + ": " + userMessage.getText());
					userMessage.setText("");
					sendButton.setEnabled(false);
				} else {
					int startPos = receiversInfo.getText().indexOf("@");
					client.sendPrivateMessage(receiversInfo.getText().substring(startPos + 1, receiversInfo.getText().length() - 1),
							"(Private) To \"" + receiversInfo.getText().substring(startPos, receiversInfo.getText().length() - 1) + "\": " + userMessage.getText());
					userMessage.setText("");
					sendButton.setEnabled(false);
				}
			}
			
		});
		userMessage = new JTextField();
		userMessage.setEditable(true);
		userMessage.setBounds(0, 451, 290, 20);
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
		switchUserList = new JButton("A");
		switchUserList.setBounds(470, 0, 25, 20);
		switchUserList.setMargin(new Insets(1, 1, 1, 1));
		switchUserList.setVisible(true);
		switchUserList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				if (switchUserList.getText().equals("A")) {
					userCountInfo.setText("  Users in chat:");
					userListCurrentRoom.setVisible(false);
					allUserList.setVisible(true);
					userCountCurrentRoom.setVisible(false);
					allUsersCount.setVisible(true);
					switchUserList.setText("R");
				} else {					
					userCountInfo.setText("  Users in room:");
					allUserList.setVisible(false);
					userListCurrentRoom.setVisible(true);
					allUsersCount.setVisible(false);
					userCountCurrentRoom.setVisible(true);
					switchUserList.setText("A");
				}
			}
			
		});
		cleanTextPane = new JButton("Clean chat window");
		cleanTextPane.setBounds(221, 431, 129, 20);
		cleanTextPane.setMargin(new Insets(1, 1, 1, 1));
		cleanTextPane.setVisible(true);
		cleanTextPane.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chatTextPane.setText("Chat has been cleared!");
			}
			
		});
		addAllCompToFrame();
		this.getContentPane().setBackground(Color.white);
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
	
	public void setAllUsers(String[] users) {
		this.allUsers = users;
		modelAllUsers.removeAllElements();
		for (String user: allUsers) {
			modelAllUsers.addElement("@" + user);
		}
		allUsersCount.setText("" + modelAllUsers.size());
	}
	
	public void setUsersCurrentRoom(String[] users) {
		this.usersCurrentRoom = users;
		modelUsersCurrentRoom.removeAllElements();
		for (String user: usersCurrentRoom) {
			modelUsersCurrentRoom.addElement("@" + user);
		}
		userCountCurrentRoom.setText("" + modelUsersCurrentRoom.size());
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
		if (!receiversInfo.getText().equals("Message to \"General\""))
			resetPrivateMsgButton.setVisible(true);
		else {
			resetPrivateMsgButton.setVisible(false);
		}
	}
	
 	private void addAllCompToFrame() {
		this.add(userCountInfo);
		this.add(userCountCurrentRoom);
		this.add(allUsersCount);
		this.add(chatTextPane);
		this.add(roomList);
		this.add(spaneRoomList);
		this.add(userListCurrentRoom);
		this.add(receiversInfo);
		this.add(sendButton);
		this.add(userMessage);
		this.add(addRoomButton);
		this.add(roomsLabel);
		this.add(switchUserList);
		this.add(allUserList);
		this.add(resetPrivateMsgButton);
		this.add(cleanTextPane);
	}
}
