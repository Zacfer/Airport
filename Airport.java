
/**
 * XML Parser for Airport Lighting Tool
 * 
 * @author 2011SEGgp04 - Michael
 * Creation: 21/02/11
 *  
 *  Modified: 03/03/11 - Zacharias
 *     Changed name of class.
 *     Added XML Validation according to Schema.
 *     
 *  Modified: 04/03/11 - Zacharias
 *     Added getRunway, getJoin  
 *     
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Airport {
	
	public static void main(String[] args) {
		new Airport(new File("src\\complex.apx"));
	}

	boolean airportLoaded = true;
	ArrayList<Runway> runways = new ArrayList<Runway>();
	ArrayList<TaxiwayJoin> taxiwayJoins = new ArrayList<TaxiwayJoin>();
	ArrayList<TaxiwayJoin> taxiwayRunwayJoins = new ArrayList<TaxiwayJoin>();
	ArrayList<Taxiway> taxiways = new ArrayList<Taxiway>();
	String airportName = "";
	String airportCreated = "";
	
	public Runway getRunway(int id) {
		Runway _runway = null;
		for (Runway runway : runways) {
			if(runway.getUID() == id) {
				_runway = runway;
			}
		}
		return _runway;
	}
	
	public TaxiwayJoin getJoin(int id) {
		TaxiwayJoin join = null;
		for (TaxiwayJoin taxiwayJoin : taxiwayJoins) {
			if(taxiwayJoin.getUID() == id) {
				join = taxiwayJoin;
			}
		}
		for (TaxiwayJoin taxiwayRunwayJoin : taxiwayRunwayJoins) {
			if(taxiwayRunwayJoin.getUID() == id) {
				join = taxiwayRunwayJoin;
			}
		}
		return join;
	}

	public boolean validate(String path) {
		boolean valid = true;
		try {
			// define the type of schema - we use W3C:
			String schemaLang = "http://www.w3.org/2001/XMLSchema";

			// get validation driver:
			SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

			// create schema by reading it from an XSD file:
			Schema schema = factory.newSchema(new StreamSource("src\\SaveFormat.xsd"));
			Validator validator = schema.newValidator();

			// at last perform validation:
			validator.validate(new StreamSource(path));
		} catch (SAXException e) {
			valid = false;
		} catch (IOException e) {
			valid = false;
		}
		
		return valid;
	}

	/**Constructs the various arraylists from the XML document
	 * 
	 * @param file - the xml file to be read - if reading fails then the global variable airportLoaded is set to false
	 */
	public Airport(File file) {
		//validate("src\\complex.apx");
		//System.out.println(validate(file.toString()));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file);
			doc.getDocumentElement().normalize();
		} catch (SAXException e) {
			airportLoaded = false;
		} catch (IOException e) {
			airportLoaded = false;
		} catch (ParserConfigurationException e) {
			airportLoaded = false;
		}
		if(airportLoaded) {
			//Airport Info:
			doc.getDocumentElement();
			//Get initial info, name, date created etc
			NodeList airportNameNodeList = doc.getElementsByTagName("airportName");
			Element element = (Element) airportNameNodeList.item(0);
			NodeList node = element.getChildNodes();
			this.airportName = ((Node)node.item(0)).getNodeValue();
			NodeList airportCreatedList = doc.getElementsByTagName("airportCreated");
			element = (Element) airportCreatedList.item(0);
			node = element.getChildNodes();
			this.airportCreated = ((Node)node.item(0)).getNodeValue();
			NodeList listOfRunways = doc.getElementsByTagName("runway");
			//Loop through runways
			for(int s=0; s<listOfRunways.getLength() ; s++){
				Node runway = listOfRunways.item(s);
				int UID = Integer.parseInt(runway.getAttributes().getNamedItem("UID").getNodeValue());
				if (runway.getNodeType() == Node.ELEMENT_NODE) {
					//Treat runway node as an element
					Element elements = (Element) runway;
					//Return centerX details: Firstly get all elements of center x (only one)
					NodeList nodes = elements.getElementsByTagName("centerX");
					//Treat as an element
					element = (Element) nodes.item(0);
					//Get child info (value)
					node = element.getChildNodes();
					//Return Value formatted
					double centerX = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					//Above code repeated for the rest of the info:
					//Center Y
					nodes = elements.getElementsByTagName("centerY");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double centerY = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					//Orientation:
					nodes = elements.getElementsByTagName("orientation");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double orientation = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					//Length
					nodes = elements.getElementsByTagName("length");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double length = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					//Width
					nodes = elements.getElementsByTagName("width");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double width = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					Runway currentRunway = new Runway(UID, centerX, centerY, orientation, length, width);
					//Bottom Extension
					nodes = elements.getElementsByTagName("bottomExtension");
					if(nodes.getLength()>0) {
						Node bottomExtensionNode = nodes.item(0);
						int extensionUID = Integer.parseInt(bottomExtensionNode.getAttributes().getNamedItem("UID").getNodeValue());
						if (bottomExtensionNode.getNodeType() == Node.ELEMENT_NODE) {
							Element extElements = (Element) bottomExtensionNode;
							NodeList extNodes = extElements.getElementsByTagName("length");
							Element extElement = (Element) extNodes.item(0);
							NodeList extNode = extElement.getChildNodes();
							double extLength = Double.parseDouble(((Node)extNode.item(0)).getNodeValue());
							extNodes = extElements.getElementsByTagName("width");
							extElement = (Element) extNodes.item(0);
							double extWidth = 0;
							if(extElement!=null) {
								extNode = extElement.getChildNodes();
								extWidth = Double.parseDouble(((Node)extNode.item(0)).getNodeValue());
							}
							extNodes = extElements.getElementsByTagName("type");
							extElement = (Element) extNodes.item(0);
							extNode = extElement.getChildNodes();
							String extType = ((Node)extNode.item(0)).getNodeValue();
							currentRunway.setBottomExtension(new Extension(extLength, extType, extWidth, extensionUID, "BOTTOM"));
						}
					}
					//Top Extension
					NodeList topNodes = elements.getElementsByTagName("topExtension");
					if(topNodes.getLength()>0) {
						Node topExtensionNode = topNodes.item(0);
						int extensionUID = Integer.parseInt(topExtensionNode.getAttributes().getNamedItem("UID").getNodeValue());
						if (topExtensionNode.getNodeType() == Node.ELEMENT_NODE) {
							Element extElements = (Element) topExtensionNode;
							NodeList extNodes = extElements.getElementsByTagName("length");
							Element extElement = (Element) extNodes.item(0);
							NodeList extNode = extElement.getChildNodes();
							double extLength = Double.parseDouble(((Node)extNode.item(0)).getNodeValue());
							extNodes = extElements.getElementsByTagName("width");
							extElement = (Element) extNodes.item(0);
							double extWidth = 0;
							if(extElement!=null) {
								extNode = extElement.getChildNodes();
								extWidth = Double.parseDouble(((Node)extNode.item(0)).getNodeValue());
							}
							extNodes = extElements.getElementsByTagName("type");
							extElement = (Element) extNodes.item(0);
							extNode = extElement.getChildNodes();
							String extType = ((Node)extNode.item(0)).getNodeValue();
							currentRunway.setTopExtension(new Extension(extLength, extType, extWidth, extensionUID, "TOP"));
						}
					}

					runways.add(currentRunway);
				}
			}
			//Loop through where the taxiways join
			NodeList listOfTaxiwayJoins = doc.getElementsByTagName("taxiwayJoin");
			for(int s=0; s<listOfTaxiwayJoins.getLength() ; s++){
				Node taxiwayJoin = listOfTaxiwayJoins.item(s);
				int UID = Integer.parseInt(taxiwayJoin.getAttributes().getNamedItem("UID").getNodeValue());
				if (taxiwayJoin.getNodeType() == Node.ELEMENT_NODE) {
					Element elements = (Element) taxiwayJoin;
					NodeList nodes = elements.getElementsByTagName("joinX");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double joinX = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					nodes = elements.getElementsByTagName("joinY");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double joinY = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					taxiwayJoins.add(new TaxiwayJoin(UID, joinX, joinY));
				}
			}
			//Loop through where the taxiways join runways
			NodeList listOfTaxiwayRunwayJoins = doc.getElementsByTagName("taxiwayRunwayJoin");
			for(int s=0; s<listOfTaxiwayRunwayJoins.getLength() ; s++){
				Node taxiwayRunwayJoin = listOfTaxiwayRunwayJoins.item(s);
				int UID = Integer.parseInt(taxiwayRunwayJoin.getAttributes().getNamedItem("UID").getNodeValue());
				if (taxiwayRunwayJoin.getNodeType() == Node.ELEMENT_NODE) {
					Element elements = (Element) taxiwayRunwayJoin;
					NodeList nodes = elements.getElementsByTagName("joinRX");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double joinX = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					nodes = elements.getElementsByTagName("joinRY");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double joinY = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					nodes = elements.getElementsByTagName("joinRunway");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					int runwayID = Integer.parseInt(((Node)node.item(0)).getNodeValue());
					TaxiwayJoin currentJoin = new TaxiwayJoin(UID, joinX, joinY);
					currentJoin.setRunwayConnected(runwayID);
					taxiwayRunwayJoins.add(currentJoin);
				}
			}
			//Loop through the taxiways
			NodeList listOfTaxiways = doc.getElementsByTagName("taxiwaySection");
			for(int s=0; s<listOfTaxiways.getLength() ; s++){
				Node taxiway = listOfTaxiways.item(s);
				int UID = Integer.parseInt(taxiway.getAttributes().getNamedItem("UID").getNodeValue());
				if (taxiway.getNodeType() == Node.ELEMENT_NODE) {
					Element elements = (Element) taxiway;
					NodeList nodes = elements.getElementsByTagName("sectionWidth");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					double sectionWidth = Double.parseDouble(((Node)node.item(0)).getNodeValue());
					nodes = elements.getElementsByTagName("sectionStartID");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					int sectionStartID = Integer.parseInt(((Node)node.item(0)).getNodeValue());
					nodes = elements.getElementsByTagName("sectionEndID");
					element = (Element) nodes.item(0);
					node = element.getChildNodes();
					int sectionEndID = Integer.parseInt(((Node)node.item(0)).getNodeValue());
					taxiways.add(new Taxiway(UID, sectionStartID, sectionEndID, sectionWidth));
				}
			}
		}
	}
}
