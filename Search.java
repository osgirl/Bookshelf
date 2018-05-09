import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;


public class Search {

	//data members for class
	private String url = "";
	private String itemUrl = "";
	private String primaryRegex = "";
	private String isbnRegex = "";
	private String titleRegex = "";
	private String authorRegex = "";
	private String genreRegex = "";
	private String priceRegex = "";
	private String advancedSearchRegex = "";
	
	private ArrayList<String> allTitleHrefResults;
	private BookStorage bookStorage = null;
    BufferedWriter writer = null;

	
	public Search(BookStorage bookStorage){
		this.bookStorage = bookStorage;
		//regex patterns for data scraping
		url = "https://www.barnesandnoble.com/s/";
		itemUrl = "https://www.barnesandnoble.com/";
		isbnRegex = "(\\.setTargeting\\('sku', '(.*?)\')";
		titleRegex = "(\\.setTargeting\\('title', '(.*?)\')";
		authorRegex = "(\\.setTargeting\\('author', '(.*?)\')";
		genreRegex = "(\\.setTargeting\\('cat1', '(.*?)\')";
		priceRegex = "(Current price is (.*?),)";
		primaryRegex = "(<a class=\"pImageLink \".*?href=\"(.*?);.*?Author?)";
		
		//holds matching regex which contains information
		//about a book
		allTitleHrefResults = new ArrayList<String>();
	}
	
    public String getinfo(String itemUrl, String titleUrlFile) throws IOException {
    	//function will get url info and store it into a text file for future processing
    	//this gets info for one title with many results
    	String itemHtml = "";
    	String outputFile = titleUrlFile;
    	BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        // Display the URL address, and information about it.
    	BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(itemUrl).openStream()));
		String line = reader.readLine();

		while (line != null) {
			writer.write(line + "\n");
			itemHtml = itemHtml + line;
			line = reader.readLine(); 
		} // while
		writer.close();
		reader.close();
		return itemHtml;
    }
    
    public void titleSearch(String title, BufferedWriter outFile){
    	//search B&N based on titles given by the user
    	String titleUrl = url + title;
    	String titleUrlFile = title + ".txt";
    	String itemHtml= "";
    	try {
    		try {
    			//write to output file
				outFile.write("Searched Barnes & Noble Url: " + titleUrl + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
    		//get the entire html for searched title which includes all books 
    		//with that title
			itemHtml = getinfo(titleUrl, titleUrlFile);
			//send html to function for parsing to find first book in list
			//that matches title the best
			parseHtml(itemHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void parseHtml(String htmlToParse){ 
    	//send html to regex matching to filter out results
    	//primary regex returns info for one book 
    	allTitleHrefResults.add(patternMatcher(htmlToParse, primaryRegex));
    }
    
    private String patternMatcher(String parse, String regex) {
    	//all regex matching occurs here
    	String info = "";
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(parse);
    	if(matcher.find()){
        	info = matcher.group(2);
    	}
    	//return indicated group
    	return info;
    }
    
    public void parseIndividualItems() {
    	//this will go through all titles needing to be search
    	for(int i=0; i<allTitleHrefResults.size(); i++){
    		getBookInfo(allTitleHrefResults.get(i));
    	}
    }
    
    private void getBookInfo(String bookUrl) {
    	//given a title, function will scrape a new webpage to get just
    	//the information of that one book
    	String isbn = "", title = "", author = "", genre = "", price = "";
    	String url = itemUrl + bookUrl;
    	String bookHtml = "";
		String line;
		try {
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
	    	String outputFile = "book.txt";
	    	BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			line = reader.readLine();
			while (line != null) {
				//write book information to text file
				bookHtml = bookHtml + line;
				writer.write(line + "\n");
				line = reader.readLine(); 
			} // while
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//use regex to parse out required information for storing
		isbn = patternMatcher(bookHtml, isbnRegex);
		title = patternMatcher(bookHtml, titleRegex);
		author = patternMatcher(bookHtml, authorRegex);
		genre = patternMatcher(bookHtml, genreRegex);
		price = patternMatcher(bookHtml, priceRegex);
		//store the book
		bookStorage.storeData(isbn, title, author, genre, price);
    }
    
    public int advancedBookSearch(String title, String isbn) {
    	//function is used for the advanced search feature 
    	String titleUrl = url + title;
    	String titleUrlFile = title + ".txt";
    	String itemHtml = "";
    	int checkSearch = 0;
    	try {
			itemHtml = getinfo(titleUrl, titleUrlFile); //returns webpages with all results
			checkSearch = getAdvancedBookInfo(itemHtml, isbn); //search for exact book
			if(checkSearch == 1) {
				return 1; //return 1 if no result is found
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return 0;
    }
    
    private int getAdvancedBookInfo(String itemHtml, String isbnAS) {
		String bookHtmlAS = findCorrectBook(itemHtml, isbnAS);
		if(bookHtmlAS.equals("no match")){
			return 1; //if no book is found
		}
		else{
			getBookInfo(bookHtmlAS); //scrape info on that book
		}
		return 0;
    }
    
    private String findCorrectBook(String bookHtml, String isbn){
    	//function will find exact book in the list of results given by B&N
    	//this is for advanced search feature
    	String info = "";
		advancedSearchRegex = "(<a class=\"pImageLink \".*?href=\"(.*?);.*?ean=("+isbn+")\">.*?Author?)";

    	Pattern pattern = Pattern.compile(advancedSearchRegex);
    	Matcher matcher = pattern.matcher(bookHtml);
    	if(matcher.find()){
        	info = matcher.group(2);
    	}
    	else{
    		return "no match";
    	}
    	return info;
    }
    
    public BufferedImage featuredItem() {
    	String imageUrl = "https://prodimage.images-bn.com/pimages/9780785168508_p0_v2_s550x406.jpg";
    		URL url = null;
			try {
				url = new URL(imageUrl);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
    		BufferedImage image = null;
    		File outputImage = new File("Image.jpg");
			try {
				image = ImageIO.read(url);
				//ImageIO.write(image, "jpg", outputImage);
			} catch (IOException e) {
				e.printStackTrace();
			} // catch
			return image;
    }
    
    
    
    public void getUrlInfo(ArrayList<String> urls) {
    	// generates output file for url searching
    	String outputFile = "output.txt";
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	for(int i=0; i<urls.size(); i++) {
    		String[] urlType = urls.get(i).split("\\.");
    		if(urlType[urlType.length-1].equals("html") || urlType[urlType.length-1].equals("htm") || urlType[urlType.length-1].equals("txt")){
    			readHtml(urls.get(i));
    		}
    	}
    	try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void readHtml(String url) {
    	//reads the html for urls that are searched
    	int numOfLines = 0;
		try {
	        // Display the URL address, and information about it.
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String line = reader.readLine();
			writer.write(url + "\n");
			while (line != null) {
				writer.write(line + "\n");
				numOfLines++;
				line = reader.readLine(); 
			} // while
			writer.write(numOfLines + "\n");
			writer.write("\n\n\n");

		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}
