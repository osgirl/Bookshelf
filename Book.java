
public class Book {
	
	private String isbn;
	private String title;
	private String author;
	private String genre;
	private String year;
	private String price;
	
	public Book(String i, String t, String a, String g, String y, String p){
		isbn = i;
		title = t;
		author = a;
		genre = g;
		year = y;
		price = p;
	}
	
	//ISBN
	public String getIsbn(){
		return isbn;
	}
	public void setIsbn(String i){
		isbn = i;
	}
	
	//Title
	public String getTitle(){
		return title;
	}
	public void setTitle(String t){
		title = t;
	}
	
	//Author
	public String getAuthor(){
		return author;
	}
	public void setAuthor(String a){
		author = a;
	}
	
	//Genre
	public String getGenre(){
		return genre;
	}
	public void setGenre(String g){
		genre = g;
	}
	
	//Year
	public String getYear(){
		return year;
	}
	public void setYear(String y){
		year = y;
	}
	
	//Price
	public String getPrice(){
		return price;
	}
	public void setPrice(String p){
		price = p;
	}
}

