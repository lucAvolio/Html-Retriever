import java.awt.print.PrinterIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//MongoDB.connectToDB();
		
		try {
	
			MongoDB.connectToDB();
//			URLReader.HttpRedirect("https://lumi.news/p/04kd3wnq5c4gy");
			//MongoDB.getHtml();
//			OutputWriterReader.writeLastInserted("113410319=12");
//			System.out.println(OutputWriterReader.checkLastInserted("113410319"));
//			List<Entry<String, Long>> map = MongoDB.getTweetsByUser();
//			System.out.println(map);

			
//			List<String> processed = new ArrayList<String>();
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}
