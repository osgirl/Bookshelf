import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Hashtable;

public class BookStorage {
	
	Hashtable<String, Book> table;
	BufferedWriter logFile = null;
	private int items = 0;
	
	//constructor
	public BookStorage(){
		table = new Hashtable<String, Book>();
		try {
			//create log file
			logFile = new BufferedWriter(new FileWriter("logFile.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//getters and setters
	public Hashtable<String, Book> getBooks(){
		return table;
	}
	
	//return an entire book
	private Book queryBook(String isbn){
		return table.get(isbn);
	}
	
	//get isbn of a book
	private String retrieveISBN (String isbn){
		return table.get(isbn).getIsbn();
	}
	
	//get title of a book
	private String retrieveTitle(String isbn){
		return table.get(isbn).getTitle();
	}
	
	private String retrieveAuthor(String isbn){
		return table.get(isbn).getAuthor();
	}
	
	private String retrieveGenre(String isbn){
		return table.get(isbn).getGenre();
	}
	
	private String retrievePrice(String isbn){
		return table.get(isbn).getPrice();
	}
	
	public int getItems() {
		return items;
	}
	
	public void setItems(int i) {
		items = i;
	}
	
	//adding new book into storage
	public void storeData(String isbn, String title, String author, String genre, String price){
		items++;
		Book newBook = new Book(isbn, title, author, genre, price);
		table.put(isbn, newBook);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
			logFile.write(timestamp + "  " + "INSERT/" + isbn + "/" + title + "/" + author + "/" + genre + "/" + price + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//delete a book from storage
	public void deleteData(String isbn){
		items--;
		//time stamp for log file
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String title = retrieveTitle(isbn);
        String author = retrieveAuthor(isbn);
        String genre = retrieveGenre(isbn);
        String price = retrievePrice(isbn);
        //write to log file
        try {
			logFile.write(timestamp + "  " +"DELETE/"+ isbn + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		table.remove(isbn);
	}
	
	//modify a book in storage 
	public void modifyData(String isbn, String field, String modifiedValue){
		//time stamp for log file
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Book modifiedBook = table.get(isbn);
		if(field.equals("isbn")) modifiedBook.setIsbn(modifiedValue);
		else if(field.equals("title")) modifiedBook.setTitle(modifiedValue);
		else if(field.equals("author")) modifiedBook.setAuthor(modifiedValue);
		else if(field.equals("genre")) modifiedBook.setGenre(modifiedValue);
		else if(field.equals("price")) modifiedBook.setPrice(modifiedValue);
		//write to log file
        try {
			logFile.write(timestamp + "  " +"MODIFY/"+ isbn + "/" + field + "/" + modifiedValue + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
