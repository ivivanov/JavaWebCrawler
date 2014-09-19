import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crowler {

	private final String ExtractUrlFromAnchorPattern = "(?<=<a href=\"{1}).+?(?=\")";
//	private final String UrlPattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private HashSet<String> urlSet;

	public Crowler() {
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

	private String getHtml(String urlStr) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF8"));
			String inputLine;

			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}

			br.close();
		} catch (MalformedURLException e) {
//			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
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

	public String crowl(String root,String needle) {
		Queue<String> urlQueue = new LinkedList<String>();
		urlQueue.add(root);
		urlSet.add(root);
		String currentUrl;

		while (urlQueue.peek() != null) {
			currentUrl = urlQueue.poll();
			String html = getHtml(currentUrl);
			if(html.contains(needle)){
				return currentUrl;
			}
			LinkedList<String> links = getAllLinks(html);
			int linksCount = links.size();

			for (int i = 0; i < linksCount; i++) {
				String tempUrl = links.get(i);

				if (!urlSet.contains(tempUrl)) {
					urlSet.add(tempUrl);

					if (isFullUrl(tempUrl)) {
						if (tempUrl.contains(root)) {
							urlQueue.add(tempUrl);
//							System.out.println(tempUrl);
						} else {
							// skip this url
//							System.out.println("skip: " + tempUrl);
						}
					} else {
						String fullUrl = root + "/" + tempUrl;
						urlQueue.add(fullUrl);
//						System.out.println(fullUrl);
					}
				}
			}
		}
		return "not found";

	}
}