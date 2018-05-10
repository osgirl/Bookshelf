/**
 * User class for each user
 * 
 * @author kimberlylalmansingh
 *
 */

public class User {
	
	//data members for User class
	private BookStorage usersBooks = null;
	private String username = "";
	private String password = "";
	
	//constructor
	public User(String username, String password){
		//book storage for user is created
		//each user has their own book storage
		usersBooks = new BookStorage();
		this.username = username;
		this.password = password;
	}
	
	//getters and setters
	public BookStorage getBookStorage(){
		return usersBooks;
	}
	
	//getter and setter for username
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	
	//getter and setter for password
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
} //end class User
