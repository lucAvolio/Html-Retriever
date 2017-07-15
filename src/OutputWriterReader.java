import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriterReader {

	public static String[] checkLastInserted() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("UserToTweet.txt"));
			
//			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			
			while (line != null) {
				String[] stringParts = line.split("=");
				br.close();
				return stringParts;
				
//		        sb.append(line);
//		        sb.append(System.lineSeparator());
//		        line = br.readLine();
		        
		    }
			br.close();
			return null;
		} catch (IOException ex) {
			System.out.println("ERRORE in checkLastInserted");
			System.out.println(ex.toString());
			return null;
		}
	}

	public static boolean writeLastInserted(String s) {
		BufferedWriter out = null;
		try  
		{
		    FileWriter fstream = new FileWriter("UserToTweet.txt", false); //true tells to append data.
		    out = new BufferedWriter(fstream);
		    out.write(s);
		    out.close();
		}
		catch (IOException e)
		{
			System.out.println("Errore in writeLastInserted");
		    System.err.println("Error: " + e.getMessage());
		}
		return true;
	} 
}
