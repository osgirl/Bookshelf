import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class GUI extends JFrame{
	private Hashtable<String, Book> currentUserBooks = null;
	private Hashtable<String, User> users = null;
	private User currentUser = null;
	private boolean signedIn = false;
	private String[] input = null;
	
	public GUI(String[] input, Hashtable<String, User> users) {
		this.input = input;
		this.users = users;
		login();
	}
	
    private JFrame signin = null;
	private JPanel panel;
	private JLabel username, password;
    private JTextField usernameFillin = new JTextField(20); 
    private JTextField passwordFillin = new JTextField(20); //change to jpasswordfield**
    private JButton btnLogin = new JButton("Login");
    private String user, pass;
    
	private void login(){
		signin = new JFrame();
		signin.setTitle("Login");
		signin.setSize(300, 200);
		signin.setLocation(500, 280);
		signin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        username = new JLabel("Username:");   
        password = new JLabel("Password:");
        panel.add(username);
        panel.add(usernameFillin);
        panel.add(password);
        panel.add(passwordFillin);  
        panel.add(btnLogin);
        signin.getContentPane().add(BorderLayout.CENTER, panel);
        signin.setVisible(true);
        btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				authenticate();
			}
		});
	} //end login
	
	private void authenticate(){
		user = usernameFillin.getText();
		pass = passwordFillin.getText();
		if(users.containsKey(user) && users.get(user).getPassword().equals(pass)) {
			currentUser = users.get(user);
			signedIn = true;
			signin.dispose();
			bootUp();
		} 
		else {
			JOptionPane.showMessageDialog(null,"Wrong Password / Username");
			usernameFillin.setText("");
			passwordFillin.setText("");
			usernameFillin.requestFocus();
		}
	}
	
	private void bootUp(){
		if(signedIn){
			processingMethods inputs = new processingMethods(currentUser.getBookStorage());
			inputs.processArgs(input);
			currentUserBooks = currentUser.getBookStorage().getBooks();
			runGUI();
		}
	}
	
	JFrame listGUI = null;
	JTable inputData = null;
	JButton remove, modify, signout;
	Label results;
	JScrollPane scrollPane;
	JTable table;
	
	public void runGUI(){
		listGUI = new JFrame();
		listGUI.setTitle("Bookshelf");
		listGUI.setSize(600, 500);
		listGUI.setLocation(200, 300);
		listGUI.setLayout(new FlowLayout());
		results = new Label("Results for " + currentUser.getUsername());  // construct the Label component
	    listGUI.add(results);  
		inputData = buildGui(currentUserBooks);
	    scrollPane = new JScrollPane(inputData);
	    scrollPane.setPreferredSize(new Dimension(600, 300));
	    listGUI.getContentPane().setLayout(new FlowLayout());
        listGUI.getContentPane().add(scrollPane, BorderLayout.CENTER);
        remove = new JButton("Remove Book");
        modify = new JButton("Modify Information");
        signout = new JButton("Sign Out");
        listGUI.add(remove);
        listGUI.add(modify);
        listGUI.add(signout);
	    listGUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
		listGUI.setVisible(true);
		
		
	}
	
	
	
	
	private JTable buildGui(Hashtable<String, Book> data){
		String[] cols = {"ISBN", "Title", "Author", "Genre", "Price"};
		int numRows = data.size();
		Object[][] rows = new Object[numRows][5];
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
	       rows[i][4] = data.get(str).getPrice();
	       i++;
	    } 
	    
	    //cannot edit table values
		table = new JTable(rows, cols){
	    	public boolean isCellEditable(int row, int column) {return false;}
	    };

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //resize column widths
        for (int j = 0; j < table.getColumnCount(); j++) {
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn col = colModel.getColumn(j);
            int width = 0;

            TableCellRenderer renderer = col.getHeaderRenderer();
            for (int r = 0; r < table.getRowCount(); r++) {
              renderer = table.getCellRenderer(r, j);
              Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, j),
                  false, false, r, j);
              width = Math.max(width, comp.getPreferredSize().width);
            }
            col.setPreferredWidth(width + 2);
          }
	    return table;
	}
	
	public boolean getSignedIn(){
		return signedIn;
	}
	
	
	public User getCurrentUser(){
		return currentUser;
	}
	
	
}
