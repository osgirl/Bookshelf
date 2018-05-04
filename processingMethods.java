import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class processingMethods {
	
	private BookStorage currentUserStorage = null;
	private Search titles = null;
	BufferedWriter receipt = null;
	
	public processingMethods(BookStorage currentUserStorage){
		this.currentUserStorage = currentUserStorage;
	}
	
	public void processArgs(String[] args){
		//need to add more flags and add more conditions
		for(int i=0; i<args.length; i++){
			if(args[i].equals("-i")){
				if(args[i+1].startsWith("-")){
					System.out.println("Please enter a textfile");
				}
				else{
					readInputFile(args[i+1]);
					//readUrls(args[i+1]);
					i++;
				}
			}
			else if(args[i].equals("-o")){
				//createOutput(args[i+1]);
				i++;
			}
			else if(args[i].equals("-r")){
				//no guis
				//process input file
				
			}
		}
	}
	
	public void readInputFile(String inputFile){
		//Storage userInputFile = new Storage();
		ArrayList<String> inputs = new ArrayList<String>();
		ArrayList<String> titlesToSearch = new ArrayList<String>();
		File inFile = new File(inputFile);
		String title;
		try {
			Scanner inputTitle = new Scanner(inFile);
			while(inputTitle.hasNext()){
				title = inputTitle.nextLine();
				inputs.add(title);
			}
			inputTitle.close();
			titlesToSearch = removeWhiteSpace(inputs);
			processDataInputs(titlesToSearch);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> removeWhiteSpace(ArrayList<String> titles) {
		ArrayList<String> newTitles = new ArrayList<String>();
		String newTitle = null;
		for(int i=0; i<titles.size(); i++){
			newTitle = titles.get(i).replaceAll("\\s+","+");
			newTitles.add(newTitle);
		}
		System.out.println(newTitles);
		return newTitles;
	}
	
	public void processDataInputs(ArrayList<String> inputs){
		titles = new Search(currentUserStorage);
		for(int i=0; i<inputs.size(); i++){
			titles.titleSearch(inputs.get(i));
		}
		titles.parseIndividualItems();
	}
	
	public void searchNewBook(String title, String isbn) {
		//titles.titleSearch(title);
		//guest account, cannot store data
		//-p flag no gui
		//load in log file from admin
		//image reading
		title = title.replaceAll("\\s+","+");
		titles.advancedBookSearch(title, isbn);
	}
	
	
	public void loadLogFile(String inputFile){
		//need to expand on commands
		File inFile = new File(inputFile);
		String isbn, title, author, genre, price, field, value, command = null;
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
					price = data.next();
					currentUserStorage.storeData(isbn, title, author, genre, price);
				}
				else if(command.equalsIgnoreCase("delete")){
					isbn = data.next();
					currentUserStorage.deleteData(isbn);
				}
				else if(command.equalsIgnoreCase("modify")){
					isbn = data.next();
					field = data.next();
					value = data.next();
					currentUserStorage.modified(isbn, field, value);
				}
			}
			data.close();
			try {
				currentUserStorage.logFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void generateReceipt() {
		try {
		    receipt = new BufferedWriter(new FileWriter("recepit.txt"));
			receipt.write("BOOKSHELF \n\n");
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			receipt.write(timestamp + " \n\n");
			double total = 0.0;
			double tax = 1.0825;
			String price = "";
			
			String str;
			Hashtable<String, Book> boughtBooks;
			boughtBooks = currentUserStorage.getBooks();
		    Set<String> keys = boughtBooks.keySet();
		    Iterator<String> itr = keys.iterator();
		    while (itr.hasNext()) { 
		       str = itr.next();
		       receipt.write("BOOK: " + boughtBooks.get(str).getTitle() +"\n");
		       receipt.write("AUTHOR: " + boughtBooks.get(str).getAuthor() +"\n");
		       receipt.write("ISBN: " + boughtBooks.get(str).getIsbn() +"\n");
		       receipt.write("GENRE: " + boughtBooks.get(str).getGenre() +"\n");
		       receipt.write("PRICE: " + boughtBooks.get(str).getPrice()  +"\n");
		       price = boughtBooks.get(str).getPrice();
		       price = price.substring(1, price.length());
		       total = total + Double.parseDouble(price);
		       receipt.write("------------\n");
		    }
		    receipt.write("----------------------------------------------------\n");
		    receipt.write("SUBTOTAL: " + total);
		    receipt.write("TAX " + tax);
		    receipt.write("TOTAL " + (total*tax));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void readUrls(String inputFile){
		File inFile = new File(inputFile);
		ArrayList<String> urls = new ArrayList<String>();
		try {
			Scanner url = new Scanner(inFile);
			while(url.hasNext()){
				urls.add(url.next());
			}
			url.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Search urlSearch = new Search(currentUserStorage);
		urlSearch.getUrlInfo(urls);
	}
	
	public void createOutput(String outputFile){
		//need to fix it up
		String str;
		try {
			BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));
			outFile.write("ISBN          " + "TITLE                    " + "AUTHOR              " + "GENRE       " + "PRICE " + "\n\n");

		    Set<String> keys = currentUserStorage.table.keySet();
		    
		    Iterator<String> itr = keys.iterator();

		    while (itr.hasNext()) { 
		       str = itr.next();
		       outFile.write(str + " " + currentUserStorage.table.get(str).getTitle()+" "+ currentUserStorage.table.get(str).getAuthor() +" "+ currentUserStorage.table.get(str).getGenre() +" "+currentUserStorage.table.get(str).getPrice()+"\n");
		    }
		    outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
