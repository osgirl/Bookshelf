import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class GUI extends JFrame{
	private BookStorage currentUserBookStorage = null;
	private UserStorage currentUsersStorage = null;
	private Hashtable<String, User> users = null;
	private User currentUser = null;
	private boolean signedIn = false;
	private String[] input = null;
	private processingMethods inputs = null;
	
	public GUI(String[] input, UserStorage initialUsers) {
		this.input = input;
		this.currentUsersStorage = initialUsers;
		this.users = initialUsers.getUsers();
		login();
	}
	
    private JFrame signin = null;
	private JPanel panel = null;
	private JPanel panelAddUser = null;
	private JLabel username, password;
	private JLabel uname, pword;
    private JTextField usernameFillin = new JTextField(20); 
    private JTextField passwordFillin = new JTextField(20);
    private JTextField unameFillin = new JTextField(20); 
    private JTextField pwordFillin = new JTextField(20);
    private JButton btnLogin = null;
    private JButton btnGuest = null;
    private JButton btnExit = null;
    private JButton btnAddUser = null;
    private JButton btnCancel = null;
    private JButton btnAdd = null;
    
    private String user, pass;
    
	private void login(){
		signin = new JFrame();
		signin.setTitle("Login");
		signin.setSize(350, 220);
		signin.setLocation(500, 280);
		signin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        username = new JLabel("Username:");   
        password = new JLabel("Password:");
        btnAddUser = new JButton("Create Account");
        btnLogin = new JButton("Login");
        btnGuest = new JButton("Login as Guest");
        btnExit = new JButton("Exit Software");
        panel.add(username);
        panel.add(usernameFillin);
        panel.add(password);
        panel.add(passwordFillin);  
        panel.add(btnLogin);
        panel.add(btnGuest);
        panel.add(btnAddUser);
        panel.add(btnExit);
        signin.getContentPane().add(BorderLayout.CENTER, panel);
        signin.setVisible(true);
        btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				authenticate();
			}
		});
        
        btnGuest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				authenticateGuest();

			}
		});
        
        btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
        
        btnAddUser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				addUser();
			}
		});
	} //end login
	
	
	
	private void addUser() {
		panel.setVisible(false);
		panelAddUser = new JPanel();
		uname = new JLabel("Enter a Username: ");
		pword = new JLabel("Enter a Password: ");
		btnCancel = new JButton("Cancel");
		btnAdd = new JButton("Create Account");
		panelAddUser.add(uname);
		panelAddUser.add(unameFillin);
		panelAddUser.add(pword);
		panelAddUser.add(pwordFillin);
		panelAddUser.add(btnAdd);
		panelAddUser.add(btnCancel);
        signin.getContentPane().add(BorderLayout.CENTER, panelAddUser);
		panelAddUser.setVisible(true);
		
	    btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				panelAddUser.setVisible(false);
				panel.setVisible(true);
			}
		});
	    
	    btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				String newUsername = "";
				String newPassword = "";
				newUsername = unameFillin.getText();
				newPassword = pwordFillin.getText();
				if(currentUsersStorage.userExists(newUsername)){
					JOptionPane.showMessageDialog(null, "That user already exists.\n Please enter a different Username.", "Attention!", JOptionPane.INFORMATION_MESSAGE);
					unameFillin.setText("");
					pwordFillin.setText("");
				}
				else {
					currentUsersStorage.addUser(newUsername, newPassword);
					JOptionPane.showMessageDialog(null, "Username Added!", "Attention!", JOptionPane.INFORMATION_MESSAGE);
					unameFillin.setText("");
					pwordFillin.setText("");
					panelAddUser.setVisible(false);
					panel.setVisible(true);
				}
			}
		});
	}
	
	private void authenticateGuest(){
		currentUser = users.get("Guest");
		signedIn = true;
		signin.dispose();
		JOptionPane.showMessageDialog(null, "Please wait while signing in! \n Press 'OK' to continue.", "Loading...", JOptionPane.INFORMATION_MESSAGE);
		bootUp();
	}
	
	private void authenticate(){
		user = usernameFillin.getText();
		pass = passwordFillin.getText();
		if(users.containsKey(user) && users.get(user).getPassword().equals(pass)) {
			currentUser = users.get(user);
			signedIn = true;
			signin.dispose();
			JOptionPane.showMessageDialog(null, "Please wait while signing in! \n Press 'OK' to continue.", "Loading...", JOptionPane.INFORMATION_MESSAGE);
			usernameFillin.setText("");
			passwordFillin.setText("");
			bootUp();
		} 
		else {
			java.awt.Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,"Wrong Password / Username");
			usernameFillin.setText("");
			passwordFillin.setText("");
		}
	}
	
	private void bootUp(){
		if(signedIn){
			inputs = new processingMethods(currentUser.getBookStorage());
			inputs.processArgs(input);
			currentUserBookStorage = currentUser.getBookStorage();
			runGUI();
		}
	}
	
	JFrame listGUI = null;
	JTable inputData = null;
	JButton remove = null;
	JButton modify = null;
	JButton signout = null; 
	JButton advancedSearch = null;
	JButton checkout = null;
	Label results = null;
	JScrollPane scrollPane = null;
	JTable table = null;
	
	public void runGUI(){
		listGUI = new JFrame();
		listGUI.setTitle("Bookshelf");
		listGUI.setSize(600, 450);
		listGUI.setLocation(200, 300);
		listGUI.setLayout(new FlowLayout());
		results = new Label("Results for " + currentUser.getUsername());  // construct the Label component
	    listGUI.add(results);  
		inputData = buildGui(currentUserBookStorage.getBooks());
	    scrollPane = new JScrollPane(inputData);
	    scrollPane.setPreferredSize(new Dimension(600, 300));
	    listGUI.getContentPane().setLayout(new FlowLayout());
        listGUI.getContentPane().add(scrollPane, BorderLayout.CENTER);
        remove = new JButton("Remove Book");
        signout = new JButton("Sign Out");
        advancedSearch = new JButton("Advanced Search");
        listGUI.add(advancedSearch);
        listGUI.add(remove);
        if(currentUser.getUsername().equals("Admin")){
            modify = new JButton("Modify Information");
            listGUI.add(modify);
            modify.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae) {
    					modify();
    			}
    		});
        }
        if(!currentUser.getUsername().equals("Guest")){
            checkout = new JButton("Checkout");
            listGUI.add(checkout);
            checkout.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae) {
    				inputs.generateReceipt();
    				JOptionPane.showMessageDialog(null, "Your receipt has been generated!", "Thank you!", JOptionPane.INFORMATION_MESSAGE);
    			}
    		});
        }
        listGUI.add(signout);
	    listGUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
		listGUI.setVisible(true);
        remove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				removeBook();
			}
		});

        signout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				listGUI.dispose();
				signout();
			}
		});
		
        advancedSearch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				listGUI.dispose();
				advancedSearch();
			}
		});
	}
	
	private void signout(){
		try {
			currentUserBookStorage.logFile.close();
			inputs.getOutFile().close();
			if(inputs.receipt != null){
				inputs.getReceipt().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentUser = null;
		currentUserBookStorage = null;
		inputs = null;
		signedIn = false;
		login();
	}
	
	private void removeBook(){
		int row = inputData.getSelectedRow();
		if(row == -1) {
			JOptionPane.showMessageDialog(null, "Please select a book to be removed!", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		String isbn = inputData.getModel().getValueAt(row, 0).toString();
		currentUserBookStorage.deleteData(isbn);
		try {
			inputs.getOutFile().write("Deleted: " + isbn + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		listGUI.dispose();
		runGUI();
	}
	
	private void modify(){
		int row = inputData.getSelectedRow();
		int col = inputData.getSelectedColumn();
		if (row == -1 || col == -1) {
			JOptionPane.showMessageDialog(null, "Please select a field from the list of books to be modified!", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		String isbn = inputData.getModel().getValueAt(row, 0).toString();
		String fieldToChange = null;
		if(col==0) fieldToChange = "isbn";
		if(col==1) fieldToChange = "title";
		if(col==2) fieldToChange = "author";
		if(col==3) fieldToChange = "genre";
		if(col==4) fieldToChange = "price";
		String valueToChange = inputData.getModel().getValueAt(row, col).toString();
		String change = JOptionPane.showInputDialog("Change " +valueToChange+ " from " + fieldToChange + " to: \n");
		inputData.getModel().setValueAt(change, row, col);
		String modifiedValue = inputData.getModel().getValueAt(row, col).toString();
		currentUserBookStorage.modifyData(isbn, fieldToChange, modifiedValue);
		try {
			inputs.getOutFile().write("Modified: " + fieldToChange + " to " + change + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private JFrame advancedSearchGui = null;
	private JPanel panel2=null;
	private JLabel title=null;
	private JLabel isbn=null;
    private JTextField titleFillin = null;
    private JTextField isbnFillin = null;
    private JButton searchButton = null;
    private JButton cancelButton = null;
	
	private void advancedSearch() {
		advancedSearchGui = new JFrame();
		advancedSearchGui.setTitle("Advanced Search");
		advancedSearchGui.setSize(300, 200);
		advancedSearchGui.setLocation(500, 280);
		advancedSearchGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel2 = new JPanel();
        title = new JLabel("Title:");   
        isbn = new JLabel("ISBN:");
        titleFillin = new JTextField(20); 
        isbnFillin = new JTextField(20);
        searchButton = new JButton("Search");
        cancelButton = new JButton("Cancel");
        panel2.add(title);
        panel2.add(titleFillin);
        panel2.add(isbn);
        panel2.add(isbnFillin); 
        panel2.add(searchButton);
        panel2.add(cancelButton);
        advancedSearchGui.getContentPane().add(BorderLayout.CENTER, panel2);
        advancedSearchGui.setVisible(true);
        searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				int checkSearch = 0;
				String bookTitle = titleFillin.getText();
				String bookIsbn = isbnFillin.getText();
				checkSearch = inputs.searchNewBook(bookTitle, bookIsbn);
				if(checkSearch==1){
					java.awt.Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null,"No book found!");
					advancedSearch();
				}
				else{
					titleFillin.setText("");
					isbnFillin.setText("");
					advancedSearchGui.dispose();
					runGUI();
				}
			}
		});
        
        cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				advancedSearchGui.dispose();
				runGUI();
			}
		});  
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
	       rows[i][1] = data.get(str).getTitle();
	       rows[i][2] = data.get(str).getAuthor();
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
	
	public boolean getSignedIn() {
		return signedIn;
	}
	
	
	public User getCurrentUser() {
		return currentUser;
	}

}
