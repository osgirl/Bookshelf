import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * This is the Driver class in which will run the software. 
 * This class handles the execution of creating storage structures,
 * processing the command line arguments, and reading in input files
 * @author Kimberly Lalmansingh
 *
 */
public class Driver {
	
	//storage structures for the software
	//static BookStorage storedBooks = null; //stores books 
	static UserStorage users = null; //stores all users of the software
	
	public static void main(String[] args) throws MalformedURLException, IOException{
		//storedBooks = new BookStorage();
		users = new UserStorage();
		GUI bookShelf = new GUI(args, users.getUsers());

		//Search newsearch = new Search();
		//String title = "cats";
		//newsearch.itemSearch(title);
		//GUI gui = new GUI(storedBooks.getBooks(), users.getUsers());

	}
	

}
