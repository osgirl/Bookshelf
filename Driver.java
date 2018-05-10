import java.io.IOException;
import java.net.MalformedURLException;


/**
 * This is the Driver class in which will run the software. 
 * This class handles the execution of creating storage structures,
 * processing the command line arguments, and reading in input files
 * @author Kimberly Lalmansingh
 *
 */
public class Driver {
	
	//stores all users of the software
	static UserStorage users = null; 
	
	public static void main(String[] args) throws MalformedURLException, IOException{
		users = new UserStorage();
		BookshelfInterface bookShelf = new BookshelfInterface(args, users);
	}
}
