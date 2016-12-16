package webcrawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	
	private static Set<String> PagesVisited = new HashSet<String>();
	private static final int MAX_PAGES_AMOUNT = 20;
	private static Queue<String> fifo = new LinkedList<String>();

		public static void main(String[]args){
			System.out.println("Enter in root URL");
			Scanner Reader = new Scanner(System.in);
			String URL = Reader.nextLine().toLowerCase();
			System.out.println(URL.contains("http://"));
			URL = checkURL(URL);
			System.out.println(URL); 
			fifo.add(URL);
			try{
				
				while(fifo.isEmpty() == false && PagesVisited.size() < MAX_PAGES_AMOUNT){
					
					URL = fifo.remove();
					searchURL(URL);
					PagesVisited.add(URL);
				}
			}

			finally{
				Reader.close();
			}
			
			try{
				PrintWriter writer = new PrintWriter("links.txt", "UTF-8");
				for(String url: PagesVisited){
					writer.println(url);
				}
				writer.close();
				System.out.println("Webcrawling has finished");
			}
			
			catch(IOException error){
				System.out.println("Error on writing to file:" + error);
			}
			
			
		}
			
		
		
		public static void searchURL(String url){
			try{
			Connection connection = Jsoup.connect(url).userAgent("Mozilla");
			Document html = connection.get();
			Elements linkset = html.select("a[href]");
			for(Element link : linkset){
				if(PagesVisited.contains(link.absUrl("href"))== false){
				fifo.add(link.absUrl("href"));
				}
			}
			
			
			}
			catch(IOException error){
				System.out.println("Error on HTTP request:" + error);
			}
		}
		public static String checkURL(String url){
			if(url.contains("http://") == false ){ //minor check for http://
				url = "http://" + url;
			}
			return url;
		}
}
