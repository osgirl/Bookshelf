
public class User {
	
	private BookStorage usersBooks = null;
	private String username = "";
	private String password = "";
	
	public User(String username, String password){
		usersBooks = new BookStorage();
		this.username = username;
		this.password = password;
	}
	
	public BookStorage getBookStorage(){
		return usersBooks;
	}
	
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
}
