import java.net.URI;
import java.net.URISyntaxException;

public class Program {
	public static void main(String[] args) {

		try {
			WebCrawler crawler = new WebCrawler();
			URI startLocation;

			startLocation = new URI("http://ebusiness.free.bg");
			URI link = crawler.crawl(startLocation, "Револвираща");
			System.out.println("result :" + link.toString());
			crawler.printPath(link.toString());
			// http: // ebusiness.free.bg/cards_bank_cards.html

			startLocation = new URI("http://zeron.bg");
			link = crawler.crawl(startLocation,
					"поддържа работа освен на български език и още на 12");
			System.out.println("result :" + link.toString());
			crawler.printPath(link.toString());
			// http://zeron.bg/Products/Multilanguage

			startLocation = new URI("http://blog.hackbulgaria.com");
			link = crawler
					.crawl(startLocation,
							"Като страничен ефект, особено при момчетата, може да бъде бързо-растяща брада.");
			System.out.println("result :" + link.toString());
			crawler.printPath(link.toString());
			// http://blog.hackbulgaria.com/fall-of-the-hackers/

			 startLocation = new URI("http://fmi.wikidot.com");
			 link = crawler.crawl(startLocation, "Докажете, че:");
			 System.out.println("result :" + link.toString());
			 crawler.printPath(link.toString());
			// http: // fmi.wikidot.com/anal-examination-1

			// find all
			// List<URI> allResults = crawler.findAll(new
			// URI("http://comac-medical.com"), "Vrabevski");
			// System.out.print(allResults.toString());
		} catch (URISyntaxException e) {
			System.out.println("Invalid url");
		}
	}
}
