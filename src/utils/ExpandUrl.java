package utils;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class ExpandUrl {


	public static String Expand(String address) {
		// TODO Auto-generated method stub
			String expandedURL ="";
			try{
				if(address!=null && !address.isEmpty() && !address.equals("")){
					URL url = new URL(address);

					HttpURLConnection connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); //using proxy may increase latency
					connection.setInstanceFollowRedirects(false);
					connection.connect();
					expandedURL = connection.getHeaderField("Location");
					connection.getInputStream().close();
				}}
			catch(Exception e){
				//	e.printStackTrace();
			}
			if(expandedURL!=null)
				return expandedURL;
			else
				return address;
		}


}
