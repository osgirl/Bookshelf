import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Search {

	private String url = "https://www.barnesandnoble.com/s/";
	
	public Search(){
		
	}
	
	
	public void itemSearch(String title) throws IOException, MalformedURLException{
		//String itemUrl = url + title;
		//getinfo(itemUrl);
		//getinfo("https://www.barnesandnoble.com/w/the-fault-in-our-stars-john-green/1104045488?ean=9780142424179#/");
		getinfo("https://www.barnesandnoble.com/s/papertowns");
	}
	
    public void getinfo(String itemUrl) throws IOException {
    	String outputFile = "html.txt";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        // Display the URL address, and information about it.
    	BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(itemUrl).openStream()));
		String line = reader.readLine();

		System.out.println(itemUrl);
		while (line != null) {
			writer.write(line + "\n");
			line = reader.readLine(); 
		} // while
    }
    
    public void titleSearch(String title){
    	String titleUrl = url + title;
    	String titleUrlFile = "html.txt";
    	try {
			getinfo(titleUrl);
			parseHtml(titleUrlFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void parseHtml(String fileToParse){
    	ArrayList<String> allResults = new ArrayList<String>();
    	//<a class="pImageLink "
    	//<a class="pImageLink([A-Za-z0-9 "'=_\-/();.?<>:,]*)align/>
    }
}
