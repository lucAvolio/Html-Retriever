import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Model.Site;

import java.io.*;


public class URLReader {
	
	public static String getHTMLFromURL(String url) {
		String html = "";
		try {
			URL oracle = new URL(url);

			System.out.println("Downloading url "+url);

			HttpURLConnection uc = (HttpURLConnection) oracle.openConnection();
			uc.setInstanceFollowRedirects(true);
			HttpURLConnection.setFollowRedirects(true);
			uc.setReadTimeout(30000);
			String newUrl = uc.getHeaderField("Location");
			System.out.println(newUrl);
			//			BufferedReader in = new BufferedReader(
			//			new InputStreamReader(oracle.openStream()));


			//			String inputLine;
			//			while ((inputLine = in.readLine()) != null)
			//				html += inputLine;
			//			in.close();
			//			Document doc =  Jsoup.parse(htmlTxt);
			//			System.out.println("Url downloaded.");
			//			 return html;
			StringBuilder data;
			String inputLine;
			try (InputStreamReader isr = new InputStreamReader(uc.getInputStream(), "utf-8");
					BufferedReader in = new BufferedReader(isr)) {
				data = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					data.append(inputLine);
				}
			}
			System.out.println(data);

			return data.toString();
		} catch (Exception e) {
			System.out.println("ERRORE in getHTMLFromURL");
			System.out.println(e.getMessage());
			return "";
		}

	}

	public static Site HttpRedirect(String url){
		try {
			Site s = new Site();
			s.setUrl(url);
			
			URL obj = new URL(url);			
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setReadTimeout(5000);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");

			//System.out.println("Request URL ... " + url);

			boolean redirect = false;
			try{
				// normally, 3xx is redirect
				int status = conn.getResponseCode();
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
							|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
						redirect = true;
				}

				//System.out.println("Response Code ... " + status);

				if (redirect) {

					// get redirect url from "location" header field
					String newUrl = conn.getHeaderField("Location");

					// get the cookie if need, for login
					String cookies = conn.getHeaderField("Set-Cookie");

					// open the new connnection again
					conn = (HttpURLConnection) new URL(newUrl).openConnection();
					conn.setRequestProperty("Cookie", cookies);
					conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
					conn.addRequestProperty("User-Agent", "Mozilla");
					conn.addRequestProperty("Referer", "google.com");

					//System.out.println("Redirect to URL : " + newUrl);
					s.setUrl(newUrl);
				}


				BufferedReader in = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer html = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					html.append(inputLine);
				}
				in.close();
				String h = html.toString();
				String doc = "";
				if(h != "" && !h.isEmpty()) {
					doc = Jsoup.parse(h).text();
					System.out.println(doc.toString());
				}
				
				s.setHtml(doc);
				
				return s;
			}
			catch (IOException ex) {
				System.out.println("Failed to connect to URL");
			}

		} catch (Exception e) {
			System.out.println("ERRORE!!!!!!! in HttpRedirect");
			e.printStackTrace();
		}
		return null;
		

	}
}
