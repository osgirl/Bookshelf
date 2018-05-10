import java.util.Hashtable;

/**
 * UserStorage is the 'database' of all users of the software
 * 
 * @author Kimberly Lalmansingh
 *
 */
public class UserStorage {
	Hashtable<String, User> users;
	private String admin = "Admin";
	private String adminpass = "pass0000";
	private String user1 = "kim";
	private String pass1 = "pass";
	private String user2 = "Guest";
	private String pass2 = null;

	
	/**
	 * Constructor
	 */
	public UserStorage(){
		User adminUser = new User(admin, adminpass);
		User regularUser1 = new User(user1, pass1);
		User regularUser2 = new User(user2, pass2);

		users = new Hashtable<String, User>();
		users.put(admin, adminUser);
		users.put(user1, regularUser1);
		users.put(user2, regularUser2);		
	}
	
	/**
	 * 
	 * @return hashtable containing users
	 */
	public Hashtable<String, User> getUsers(){
		return users;
	}
	
	
	/**
	 * @param username - username to add
	 * @param password - password to add 
	 */
	public void addUser(String username, String password){
			User newUser = new User(username, password);
			users.put(username, newUser);
	}
	
	/**
	 * 
	 * @param username - user to remove
	 */
	public void deleteUser(String username){
		if(users.containsKey(username)){
			users.remove(username);
		}
	}
	
	/**
	 * 
	 * @param username - user to check
	 * @return true if it is admin, false otherwise
	 */
	public boolean isAdmin(String username){
		if(username.equals("Admin")) return true;
		else return false;
	}
	
	/**
	 * 
	 * @param username - user to check
	 * @return true if user exists, false otherwise
	 */
	public boolean userExists(String username){
		if(users.containsKey(username)) return true;
		else return false;
	}
} //end class UserStorage
