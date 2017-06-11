package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.Customer;

public class CustomerGenarator implements CustomerStorage {
	List<Customer> storage;
	
	public CustomerGenarator() {
		storage = new ArrayList<Customer>();
		storage.add( new Customer("1","111","Zinatulin Ayrat") );
		storage.add( new Customer("2","222","Pektas Tarik") );
		storage.add( new Customer("3","333","Ornetsmueller Raphael") );
		storage.add( new Customer("4","444","Zvonek Jakub") );
	}

	@Override
	public List<Customer> getCustomers() {
		return storage;
	}

	@Override
	public Customer getCustomerByLocalId(String localId) {
		for (Customer c : storage)
			if (c.getLocalId().equals(localId))
				return c;
		throw new IllegalArgumentException("The User with localId=" + localId + " does not exist");
	}

	@Override
	public Customer getCustomerByRemoteId(String remoteId) {
		for (Customer c : storage)
			if (c.getLocalId().equals(remoteId))
				return c;
		throw new IllegalArgumentException("The User with remoteId=" + remoteId + " does not exist");
	}
}
