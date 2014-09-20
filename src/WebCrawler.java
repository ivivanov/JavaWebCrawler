import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

	private final String ExtractUrlFromAnchorPattern = "(?<=<a href=\"{1}).+?(?=\")";
	// private final String UrlPattern =
	// "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private HashSet<String> urlSet;

	public WebCrawler() {
		this.urlSet = new HashSet<String>();
	}

	private LinkedList<String> getAllLinks(String html) {
		Pattern regex = Pattern.compile(ExtractUrlFromAnchorPattern);
		Matcher m = regex.matcher(html);
		LinkedList<String> urls = new LinkedList<String>();
		while (m.find()) {
			urls.add(html.substring(m.start(), m.end()));
		}

		return urls;
	}

	private String getHtml(URI uri) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(uri.toString());
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF8"));
			String inputLine;

			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}

			br.close();
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		return sb.toString();
	}

	private boolean isFullUrl(String input) {
		// Pattern regex = Pattern.compile(ExtractUrlFromAnchorPattern);
		// Matcher m = regex.matcher(input);
		// return m.find();
		try {
			@SuppressWarnings("unused")
			URL temp = new URL(input);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	private URI createURI(String link) {
		URI result;
		try {
			result = new URI(link);
			return result;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public URI crawl(URI root, String needle) {
		Queue<URI> uriQueue = new LinkedList<URI>();
		uriQueue.add(root);
		urlSet.add(root.toString());
		URI currentUri;
		
		while (uriQueue.peek() != null) {
			currentUri = uriQueue.poll();
			String html = getHtml(currentUri);

			if (html.contains(needle)) {
				return currentUri;
			}

			LinkedList<String> links = getAllLinks(html);
			int linksCount = links.size();

			for (int i = 0; i < linksCount; i++) {
				String tempUrl = links.get(i);

				if (!urlSet.contains(tempUrl)) {
					urlSet.add(tempUrl);

					if (isFullUrl(tempUrl)) {
						if (tempUrl.contains(root.toString())) {

							uriQueue.add(createURI(tempUrl));

							// System.out.println(tempUrl);
						} else {
							// skip this url
							// System.out.println("skip: " + tempUrl);
						}
					} else {
						String fullUrl = root + "/" + tempUrl;
						uriQueue.add(createURI(fullUrl));
						// System.out.println(fullUrl);
					}
				}
			}
		}
		return createURI("");
	}
}