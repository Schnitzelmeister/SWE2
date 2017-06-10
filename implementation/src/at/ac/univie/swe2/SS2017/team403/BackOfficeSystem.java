package at.ac.univie.swe2.SS2017.team403;

import java.util.List;

import at.ac.univie.swe2.SS2017.team403.datagenerator.TestDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.fastbill.FastBillDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.model.Customer;

public class BackOfficeSystem {
	private String apiKey;
	private String apiEmail;
	private boolean productive;
	
	private AbstractDataStorageFactory factory;
	
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
		//init xml
		
		productive = false;
		
		if (productive) {
			factory = new FastBillDataStorageFactory();
		}
		else {
			factory = new TestDataStorageFactory();
		}
	}
	
	public List<Customer> getCustomers(/* params */) {
		return factory.CreateCustomerStorage().getCustomers(/* params */);
	}
	
	public static void main(String args[]) {
		BackOfficeSystem system = new BackOfficeSystem("config.xml");
		
		for (Customer customer : system.getCustomers())
			System.out.println(customer.getLastName());
	}
}
