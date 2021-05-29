package jsonFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class JsonFileData {

	public static void main(String[] args) throws Exception {
		
		JSONArray recordsObject = getDataFromJson();
		
		xmlDocCreater(recordsObject);

	}
	
	private static void xmlDocCreater(JSONArray recordsObject) {
		
		JSONObject obj1 = null;
		Iterator itr1 = recordsObject.iterator();
		
		
		  try {
		         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		         Document doc = dBuilder.newDocument();
		         
		         // root element
		         Element rootElement = doc.createElement("books");
		         doc.appendChild(rootElement);

		         
		         while (itr1.hasNext()) {
		 			obj1 = (JSONObject) itr1.next();
		 			JSONArray authorsList = (JSONArray) obj1.get("authors");

		 			
		         // book element
		         Element book = doc.createElement("book");
		         rootElement.appendChild(book);
		         
		         // id element
		         Element id = doc.createElement("id");
		         id.appendChild(doc.createTextNode((String) obj1.get("id")));
		         book.appendChild(id);
		         
		      // title element
		         Element title = doc.createElement("title");
		         title.appendChild(doc.createTextNode((String) obj1.get("title")));
		         book.appendChild(title);
		         
		         
		      // price element
		         Element price = doc.createElement("price");
		         price.appendChild(doc.createTextNode(String.valueOf(obj1.get("price"))));
		         book.appendChild(price);
		         
		      // authors element
		         Element authors = doc.createElement("authors");
		         book.appendChild(authors);
		         
		         Iterator itrAu = authorsList.iterator();

					while (itrAu.hasNext()) {
						JSONObject obj2 = (JSONObject) itrAu.next();
			
		         
		         //author id element
		         Element authorId = doc.createElement("id");
		         authorId.appendChild(doc.createTextNode((String) obj2.get("id")));
		         authors.appendChild(authorId);
		         
		         //author id element
		         Element authorName = doc.createElement("name");
		         authorName.appendChild(doc.createTextNode((String) obj2.get("name")));
		         authors.appendChild(authorName);
		         
					}
		         }
		         // write the content into xml file
		         TransformerFactory transformerFactory = TransformerFactory.newInstance();
		         Transformer transformer = transformerFactory.newTransformer();
		         DOMSource source = new DOMSource(doc);
		         StreamResult result = new StreamResult(new File("books.xml"));
		         transformer.transform(source, result);
		         
		         // Output to console for testing
		         StreamResult consoleResult = new StreamResult(System.out);
		         transformer.transform(source, consoleResult);
		        
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		}
	

	private static JSONArray getDataFromJson() throws ParseException, IOException {
		
		JSONObject obj1 = null;
	
		// parsing file "JSONExample.json"
		Object obj = new JSONParser().parse(new FileReader("books.json"));
		DatabaseService service = new DatabaseService();
		
		//parsing json from url
//		String url="https://restcountries.eu/rest/v2/region/asia";
//		String json = getJSON(url);
//		Object urlObj = new JSONParser().parse(json);
		
		// typecasting obj to JSONObject
		JSONArray jo = (JSONArray) obj;//urlObj;
		Iterator itr1 = jo.iterator();

		while(itr1.hasNext()) {
			String authorsArray = "[";
			obj1 = (JSONObject) itr1.next();

			//get the the name of keys
			Set<String> keys =  obj1.keySet();
			SortedSet<String> sorted = new TreeSet<String>(keys);
			Iterator<String> k =keys.iterator();
			while(k.hasNext()) {
				System.out.print(k.next() + " ");
			}
			
			JSONArray authors = (JSONArray) obj1.get("authors");
		
			System.out.println();
			System.out.println(obj1.get("title"));
			System.out.println(obj1.get("price"));
			
			//[{"id":"1","name":"Maanavi"},{"id":"2","name":"Aman"},{"id":"3","name":"Tejal"}]
			
			  Iterator itrAu = authors.iterator();
			  
			  while(itrAu.hasNext()) { JSONObject obj2 = (JSONObject) itrAu.next();
			  authorsArray += "{\"id\":\""; authorsArray += obj2.get("id") + "\",";
			  authorsArray += "\"name\":\"" + obj2.get("name") + "\"}";
			  System.out.println("\t"+obj2.get("name")); if(itrAu.hasNext()) { authorsArray
			  += ",";} } authorsArray +="\"}]";
			 	
			//service.insertData(obj1.get("id"), obj1.get("title"), obj1.get("price"), authorsArray);
			
			System.out.println(authorsArray);
		}
	
		return jo;
	}

	public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
	
}