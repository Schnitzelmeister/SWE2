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
import at.ac.univie.swe2.SS2017.team403.model.CustomerReportNotifier;
import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.Payment;
import at.ac.univie.swe2.SS2017.team403.model.Plan;
import at.ac.univie.swe2.SS2017.team403.model.Product;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;

public class BackOfficeSystem implements Billing {
	
	public static final String FastBillWSURL = "https://my.fastbill.com/api/1.0/api.php";
	
	private List<CustomerListener> listeners = new ArrayList<CustomerListener>();
	
	public void addListener(CustomerListener listener) {
		listeners.add(listener);
	}

	private static BackOfficeSystem sys = new BackOfficeSystem();
	public static BackOfficeSystem getSystem() {
		return sys;
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
	
	public static void initialize(String xmlFileName) {
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
					case "APIKey": sys.apiKey = nNode.getTextContent(); 
					break;
					case "APIEmail": sys.apiEmail = nNode.getTextContent(); 
					break;
					case "Mode": sys.productive = (nNode.getTextContent().equals("productive")); 
					break;
				}
			}
			
			sys.dataStorage = new DataStorageProxy(sys.productive);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public Customer[] getCustomers() throws IllegalArgumentException {
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
	
	public void addInvoice(Invoice invoice) throws IllegalArgumentException {
		dataStorage.getInvoiceStorage().addInvoice(invoice);
	}
	
	public Invoice[] getInvoices(){
		return dataStorage.getInvoiceStorage().getInvoices();
	}
	
	public void addPlan(Plan plan) throws IllegalArgumentException {
		dataStorage.getPlanStorage().addPlan(plan);
	}
	
	public Plan[] getPlans() {
		return dataStorage.getPlanStorage().getPlans();
	}
	
	public void addProduct(Product product) throws IllegalArgumentException {
		dataStorage.getProductStorage().addProduct(product);
	}
	
	public Product[] getProducts() {
		return dataStorage.getProductStorage().getProducts();
	}
	
	public void addSubscription(Subscription subscription) throws IllegalArgumentException {
		dataStorage.getSubscriptionStorage().addSubscription(subscription);
	}
	
	public Subscription[] getSubscriptions() {
		return dataStorage.getSubscriptionStorage().getSubscriptions();
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
		BackOfficeSystem.initialize("config.xml");
		BackOfficeSystem system = BackOfficeSystem.getSystem();
		system.addCustomer(new Customer("5", "555", "Mustermann", "Muster", "muster@gmail.com", ""));
		/*
		system.addProduct(new Product("Produkt1", "productId1"));
		system.addPlan( new Plan("1111", "Deluxe", 11.11,new Product("Produkt1","productId1"),"planId1"));
		
		for(Plan plans : system.getPlans()){
			System.out.println(plans.getProduct().getName()+" offers "+plans.getName()+"-Account");
		}
		
		for(Subscription subscriptions : system.getSubscriptions()){
			System.out.println(subscriptions.getName()+" subscribed by "+system.getCustomerByLocalId(subscriptions.getLocalId()).getLastName());
		}
		
		for(Invoice invoices: system.getInvoices()){
			System.out.println(system.getCustomerByLocalId(invoices.getId()).getLastName()+"'s invoice");
		}*/
		
		for (Customer customer : system.getCustomers()) {
			System.out.println(customer);
			
			CustomerReportNotifier notifier = customer;

			if (customer.getPhone() != null && customer.getPhone().length() > 0 ) {
				notifier = new CustomerReportSMSNotifier(customer);
			}
			
			notifier.send();
		}
	}

}
