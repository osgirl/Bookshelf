import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * This is where the prime functionality of the GUI occurs
 * 
 * @author Kimberly Lalmansingh
 *
 */

public class GUI extends JFrame{
	//data members
	private BookStorage currentUserBookStorage = null;
	private UserStorage currentUsersStorage = null;
	private Hashtable<String, User> users = null;
	private User currentUser = null;
	private boolean signedIn = false;
	private String[] input = null;
	private ProcessingMethods inputs = null;
	
	//constructor
	public GUI(String[] input, UserStorage initialUsers) {
		this.input = input;
		this.currentUsersStorage = initialUsers;
		this.users = initialUsers.getUsers();
		login();
	}
	
	//data members for login GUI
    private JFrame signin = null;
	private JPanel panel = null;
	private JPanel panelAddUser = null;
	private JLabel username, password;
	private JLabel uname, pword;
    private JTextField usernameFillin; 
    private JTextField passwordFillin;
    private JTextField unameFillin; 
    private JTextField pwordFillin;
    private JButton btnLogin = null;
    private JButton btnGuest = null;
    private JButton btnExit = null;
    private JButton btnAddUser = null;
    private JButton btnCancel = null;
    private JButton btnAdd = null;
    private String user, pass;
    
    //GUI for login
	private void login(){
		signin = new JFrame();
		signin.setTitle("Login");
		signin.setSize(350, 220);
		signin.setLocation(500, 280);
		signin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
		panel.setBackground(Color.pink);
        username = new JLabel("Username:");   
        password = new JLabel("Password:");
        btnAddUser = new JButton("Create Account");
        btnLogin = new JButton("Login");
        btnGuest = new JButton("Login as Guest");
        btnExit = new JButton("Exit Software");
        usernameFillin = new JTextField(20); 
        passwordFillin = new JTextField(20);
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
				authenticate(); //function call to check sign in credentials 
			}
		});
        
        btnGuest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				authenticateGuest(); //function call to login as a guest

			}
		});
        
        btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				System.exit(0); //if "Exit Software" is selected, terminate software
			}
		});
        
        btnAddUser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				addUser(); //user creation 
			}
		});
	} //end login
	
	
	//add a new user to the database
	private void addUser() {
		panel.setVisible(false);
		panelAddUser = new JPanel();
		panelAddUser.setBackground(Color.pink);
		uname = new JLabel("Enter a Username: ");
		pword = new JLabel("Enter a Password: ");
		btnCancel = new JButton("Cancel");
		btnAdd = new JButton("Create Account");
        unameFillin = new JTextField(20); 
        pwordFillin = new JTextField(20);
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
				//go back to previous window
				panelAddUser.setVisible(false);
				panel.setVisible(true);
			}
		});
	    
	    btnAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				addUserIntoDB();
			}
		});
	}
	
	private void addUserIntoDB() {
		//user selects to add new account
		String newUsername = "";
		String newPassword = "";
		newUsername = unameFillin.getText();
		newPassword = pwordFillin.getText();
		if(currentUsersStorage.userExists(newUsername)){
			//if this username already exists, inform the user
			JOptionPane.showMessageDialog(null, "That user already exists.\n Please enter a different Username.", "Attention!", JOptionPane.INFORMATION_MESSAGE);
			unameFillin.setText("");
			pwordFillin.setText("");
		}
		else {
			//if the username is valid, add the user to the database so they can loging
			currentUsersStorage.addUser(newUsername, newPassword);
			JOptionPane.showMessageDialog(null, "Username Added!", "Attention!", JOptionPane.INFORMATION_MESSAGE);
			unameFillin.setText("");
			pwordFillin.setText("");
			panelAddUser.setVisible(false);
			panel.setVisible(true);
		}
	}
	
	private void authenticateGuest(){
		//set up for Guest login
		currentUser = users.get("Guest");
		signedIn = true;
		signin.dispose();
		JOptionPane.showMessageDialog(null, "Please wait while signing in! \n Press 'OK' to continue.", "Loading...", JOptionPane.INFORMATION_MESSAGE);
		bootUp();
	}
	
	private void authenticate(){
		//set up for non-Guest users
		user = usernameFillin.getText();
		pass = passwordFillin.getText();
		if(users.containsKey(user) && users.get(user).getPassword().equals(pass)) {
			//check to make sure the username and passwords match for the account
			currentUser = users.get(user);
			signedIn = true;
			signin.dispose();
			JOptionPane.showMessageDialog(null, "Please wait while signing in! \n Press 'OK' to continue.", "Loading...", JOptionPane.INFORMATION_MESSAGE);
			usernameFillin.setText("");
			passwordFillin.setText("");
			bootUp();
		} 
		else {
			//sound if incorrect information is entered
			java.awt.Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,"Wrong Password / Username");
			usernameFillin.setText("");
			passwordFillin.setText("");
		}
	}
	
	private void bootUp(){
		//when the user signs in, run main process of software
		if(signedIn){
			inputs = new ProcessingMethods(currentUser.getBookStorage());
			//process the flags given by user
			inputs.processArgs(input);
			//set the filled storaged after processing inputs
			currentUserBookStorage = currentUser.getBookStorage();
			runGUI();
		}
	}
	
	//data members for main GUI
	JFrame listGUI = null;
	JTable inputData = null;
	JButton remove = null;
	JButton modify = null;
	JButton signout = null; 
	JButton rebuild = null;
	JButton featuredItem = null;
	JButton advancedSearch = null;
	JButton checkout = null;
	Label results = null;
	JScrollPane scrollPane = null;
	JTable table = null;
	
	public void runGUI(){
		//main GUI
		listGUI = new JFrame();
		listGUI.setTitle("Bookshelf");
		listGUI.setSize(600, 450);
		listGUI.setLocation(440, 230);
		listGUI.setLayout(new FlowLayout());
		//display username
		results = new Label("Results for " + currentUser.getUsername());
	    listGUI.add(results);  
	    //set up table which will be displayed in GUI using
	    //users initially searched data
		inputData = buildGui(currentUserBookStorage.getBooks());
	    scrollPane = new JScrollPane(inputData);
	    scrollPane.setPreferredSize(new Dimension(600, 300));
	    listGUI.getContentPane().setLayout(new FlowLayout());
        listGUI.getContentPane().add(scrollPane, BorderLayout.CENTER);
	    listGUI.getContentPane().setBackground(Color.pink);
        remove = new JButton("Remove Book");
        signout = new JButton("Sign Out");
        featuredItem = new JButton("Check this out!");
        advancedSearch = new JButton("Advanced Search");
        listGUI.add(featuredItem);
        listGUI.add(advancedSearch);
        listGUI.add(remove);
        //only admin can modify data
        if(currentUser.getUsername().equals("Admin")){
            modify = new JButton("Modify Information");
            listGUI.add(modify);
            modify.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae) {
    					modify();
    			}
    		});
        }
        if(currentUser.getUsername().equals("Admin")){
        	rebuild = new JButton("Rebuild from a Log");            
        	listGUI.add(rebuild);
            rebuild.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae) {
    					rebuildLog();
    			}
    		});
        }
        //Guests cannot checkout books
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
        
        featuredItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				BufferedImage img = inputs.showFeaturedItem();
				displayFeaturedItem(img);
			}
		});
	}
	
	//displays featured item poster
	private void displayFeaturedItem(BufferedImage img) {
        ImageIcon icon = new ImageIcon(img);
        JOptionPane.showMessageDialog(null, "", "Featured Item of 2018", JOptionPane.INFORMATION_MESSAGE, icon);
	}
	
	//method when a user selects to sign out of system
	private void signout(){
		try {
			//close all files (log file and receipt file)
			currentUserBookStorage.logFile.close();
			inputs.getOutFile().close();
			if(inputs.receipt != null){
				inputs.getReceipt().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//set variables to null
		currentUser = null;
		currentUserBookStorage = null;
		inputs = null;
		signedIn = false;
		//go back to login screen
		login();
	}
	
	//method when a user choses to remove a book 
	private void removeBook(){
		int row = inputData.getSelectedRow();
		if(row == -1) {
			//an item must be selected for deletion
			JOptionPane.showMessageDialog(null, "Please select a book to be removed!", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		//get values at indicated spot
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
	
	//if admin wants to modify a value in the storage
	private void modify(){
		//get selection position
		int row = inputData.getSelectedRow();
		int col = inputData.getSelectedColumn();
		if (row == -1 || col == -1) {
			JOptionPane.showMessageDialog(null, "Please select a field from the list of books to be modified!", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		//get value of postion
		String isbn = inputData.getModel().getValueAt(row, 0).toString();
		String fieldToChange = null;
		//get field that will be changed
		if(col==0) fieldToChange = "isbn";
		if(col==1) fieldToChange = "title";
		if(col==2) fieldToChange = "author";
		if(col==3) fieldToChange = "genre";
		if(col==4) fieldToChange = "price";
		String valueToChange = inputData.getModel().getValueAt(row, col).toString();
		//indicate to admin what value previously was and prompt them for change
		String change = JOptionPane.showInputDialog("Change " +fieldToChange+ " from " + valueToChange + " to: \n");
		if(!(change==null)) {
			//if admin does not press on cancel, change the value
			inputData.getModel().setValueAt(change, row, col);
			String modifiedValue = inputData.getModel().getValueAt(row, col).toString();
			currentUserBookStorage.modifyData(isbn, fieldToChange, modifiedValue);
			try {
				//write to output file
				inputs.getOutFile().write("Modified: " + fieldToChange + " to " + change + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			listGUI.dispose();
			//rebuild gui with new changes
			runGUI();
		}
	}
	
	//function to rebuild data from a logfile
	private void rebuildLog() {
		//allow the admin to select the log file
		JFileChooser selectLogFile = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
	    selectLogFile.setFileFilter(filter);
	    File workingDirectory = new File(System.getProperty("user.dir"));
	    selectLogFile.setCurrentDirectory(workingDirectory);
	    int returnVal = selectLogFile.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			//run load log file method and restructure GUI
			inputs.loadLogFile(selectLogFile.getSelectedFile().getName());
			listGUI.dispose();
			runGUI();
		}
		
	}
	
	//advanced GUI data members/ components
	private JFrame advancedSearchGui = null;
	private JPanel panel2=null;
	private JLabel title=null;
	private JLabel isbn=null;
	private JLabel author=null;
	private JLabel genre = null;
    private JTextField titleFillin = null;
    private JTextField isbnFillin = null;
    private JTextField authorFillin = null;
    private JTextField genreFillin = null;
    private JButton searchButton = null;
    private JButton cancelButton = null;
	
    //advanced search GUI
	private void advancedSearch() {
		advancedSearchGui = new JFrame();
		advancedSearchGui.setTitle("Advanced Search");
		advancedSearchGui.setSize(320, 200);
		advancedSearchGui.setLocation(500, 280);
		advancedSearchGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel2 = new JPanel();
		panel2.setBackground(Color.pink);
        title = new JLabel("Title*:");   
        isbn = new JLabel("ISBN*:");
        author = new JLabel("Author");
        genre = new JLabel("Genre");
        titleFillin = new JTextField(20); 
        isbnFillin = new JTextField(20);
        authorFillin = new JTextField(20); 
        genreFillin = new JTextField(20);
        searchButton = new JButton("Search");
        cancelButton = new JButton("Cancel");
        panel2.add(title);
        panel2.add(titleFillin);
        panel2.add(isbn);
        panel2.add(isbnFillin); 
        panel2.add(author);
        panel2.add(authorFillin);
        panel2.add(genre);
        panel2.add(genreFillin);
        panel2.add(searchButton);
        panel2.add(cancelButton);
        advancedSearchGui.getContentPane().add(BorderLayout.CENTER, panel2);
        advancedSearchGui.setVisible(true);
        searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				advancedSearchButton();
			}
		});
        
        
        
        cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				advancedSearchGui.dispose();
				runGUI();
			}
		});  
	}
	
	//function when advanced search request on item is placed
	private void advancedSearchButton() {
		int checkSearch = 0;
		//get inputs user provided
		String bookTitle = titleFillin.getText();
		String bookIsbn = isbnFillin.getText();
		String bookAuthor = authorFillin.getText();
		String bookGenre = genreFillin.getText();
		
		if(bookGenre.equals("")){
			bookGenre = "N/A";
		}
		if(bookAuthor.equals("")){
			bookAuthor = "N/A";
		}

		//check search would return 1 if no book is found or
		//returns 0 if book is found
		checkSearch = inputs.searchNewBook(bookTitle, bookIsbn, bookAuthor, bookGenre);
		if(checkSearch==1){
			java.awt.Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,"No book found!");
			titleFillin.setText("");
			isbnFillin.setText("");
			authorFillin.setText("");
			genreFillin.setText("");
		}
		else{
			//reset advanced search textboxes
			titleFillin.setText("");
			isbnFillin.setText("");
			authorFillin.setText("");
			genreFillin.setText("");
			advancedSearchGui.dispose();
			runGUI();
		}
    }

	//method to build table in main GUI which displays all stored books
	private JTable buildGui(Hashtable<String, Book> data){
		//column information
		String[] cols = {"ISBN", "Title", "Author", "Genre", "Price"};
		int numRows = data.size();
		Object[][] rows = new Object[numRows][5];
		String str;
	    Set<String> keys = data.keySet();
	    
	    Iterator<String> itr = keys.iterator();
	    
	    //row information
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
        //resize column widths of the table created
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
	
	//getters and setters
	public boolean getSignedIn() {
		return signedIn;
	}
	
	public void setSignedIn(boolean s) {
		signedIn = s;
	}
	
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(User u) {
		currentUser = u;
	}

}
