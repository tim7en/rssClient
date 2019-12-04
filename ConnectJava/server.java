import java.io.*;
import java.net.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class server 
{
	private static BufferedReader in;
	private static BufferedWriter out;
	
	public static void main (String arg [])
		{
		 try {

				ServerSocket server=new ServerSocket(3000);
				Socket s=server.accept();
			try {
				in = new BufferedReader(new InputStreamReader(s.getInputStream())); //asks for url
				out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream())); //outputs return dictionary
				String word = in.readLine(); //reads an input url
				List<Map<String,String>> data = readRSS(word);
				for (int j = 0; j<data.size(); j++){
					out.write ("---");
					out.write (data.get(j).get("Title"));
					out.write (data.get(j).get("Description"));
					out.write (data.get(j).get("Link"));
					out.write (data.get(j).get("PubDate"));
				}
				out.flush(); //flush output
			} finally {
				s.close(); //close all connections
				in.close();
				out.close();
				server.close();
			}
		 } catch (Exception e) {}
		}
	
	
	public static List<Map<String,String>> readRSS (String urlAddress) 
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(urlAddress).openStream());
			Element element = doc.getDocumentElement();                         // get the first element
			NodeList nodes = element.getChildNodes();                           // get all child nodes
			NodeList nList = element.getElementsByTagName("item");              //Get rss items only
			String strRegEx = "<[^>]*>";                                        //remove html tags
			List<Map<String,String>> dictionaryList = new ArrayList<Map<String,String>>();
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					HashMap<String, String> hmap = new HashMap<String, String>();
					Element eElement = (Element) node;
					hmap.put ("Title", eElement.getElementsByTagName("title").item(0).getTextContent()); //str.replaceAll("&.*?;", "");
					hmap.put ("Description", eElement.getElementsByTagName("description").item(0).getTextContent().replaceAll(strRegEx, ""));
					hmap.put ("Link", eElement.getElementsByTagName("link").item(0).getTextContent());
					hmap.put ("PubDate", eElement.getElementsByTagName("pubDate").item(0).getTextContent());
					dictionaryList.add(hmap);
				}
			}
			return dictionaryList;
		} catch (MalformedURLException ue) {
			System.out.println ("Malformed URL");
		} catch (IOException ioe){
			System.out.println ("Something went wrong reading the contents");
		} catch(SAXException sae){
			System.out.println ("SAXE error");
		} catch(ParserConfigurationException par){
			System.out.println ("Parsing error");
		}
		return null;
	}
}
