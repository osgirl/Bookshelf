
public class Book {
	
	private String isbn;
	private String title;
	private String author;
	private String genre;
	private String year;
	private String price;
	
	public Book(String isbn, String title, String author, String genre, String year, String price){
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.year = year;
		this.price = price;
	}
	
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
	
	//Year
	public String getYear(){
		return year;
	}
	public void setYear(String year){
		this.year = year;
	}
	
	//Price
	public String getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price = price;
	}
}

