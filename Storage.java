import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Hashtable;

public class Storage {
	
	Hashtable<String, Book> table;
	Hashtable<String, String> user;
	BufferedWriter logFile = null;
	private int items = 0;
	private String admin = "Admin";
	private String adminpass = "pass0000";
	
	public Storage(){
		user = new Hashtable<String, String>();
		user.put(admin, adminpass);
		table = new Hashtable<String, Book>();
		try {
			logFile = new BufferedWriter(new FileWriter("logFile.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Hashtable<String, Book> queryAllStored(String isbn){
		return table;
	}
	
	private Book queryBook(String isbn){
		return table.get(isbn);
	}
	
	private String retrieveISBN (String isbn){
		return table.get(isbn).getIsbn();
	}
	
	private String retrieveTitle(String isbn){
		return table.get(isbn).getTitle();
	}
	
	private String retrieveAuthor(String isbn){
		return table.get(isbn).getAuthor();
	}
	
	private String retrieveGenre(String isbn){
		return table.get(isbn).getGenre();
	}
	
	private String retrieveYear(String isbn){
		return table.get(isbn).getYear();
	}
	
	private String retrievePrice(String isbn){
		return table.get(isbn).getPrice();
	}
	
//	private String getUser(String u){
//		return user.get(u);
//	}
//	
//	private String getPassword(String p){
//		return user.get(p);
//	}
//	
//	private String getUserPass(String s){
//		String p = user.get(s).
//	}
	
	
	public void storeData(String isbn, String title, String author, String genre, String year, String price){
		items++;
		Book newBook = new Book(isbn, title, author, genre, year, price);
		table.put(isbn, newBook);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
			logFile.write(timestamp + "  " + "INSERT/" + isbn + "/" + title + "/" + author + "/" + genre + "/" + year + "/" + price + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteData(String isbn){
		items--;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String title = retrieveTitle(isbn);
        String author = retrieveAuthor(isbn);
        String genre = retrieveGenre(isbn);
        String year = retrieveYear(isbn);
        String price = retrievePrice(isbn);
        try {
			logFile.write(timestamp + "  " +"DELETE/"+ isbn + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		table.remove(isbn);
	}
	
	public void modifyPrice(String isbn, String newPrice){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Book bookToModify = queryBook(isbn);
        bookToModify.setPrice(newPrice);
        try {
			logFile.write(timestamp + "  " +"MODIFY/"+ isbn + "/" + newPrice + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(String u, String p){
		if(!user.contains(u)){
			user.put(u, p);
		}
	}
}
