import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GUI extends JFrame{

	public GUI(Hashtable<String, Book> table) {
		setTitle("Bookshelf");
		setSize(400, 400);
		setLocation(200, 300);
		Hashtable<String, Book> data = table;
		setLayout(new FlowLayout());
		Container contentPane1 = getContentPane();
		//Container contentPane2 = getContentPane();
		//add(contentPane1);
		//add(contentPane2);
		contentPane1.setLayout(new FlowLayout());
		Label results = new Label("Results");  // construct the Label component
	    add(results);  
		JTable inputData = buildGui(table);
	    JScrollPane scrollPane = new JScrollPane(inputData);
	    contentPane1.add(scrollPane, BorderLayout.CENTER);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);
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
	
	
}
