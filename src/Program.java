import java.net.URI;
import java.net.URISyntaxException;

public class Program {
	public static void main(String[] args) {
		try {
			WebCrawler crawler = new WebCrawler();
			URI startLocation;

			// startLocation = new URI("http://ebusiness.free.bg");
			// URI link = crawler.crawl(startLocation, "Револвираща");
			// System.out.println("result :" + link.toString());
			// http://ebusiness.free.bg/cards_bank_cards.html

			startLocation = new URI("http://blog.hackbulgaria.com");
			URI link = crawler.crawl(startLocation,
							"Като страничен ефект, особено при момчетата, може да бъде бързо-растяща брада.");

			System.out.println("result :" + link.toString());
			// http://blog.hackbulgaria.com/fall-of-the-hackers/
			
			// startLocation = new URI("http://fmi.wikidot.com");
			// link = crawler.crawl(startLocation, "Докажете, че:");
			//
			// System.out.println("result :" + link.toString());
			// http://fmi.wikidot.com/anal-examination-1
		} catch (URISyntaxException e) {
			System.out.println("Invalid url");
		}
	}
}
