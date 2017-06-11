package at.ac.univie.swe2.SS2017.team403;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.ac.univie.swe2.SS2017.team403.model.Billing;
import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.Payment;

public class BackOfficeSystem implements Billing {
	private List<CustomerListener> listeners = new ArrayList<CustomerListener>();
	
	public void addListener(CustomerListener listener) {
		listeners.add(listener);
	}

	public void removeListener(CustomerListener listener) {
		if (listeners.size() <= 0)
			return;
		
		for (int i = listeners.size() - 1; i < 0; --i) {
			if (listeners.get(i) == listener)
				listeners.remove(i);
		}
	}
	
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
		
		for (CustomerListener l : listeners)
			l.afterCustomerAdded(customer);
	}

	@Override
	public boolean billing() {
		return this.billing(null);
	}
	
	@Override
	public boolean billing(Payment[] payments) {
		boolean changed = false;
		for (Customer customer : this.getCustomers()) {
			if (customer.billing(payments)) {
				changed = true;
			
				for (CustomerListener l : listeners)
					l.afterCustomerChanged(customer);
			}
		}

		return changed;
	}

	public static void main(String args[]) {
		BackOfficeSystem system = new BackOfficeSystem("config.xml");
		
		system.addCustomer( new Customer("5", "555", "Muster Muster", "muster@gmail.com", "56789012") );
		
		for (Customer customer : system.getCustomers()) {
			System.out.println(customer.getLastName());
			if (customer.getLastName().equals("Ornetsmueller Raphael")) {
				CustomerReportSMSNotifier decorator = new CustomerReportSMSNotifier(customer);
				decorator.send();
			}
			else {
				customer.send();
			}
		}
	}

}
