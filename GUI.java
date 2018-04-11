import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class GUI extends JFrame{
	private Hashtable<String, Book> table = null;
	private Hashtable<String, String> users = null;
	private String currentUser = null;
	
	public GUI(Hashtable<String, Book> table, Hashtable<String, String> users) {
		this.table = table;
		this.users = users;
		//login();
		runGUI();
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
	    
	    //cannot edit table values
		JTable table = new JTable(rows, cols){
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
	
    JTextField usernameFillin = new JTextField(20); 
    JTextField passwordFillin = new JTextField(20); //change to jpasswordfield**
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
