import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import org.bson.BasicBSONObject;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import Model.Site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class MongoDB {


	public static void connectToDB() {
		try {
			
			//CONNECT TO DB
			MongoClient mongoClient = new MongoClient("localhost",27017);		
			 // Now connect to your databases
	         MongoDatabase db = mongoClient.getDatabase("twitterDB");
	         
	         MongoCollection<Document> coll = db.getCollection("tweets");
	         MongoCollection<Document> htmlColl = db.getCollection("html");
	         //one document
	         //Document myDoc = coll.find().limit(1);
	         
	         
	         List<Entry<String, Long>> map = MongoDB.getTweetsByUser();
	         String[] lastInserted = OutputWriterReader.checkLastInserted();
	          int skip=0;
	         if(lastInserted != null){
	        	 skip = Integer.parseInt(lastInserted[1]);
	        	 String key = lastInserted[0];
	         
	         	MongoDB.spliceMap(key, map);
	         }
	         

	         
	         //get 5 docs
	         for(Entry<String,Long> entry: map){
	        	 
	        	 int i = skip;
	        	 System.out.println(entry.getValue());
	        	 
	        	 Number id = Integer.parseInt(entry.getKey());
	        	 BasicDBObject q = new BasicDBObject("id_user",id);
	        	 
//	        	 long numDoc = htmlColl.count(q);
		        	 
		        	 FindIterable<Document> myDocTemp = coll.find(q).skip(skip);
		        	 myDocTemp = myDocTemp.limit(30);
		        	 MongoCursor<Document> myDoc = myDocTemp.iterator();
		        	 
		        	 
		        	 
		        	 
		             System.out.println("User: " + id);
		             int j = 0;
			         try {
			             while (myDoc.hasNext()) {
			            	 Document d = myDoc.next();
			            	 Object id_user = d.getOrDefault("id_user", null).toString();
			            	 Object id_Doc = d.getOrDefault("_id", null).toString();	 
			            	 List<String> docUrls = (List<String>) d.get("urls");
			            	 
			            	 for (String url : docUrls) {
			            		 try{
			            			 
			            			 if (url != null){
			            			 Site site = URLReader.HttpRedirect(url);
				            		 //update doc with html of the url
			            			 persistHTML(site.getHtml(),id_user,site.getUrl(),htmlColl, id_Doc, i);
				            		 
			            			 }
			            			 i++;
				            		 
			            		 } catch (Exception e) {
			            			 System.out.println("Errore in connectToDB - Riga 84");
			            			 System.out.println(e.getMessage());
			            		 }
			            	
							}
			            	OutputWriterReader.writeLastInserted(id_user.toString() + "=" + i);
			             }
			         } finally {
			             myDoc.close();
			         }
		 	     
	         }

	         //System.out.println(myDoc.get("urls"));
	         
	         mongoClient.close();
			
		} catch (NullPointerException ex) {
			System.out.println("Errore in connectToDB");
			System.out.println(ex.getMessage());
		}
	}
	
	private static void persistHTML(String html,Object id, String url, MongoCollection<Document> coll, Object docId, int i){
		try {
			
//			//CONNECT TO DB
//			MongoClient mongoClient = new MongoClient("localhost",27017);		
//			 // Now connect to your databases
//	         MongoDatabase db = mongoClient.getDatabase("twitterDB");
//	         
//	         MongoCollection<Document> coll = db.getCollection("html");
	         


	         //get 5 docs
	         Document doc = new Document()
	            			 		.append("url", url)
	            			 		.append("id_user",id)
	            			 		.append("html",html)
	            			 		.append("id_doc", docId);
	         coll.insertOne(doc);
	         
	         System.out.println(i + ") Id_user: "+ id + " - Url: " + url + " - TweetsIdDoc: " + docId);

//	         } finally {
//	             doc.close();
//	         }

	         //System.out.println(myDoc.get("urls"));
			
		} catch (Exception ex) {
			System.out.println("ERRORE in persistHTML");
			System.out.println(ex.getMessage());
		}
	}
	
	public static List<Entry<String, Long>> getTweetsByUser() {
		//CONNECT TO DB
		MongoClient mongoClient = new MongoClient("localhost",27017);		
		 // Now connect to your databases
         MongoDatabase db = mongoClient.getDatabase("twitterDB");
         
         MongoCollection<Document> coll = db.getCollection("tweets");
         //one document
         //Document myDoc = coll.find().limit(1);
         Map<String,Long> map = new HashMap<String, Long>();
         int i = 0;
         try {
        	 
        	 MongoCursor<Long> user_list = coll.distinct("id_user",Long.class).iterator();
        	 
        	 while(user_list.hasNext()) {
            	 
        		 BasicDBObject q = new BasicDBObject("id_user",user_list.next());
        		 //long res = coll.count(q);
        		 long res = 0;
        		 map.put(q.get("id_user").toString(), res);
        		 

        		 i++;
        	 };
        	 
        	 
			
		} catch (Exception e) {
			System.out.println("ERRORE in getTweetsByUser");
			System.out.println(e.getMessage());
		} finally {
         mongoClient.close();
         
		}
         List<Entry<String, Long>> result = entriesSortedByValues(map);
         //result = result.subList(0, 50);		
        
        return result;
        
	}
	
	public static void getHtml() {
		//CONNECT TO DB
		MongoClient mongoClient = new MongoClient("localhost",27017);		
		 // Now connect to your databases
         MongoDatabase db = mongoClient.getDatabase("twitterDB");
         
         MongoCollection<Document> htmlColl = db.getCollection("html");
         
    	 Number id = Integer.parseInt("7280142");
    	 BasicDBObject q = new BasicDBObject("id_user",id);
         
           MongoCursor<Document> myDoc = htmlColl.find().limit(1).iterator();

         int i = 0;
         while (myDoc.hasNext()) {
        	 System.out.println("ciao");
//        	 if(i == 5) {
//        		 myDoc.next().get("url");
//        	 }
//        	 i++;
         }
	}
	
	static <K,V extends Comparable<? super V>> 
    List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

Collections.sort(sortedEntries, 
    new Comparator<Entry<K,V>>() {
        @Override
        public int compare(Entry<K,V> e1, Entry<K,V> e2) {
            return e2.getValue().compareTo(e1.getValue());
        }
    }
);

return sortedEntries;
}
	
	public static void spliceMap(String key, List<Entry<String, Long>> map) {
		int numOfEntryToSplice = 0;
		
		for(Entry<String,Long> entry: map){
			if(!entry.getKey().equals(key)) {
				numOfEntryToSplice++;
			} else
				break;
		
		}
		
		for(int i=0 ; i< numOfEntryToSplice; i++){
			map.remove(0);
		}
	}
	
}
