import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class GUI extends JFrame{
	private Hashtable<String, Book> table = null;
	private Hashtable<String, String> users = null;
	private String currentUser = null;
	
	public GUI(Hashtable<String, Book> table, Hashtable<String, String> users) {
		this.table = table;
		this.users = users;
		login();
	}
	
	JFrame listGUI = null;
	JTable inputData = null;
	
	private void runGUI(){
		listGUI = new JFrame();
		listGUI.setTitle("Bookshelf");
		listGUI.setSize(500, 500);
		listGUI.setLocation(200, 300);
		listGUI.setLayout(new FlowLayout());
		Label results = new Label("Results");  // construct the Label component
	    listGUI.add(results);  
		inputData = buildGui(table);
	    JScrollPane scrollPane = new JScrollPane(inputData);
	    listGUI.getContentPane().setLayout(new FlowLayout());
        listGUI.getContentPane().add(scrollPane, BorderLayout.CENTER);
	    listGUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
		listGUI.setVisible(true);
	}
	
	
	private JTable buildGui(Hashtable<String, Book> data){
		String[] cols = {"ISBN", "Title", "Author", "Genre", "Year", "Price"};
		int numRows = data.size();
		Object[][] rows = new Object[numRows][6];
		String str;
	    Set<String> keys = data.keySet();
	    
	    Iterator<String> itr = keys.iterator();
	    
    	int i=0;
	    while (itr.hasNext()) { 
	       str = itr.next();
	       rows[i][0] = data.get(str).getIsbn();
	       rows[i][1] = data.get(str).getAuthor();
	       rows[i][2] = data.get(str).getTitle();
	       rows[i][3] = data.get(str).getGenre();
	       rows[i][4] = data.get(str).getYear();
	       rows[i][5] = data.get(str).getPrice();
	       i++;
	    }
		JTable table = new JTable(rows, cols);
	    return table;
	}
	
    JTextField usernameFillin = new JTextField(20); 
    JTextField passwordFillin = new JTextField(20);
    JButton btnLogin = new JButton("Login");
    JFrame signin = null;
    
	public void login(){
		signin = new JFrame();
		signin.setTitle("Login");
		signin.setSize(300, 200);
		signin.setLocation(500, 280);
		signin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel username = new JLabel("Username:");   
        JLabel password = new JLabel("Password:");
        panel.add(username);
        panel.add(usernameFillin);
        panel.add(password);
        panel.add(passwordFillin);  
        panel.add(btnLogin);
        signin.getContentPane().add(BorderLayout.CENTER, panel);
        signin.setVisible(true);
        authenticate();
	} //end login
	
	public void authenticate(){
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				String user = usernameFillin.getText();
				String pass = passwordFillin.getText();
				if(users.containsKey(user) && users.get(user).equals(pass)) {
					System.out.println("YAYYYY");
					currentUser = user;
					signin.dispose();
					runGUI();
				} 
				else {
					JOptionPane.showMessageDialog(null,"Wrong Password / Username");
					usernameFillin.setText("");
					passwordFillin.setText("");
					usernameFillin.requestFocus();
				}
			}
		});
	} //end authenticate 
	
	
	
}
