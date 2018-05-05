import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;


public class Search {

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

	
	public Search(BookStorage bookStorage){
		this.bookStorage = bookStorage;
		url = "https://www.barnesandnoble.com/s/";
		itemUrl = "https://www.barnesandnoble.com/";
		isbnRegex = "(\\.setTargeting\\('sku', '(.*?)\')";
		titleRegex = "(\\.setTargeting\\('title', '(.*?)\')";
		authorRegex = "(\\.setTargeting\\('author', '(.*?)\')";
		genreRegex = "(\\.setTargeting\\('cat1', '(.*?)\')";
		priceRegex = "(Current price is (.*?),)";
		primaryRegex = "(<a class=\"pImageLink \".*?href=\"(.*?);.*?Author?)";
		
		allTitleHrefResults = new ArrayList<String>();
	}
	
    public String getinfo(String itemUrl, String titleUrlFile) throws IOException {
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
    	String titleUrl = url + title;
    	String titleUrlFile = title + ".txt";
    	String itemHtml= "";
    	try {
    		try {
				outFile.write("Searched Barnes & Noble Url: " + titleUrl + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			itemHtml = getinfo(titleUrl, titleUrlFile);
			parseHtml(itemHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void parseHtml(String htmlToParse){ 
    	allTitleHrefResults.add(patternMatcher(htmlToParse, primaryRegex));
    }
    
    private String patternMatcher(String parse, String regex) {
    	String info = "";
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(parse);
    	if(matcher.find()){
        	info = matcher.group(2);
    	}
    	return info;
    }
    
    public void parseIndividualItems() {
    	for(int i=0; i<allTitleHrefResults.size(); i++){
    		getBookInfo(allTitleHrefResults.get(i));
    	}
    }
    
    private void getBookInfo(String bookUrl) {
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
				bookHtml = bookHtml + line;
				writer.write(line + "\n");
				line = reader.readLine(); 
			} // while
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isbn = patternMatcher(bookHtml, isbnRegex);
		title = patternMatcher(bookHtml, titleRegex);
		author = patternMatcher(bookHtml, authorRegex);
		genre = patternMatcher(bookHtml, genreRegex);
		price = patternMatcher(bookHtml, priceRegex);
		bookStorage.storeData(isbn, title, author, genre, price);
    }
    
    public int advancedBookSearch(String title, String isbn) {
    	String titleUrl = url + title;
    	String titleUrlFile = title + ".txt";
    	String itemHtml = "";
    	int checkSearch = 0;
    	try {
			itemHtml = getinfo(titleUrl, titleUrlFile); //returns webpages with all results
			checkSearch = getAdvancedBookInfo(itemHtml, isbn);
			if(checkSearch == 1) {
				return 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return 0;
    }
    
    private int getAdvancedBookInfo(String itemHtml, String isbnAS) {
		String bookHtmlAS = findCorrectBook(itemHtml, isbnAS);
		if(bookHtmlAS.equals("no match")){
			return 1;
		}
		else{
			getBookInfo(bookHtmlAS);
		}
		return 0;
    }
    
    private String findCorrectBook(String bookHtml, String isbn){
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
    
    
    BufferedWriter writer = null;
    
    public void getUrlInfo(ArrayList<String> urls) {
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
