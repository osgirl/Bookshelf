import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Processing Methods hold functions for processing inputs and outputs of the software
 * 
 * @author Kimberly Lalmansingh
 *
 */

public class processingMethods {
	
	//global data members
	private BookStorage currentUserStorage = null;
	private Search titles = null;
	BufferedWriter receipt = null;
	BufferedWriter outFile = null;
	
	//constructor
	public processingMethods(BookStorage currentUserStorage){
		this.currentUserStorage = currentUserStorage;
	}
	
	//process input arguments
	public void processArgs(String[] args){
		String inputFile = "";
		String outputFile = "";
		boolean input = false;
		boolean output=false;
		for(int i=0; i<args.length; i++){
			//input file
			if(args[i].equals("-i")){
				if(args[i+1].startsWith("-")){
					System.out.println("Please enter a textfile. Exiting software now.");
					System.exit(0);
				}
				else{
					inputFile = args[i+1];
					input=true;
					i++;
				}
			}
			//read a url file
			else if(args[i].equals("-u")){
				readUrls(args[i+1]);
				i++;
			}
			//create output
			else if(args[i].equals("-o")){
				outputFile = args[i+1];
				output=true;
				i++;
			}
			//load in log file
			else if(args[i].equals("-l")){
				loadLogFile("logFile.txt");
				i++;
				
			}
		}
		if(output){
			createOutput(outputFile); //will create an output file if output flag was present
		}
		if(input){
			readInputFile(inputFile); //will read in the inputs if input flag was present
		}
	}
	
	//process the input file
	public void readInputFile(String inputFile){
		ArrayList<String> inputs = new ArrayList<String>(); //stores inputs
		//will hold the titles that will allow the searching, it cannot have spaces
		ArrayList<String> titlesToSearch = new ArrayList<String>(); 
		File inFile = new File(inputFile);
		String title;
		//read in from input file and store in array list
		try {
			Scanner inputTitle = new Scanner(inFile);
			while(inputTitle.hasNext()){
				title = inputTitle.nextLine();
				inputs.add(title);
				try {
					//write to output file
					outFile.write("Added to Searching list: " + title + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			inputTitle.close();
			//will modify the titles and store them as required to allow searching
			titlesToSearch = removeWhiteSpace(inputs);
			//process the titles 
			processDataInputs(titlesToSearch);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> removeWhiteSpace(ArrayList<String> titles) {
		//remove white spaces and adds "+" between spaces to allow searching 
		ArrayList<String> newTitles = new ArrayList<String>();
		String newTitle = null;
		for(int i=0; i<titles.size(); i++){
			newTitle = titles.get(i).replaceAll("\\s+","+");
			newTitles.add(newTitle);
		}
		return newTitles;
	}
	
	public void processDataInputs(ArrayList<String> inputs) {
		//process searching the titles
		titles = new Search(currentUserStorage);
		for(int i=0; i<inputs.size(); i++){
			titles.titleSearch(inputs.get(i), outFile);
		}
		titles.parseIndividualItems();
	}
	
	public int searchNewBook(String title, String isbn) {
		int checkSearch = 0;
		try {
			outFile.write("Advanced Search on: Title: " + title + " ISBN: " + isbn + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		title = title.replaceAll("\\s+","+");
		checkSearch = titles.advancedBookSearch(title, isbn);
		if(checkSearch==1){
			return 1;
		}
		return 0;
		
	}
	
	
	public void loadLogFile(String inputFile){
		
		File inFile = new File(inputFile);
		String isbn, title, author, genre, price, field, value, command = null;
		try {
			//read in each command in log file
			Scanner data = new Scanner(inFile);
			data.useDelimiter("/|\\n");
			while(data.hasNext()){
				command = data.next();
				//if something was inserted
				if(command.equalsIgnoreCase("insert")){
					isbn = data.next();
					title = data.next();
					author = data.next();
					genre = data.next();
					price = data.next();
					currentUserStorage.storeData(isbn, title, author, genre, price);
				}
				//if something was deleted
				else if(command.equalsIgnoreCase("delete")){
					isbn = data.next();
					currentUserStorage.deleteData(isbn);
				}
				//if something was modified
				else if(command.equalsIgnoreCase("modify")){
					isbn = data.next();
					field = data.next();
					value = data.next();
					currentUserStorage.modifyData(isbn, field, value);
				}
			}
			data.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void generateReceipt() {
		try {
			//create a receipt with the currently stored book in the 
			//users storage
		    receipt = new BufferedWriter(new FileWriter("receipt.txt"));
			receipt.write("BOOKSHELF \n");
			receipt.write("Sold by: Kimberly \n");
			receipt.write("In partnership with: Barnes & Nobles \n\n");
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			receipt.write("Date and Time: " + timestamp + " \n\n");
			double total = 0.0;
			double tax = 1.0825;
			String price = "";
			
			String str;
			Hashtable<String, Book> boughtBooks;
			boughtBooks = currentUserStorage.getBooks();
		    Set<String> keys = boughtBooks.keySet();
		    Iterator<String> itr = keys.iterator();
			receipt.write("Number of items bought: " + currentUserStorage.getItems() + "\n\n");
		    while (itr.hasNext()) { 
		    	//write all book information
		       str = itr.next();
		       receipt.write("BOOK: " + boughtBooks.get(str).getTitle() +"\n");
		       receipt.write("AUTHOR: " + boughtBooks.get(str).getAuthor() +"\n");
		       receipt.write("ISBN: " + boughtBooks.get(str).getIsbn() +"\n");
		       receipt.write("GENRE: " + boughtBooks.get(str).getGenre() +"\n");
		       if(boughtBooks.get(str).getPrice().equals("")){
			       receipt.write("PRICE: " + "$0.00"  +"\n");
		       }
		       else{
		    	   //calculate price of all the books in total
		    	   receipt.write("PRICE: " + boughtBooks.get(str).getPrice()  +"\n");
		    	   price = boughtBooks.get(str).getPrice();
			       price = price.substring(1, price.length());
			       total = total + Double.parseDouble(price);
		       }
		       receipt.write("------------\n");
		    }
		    receipt.write("----------------------------------------------------\n");
		    receipt.write("SUBTOTAL: $" + total + "\n");
		    receipt.write("TAX " + tax + "\n");
		    DecimalFormat totalInDecimal = new DecimalFormat(".##");
		    double totalWithTax = total*tax;
		    String result = "";
		    result = totalInDecimal.format(totalWithTax);
		    receipt.write("TOTAL $" + result);
		    
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//getters and setters
	public BufferedWriter getReceipt(){
		return receipt;
	}
	
	public BufferedWriter getOutFile(){
		return outFile;
	}
	
	//read in url information
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
	
	//create an output file if flag was present
	public void createOutput(String outputFile){
		try {
			outFile = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
