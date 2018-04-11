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

public class Driver {
	
	static Storage storedBooks = null;
	static Storage users = null;
	
	public static void main(String[] args) throws MalformedURLException, IOException{
		storedBooks = new Storage();
		processArgs(args);
		//users = new Storage();
		
		Search newsearch = new Search();
		String title = "cats";
		newsearch.itemSearch(title);
		GUI gui = new GUI(storedBooks.table, storedBooks.user);

	}
	
	public static void processArgs(String[] args){
		//need to add more flags and add more conditions
		for(int i=0; i<args.length; i++){
			if(args[i].equals("-i")){
				if(args[i+1].startsWith("-")){
					System.out.println("Please enter a textfile");
				}
				else{
					loadInput(args[i+1]);
					//readInputFile(args[i+1]);
					i++;
				}
			}
			else if(args[i].equals("-o")){
				//createOutput(args[i+1]);
				i++;
			}
			else if(args[i].equals("-p")){
				//no guis
				//process input file
				
			}
		}
	}
	
	public static void readInputFile(String inputFile){
		//Storage userInputFile = new Storage();
		ArrayList<String> inputs = new ArrayList<String>();
		File inFile = new File(inputFile);
		String title;
		try {
			Scanner inputTitle = new Scanner(inFile);
			while(inputTitle.hasNext()){
				title = inputTitle.next();
				inputs.add(title);
			}
			inputTitle.close();
			//processDataInputs(inputs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void processDataInputs(ArrayList<String> inputs){
		Search titles = new Search();
		for(int i=0; i<inputs.size(); i++){
			titles.titleSearch(inputs.get(i));
		}
	}
	
	public static void loadInput(String inputFile){
		//need to expand on commands
		File inFile = new File(inputFile);
		String isbn, title, author, genre, year, price, command = null;
		try {
			Scanner data = new Scanner(inFile);
			data.useDelimiter("/|\\n");
			while(data.hasNext()){
				command = data.next();
				if(command.equalsIgnoreCase("insert")){
					isbn = data.next();
					title = data.next();
					author = data.next();
					genre = data.next();
					year = data.next();
					price = data.next();
					storedBooks.storeData(isbn, title, author, genre, year, price);
				}
				else if(command.equalsIgnoreCase("delete")){
					isbn = data.next();
					storedBooks.deleteData(isbn);
				}
				else if(command.equalsIgnoreCase("modify")){
					isbn = data.next();
					String newPrice = data.next();
					storedBooks.modifyPrice(isbn, newPrice);
				}
			}
			data.close();
			try {
				storedBooks.logFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void createOutput(String outputFile){
		//need to fix it up
		String str;
		try {
			BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));
			outFile.write("ISBN          " + "TITLE                    " + "AUTHOR              " + "GENRE       " + "YEAR " + "PRICE " + "\n\n");

		    Set<String> keys = storedBooks.table.keySet();
		    
		    Iterator<String> itr = keys.iterator();

		    while (itr.hasNext()) { 
		       str = itr.next();
		       outFile.write(str + " " + storedBooks.table.get(str).getTitle()+" "+ storedBooks.table.get(str).getAuthor() +" "+ storedBooks.table.get(str).getGenre() +" "+ storedBooks.table.get(str).getYear() +" "+storedBooks.table.get(str).getPrice()+"\n");
		    }
		    outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
