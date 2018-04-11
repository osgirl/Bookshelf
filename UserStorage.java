import java.util.Hashtable;

/**
 * UserStorage is the 'database' of all users of the software
 * 
 * @author Kimberly Lalmansingh
 *
 */
public class UserStorage {
	Hashtable<String, String> user;
	private String admin = "Admin";
	private String adminpass = "pass0000";
	
	/**
	 * Constructor
	 */
	public UserStorage(){
		user = new Hashtable<String, String>();
		user.put(admin, adminpass);
	}
	
	public Hashtable<String, String> getUsers(){
		return user;
	}
	
	
	/**
	 * @param username - username to add
	 * @param password - password to add 
	 */
	private void addUser(String username, String password){
		if(!user.containsKey(username)){
			user.put(username, password);
		}
	}
	
	/**
	 * 
	 * @param username - user to remove
	 */
	private void deleteUser(String username){
		if(user.containsKey(username)){
			user.remove(username);
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
		if(user.containsKey(username)) return true;
		else return false;
	}
}
