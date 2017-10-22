import java.awt.print.PrinterIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import utils.ExpandUrl;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//MongoDB.connectToDB();
		
		try {
	
			MongoDB.connectToDB();	//ESEGUIRE QUESTO
			
//			List<String> processed = new ArrayList<String>();
//			MongoDB.TESTDB();
			
//			String address = ExpandUrl.Expand("https://en.wikipedia.org/wiki/Recommender_system");
//			System.out.println(address);
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}
