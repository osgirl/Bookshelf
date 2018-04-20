/**
 * The class is a Book object. A Book objects stores the 
 * isbn, title, author, genre, year, and price of the book
 * 
 * @author Kimberly Lalmansingh
 *
 */
public class Book {
	
	//data members of the Book object
	private String isbn;
	private String title;
	private String author;
	private String genre;
	private String price;
	
	//constructor when creating a new Book object
	public Book(String isbn, String title, String author, String genre, String price){
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.price = price;
	}
	
	//Getters and Setters
	
	//ISBN
	public String getIsbn(){
		return isbn;
	}
	public void setIsbn(String isbn){
		this.isbn = isbn;
	}
	
	//Title
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	//Author
	public String getAuthor(){
		return author;
	}
	public void setAuthor(String author){
		this.author = author;
	}
	
	//Genre
	public String getGenre(){
		return genre;
	}
	public void setGenre(String genre){
		this.genre = genre;
	}
	
	//Price
	public String getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price = price;
	}
} // end class Book

