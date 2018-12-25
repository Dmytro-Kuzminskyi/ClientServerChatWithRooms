import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class ClientGUI extends JFrame {
	private String[] usernames;
	private JList<String> userList;
	private JScrollPane spane;
	private JLabel userCountInfo;
	private JLabel userCount;
	private DefaultListModel<String> modelUsers;
	
	public ClientGUI(String[] clients) {
		this.usernames = clients;
		this.setTitle("Chat");
		this.setSize(500, 500);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);	
		userCountInfo = new JLabel("Users in chat:");
		userCountInfo.setBounds(350, 0, 100, 20);
		userCountInfo.setVisible(true);
		this.add(userCountInfo);
		userCount = new JLabel("0");
		userCount.setBounds(435, 0, 59, 20);
		userCount.setVisible(true);
		this.add(userCount);
		userList = new JList<String>(usernames);
		modelUsers = (DefaultListModel<String>)userList.getModel();
		userList.setBounds(350, 20, 144, 451);
		userList.setBackground(Color.WHITE);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		userList.setVisible(true);
		spane = new JScrollPane();
		spane.getViewport().add(userList);
		this.add(userList);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "Close application?", 
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			            System.exit(0);
				}
			}
		});
	}
	
	public void setUsernames(String[] users) {
		this.usernames = users;
		modelUsers.clear();
		for (String user: usernames) {
			modelUsers.addElement(user);
		}
	}
}
