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
	private String user2 = "goldberg";
	private String pass2 = "370";
	private String user3 = "1";
	private String pass3 = "2";
	
	/**
	 * Constructor
	 */
	public UserStorage(){
		User adminUser = new User(admin, adminpass);
		User regularUser1 = new User(user1, pass1);
		User regularUser2 = new User(user2, pass2);
		User regularUser3 = new User(user3, pass3);

		users = new Hashtable<String, User>();
		users.put(admin, adminUser);
		users.put(user1, regularUser1);
		users.put(user2, regularUser2);
		users.put(user3, regularUser3);
		
	}
	
	public Hashtable<String, User> getUsers(){
		return users;
	}
	
	
	/**
	 * @param username - username to add
	 * @param password - password to add 
	 */
	private void addUser(String username, String password){
		if(!users.containsKey(username)){
			User newUser = new User(username, password);
			users.put(username, newUser);
		}
	}
	
	/**
	 * 
	 * @param username - user to remove
	 */
	private void deleteUser(String username){
		if(users.containsKey(username)){
			users.remove(username);
		}
	}
	
	/**
	 * 
	 * @param username - user to check
	 * @return true if it is admin, false otherwise
	 */
	private boolean isAdmin(String username){
		if(username==admin) return true;
		else return false;
	}
	
	/**
	 * 
	 * @param username - user to check
	 * @return true if user exists, false otherwise
	 */
	private boolean userExists(String username){
		if(users.containsKey(username)) return true;
		else return false;
	}
}
