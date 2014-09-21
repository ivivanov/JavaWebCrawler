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
	private final String AnchorBeginningPattern = "^.?.?/";
	// private final String UrlPattern =
	// "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private Map<String, String> urlParentUrl;

	public WebCrawler() {
		this.urlParentUrl = new HashMap<String, String>();
	}

	public URI crawl(URI root, String needle) {
		this.urlParentUrl.clear();
		Queue<URI> uriQueue = new LinkedList<URI>();
		uriQueue.add(root);
		urlParentUrl.put(root.toString(), "");
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
				String corectUrl = buildCorectUrl(tempUrl, root.toString());

				if (corectUrl.length() > 0
						&& !urlParentUrl.containsKey(corectUrl)) {
					urlParentUrl.put(corectUrl, currentUri.toString());
					uriQueue.add(createURI(corectUrl));
				}
			}
		}
		return createURI("");
	}

	public List<URI> findAll(URI root, String needle) {
		Queue<URI> uriQueue = new LinkedList<URI>();
		uriQueue.add(root);
		urlParentUrl.put(root.toString(), "");
		URI currentUri;
		List<URI> result = new LinkedList<URI>();

		while (uriQueue.peek() != null) {
			currentUri = uriQueue.poll();
			String html = getHtml(currentUri);

			if (html.contains(needle)) {
				result.add(currentUri);
			}

			LinkedList<String> links = getAllLinks(html);
			int linksCount = links.size();

			for (int i = 0; i < linksCount; i++) {
				String tempUrl = links.get(i);
				String fullUrl = "";

				if (isFullUrl(tempUrl)) {
					if (tempUrl.contains(root.toString())) {
						fullUrl = tempUrl;
					} else {
						fullUrl = "";
					}
				} else {
					fullUrl = root + "/" + tempUrl;
				}

				if (fullUrl.length() > 0 && !urlParentUrl.containsKey(fullUrl)) {
					System.out.println(fullUrl);
					urlParentUrl.put(fullUrl, currentUri.toString());
					uriQueue.add(createURI(fullUrl));
				}
			}
		}
		return result;
	}

	public void printPath(String url){
		System.out.println(this.getPath(url));
	}
	
	public List<String> getPath(String url) {
		List<String> path = new LinkedList<String>();
		path.add(url);
		String parent = this.urlParentUrl.get(url);
		while (parent != null && parent.length() > 0) {
			path.add(parent);
			String tmp = this.urlParentUrl.get(parent);
			parent = tmp;
		}

		// reverse
		List<String> reversedPath = new LinkedList<String>();
		for (int i = path.size() - 1; i >= 0; i--) {
			reversedPath.add(path.get(i));
		}

		return reversedPath;
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
			InputStreamReader reader = new InputStreamReader(
					conn.getInputStream());
			BufferedReader br = new BufferedReader(reader);
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

	private String buildCorectUrl(String tempUrl, String root) {
		String corectUrl = "";
		if (isFullUrl(tempUrl)) {
			if (tempUrl.contains(root)) {
				corectUrl = tempUrl;
			} else {
				corectUrl = "";
			}
		} else {
			corectUrl = root + "/"
					+ tempUrl.replaceAll(AnchorBeginningPattern, "");
		}
		return corectUrl;
	}
}