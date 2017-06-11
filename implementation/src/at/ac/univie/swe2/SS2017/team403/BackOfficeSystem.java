package at.ac.univie.swe2.SS2017.team403;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.ac.univie.swe2.SS2017.team403.model.Billing;
import at.ac.univie.swe2.SS2017.team403.model.Customer;

public class BackOfficeSystem implements Billing {
	private String apiKey;
	private String apiEmail;
	private boolean productive;
	
	private DataStorageProxy dataStorage;
	
	public String getAPIKey() {
		return apiKey;
	}
	
	public String getAPIEmail() {
		return apiEmail;
	}
	
	public boolean isProductive() {
		return productive;
	}

	public boolean isTest() {
		return !productive;
	}
	
	public BackOfficeSystem(String xmlFileName) {
		//read xml
		try {
			File fXmlFile = new File(xmlFileName);
			System.out.println("configXMLFileName: " + fXmlFile.getAbsolutePath());

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			org.junit.Assert.assertEquals("settings", doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getDocumentElement().getChildNodes();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				switch (nNode.getNodeName()) {
					case "APIKey": apiKey = nNode.getTextContent(); break;
					case "APIEmail": apiEmail = nNode.getTextContent(); break;
					case "Mode": productive = (nNode.getTextContent().equals("productive")); break;
				}
			}
			
			dataStorage = new DataStorageProxy(productive);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Customer[] getCustomers() {
		return dataStorage.getCustomerStorage().getCustomers();
	}
	
	public Customer getCustomerByLocalId(String localId) throws IllegalArgumentException {
		return dataStorage.getCustomerStorage().getCustomerByLocalId(localId);
	}

	public Customer getCustomerByRemoteId(String remoteId) throws IllegalArgumentException {
		return dataStorage.getCustomerStorage().getCustomerByRemoteId(remoteId);
	}
	
	public void addCustomer(Customer customer) throws IllegalArgumentException {
		dataStorage.getCustomerStorage().addCustomer(customer);
	}

	@Override
	public void billing() {
		for (Customer customer : this.getCustomers())
			customer.billing();
	}

	public static void main(String args[]) {
		BackOfficeSystem system = new BackOfficeSystem("config.xml");
		
		for (Customer customer : system.getCustomers())
			System.out.println(customer.getLastName());
	}

}
