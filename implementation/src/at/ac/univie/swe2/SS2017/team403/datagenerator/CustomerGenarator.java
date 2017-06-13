package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.Iterator;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;

public class CustomerGenarator implements CustomerStorage {
	TestDataStorageFactory factory;
	List<Customer> storage;
	
	public CustomerGenarator(TestDataStorageFactory factory) {
		this.factory = factory;
		storage = new ArrayList<Customer>();
		storage.add( new Customer(factory, "1","111","Zinatulin Ayrat", "ayrat@gmail.com", "12345678") );
		storage.add( new Customer(factory, "2","222","Pektas Tarik", "tarik@gmail.com", "23456789") );
		storage.add( new Customer(factory, "3","333","Ornetsmueller Raphael", "raphael@gmail.com", "34567890") );
		storage.add( new Customer(factory, "4","444","Zvonek Jakub", "jakub@gmail.com", "45678901") );
	}

	@Override
	public Customer[] getCustomers() {
		return storage.toArray( new Customer[storage.size()] );
	}

	@Override
	public Customer getCustomerByLocalId(String localId) {
		for (Customer c : storage) {
			if (c.getLocalId().equals(localId)) {
				return c;
			}
		}
		throw new IllegalArgumentException("The User with localId=" + localId + " does not exist");
	}

	@Override
	public Customer getCustomerByRemoteId(String remoteId) {
		for (Customer c : storage){
			if (c.getLocalId().equals(remoteId)){
				return c;
			}	
		}
		throw new IllegalArgumentException("The User with remoteId=" + remoteId + " does not exist");
	}

	@Override
	public void addCustomer(Customer customer) throws IllegalArgumentException {
		for (Customer c : storage) {
			if (c.getLocalId().equals(customer.getLocalId()) || c.getRemoteId().equals(customer.getRemoteId())) {
				throw new IllegalArgumentException("The User with localId=" + customer.getLocalId() + " and remoteId=" + customer.getRemoteId() + " exist");
			}
		}
		customer.setFactory(factory);
		storage.add(customer);
	}

	//inner class, which are used for Iterator Pattern
	private class AllCustomerIterator implements Iterator<Customer> {
		private int index = 0;
		
		@Override
		public boolean hasNext() {
			return (index < storage.size());
		}
		
		@Override
		public int count() {
			return storage.size();
		}
		
		@Override
		public Customer next() {
			if(this.hasNext()) {
				return storage.get(index++);
			} else {
				return null;
			}
		}
	}

	//inner class, which are used for Iterator Pattern
	private class DebtCustomerIterator implements Iterator<Customer> {
		private List<Customer> debtStorage;
		private int index = 0;
		
		DebtCustomerIterator() {
			debtStorage = new ArrayList<Customer>();
			for (Customer customer : storage) {
				boolean hasDebt = false;
			
				search:
				for (Subscription subscription : customer.getSubscriptions()) {
					for (Invoice invoice : subscription.getInvoices()) {
						if (invoice.isUnpaid()) {
							hasDebt = true;
							break search;
						}
					}
				}
				
				if (hasDebt){
					debtStorage.add(customer);
				}
			}
		}
		
		
		@Override
		public boolean hasNext() {
			return (index < debtStorage.size());
		}
		
		@Override
		public int count() {
			return debtStorage.size();
		}
		
		@Override
		public Customer next() {
			if(this.hasNext()){
				return debtStorage.get(index++);
			} else {
				return null;
			}
		}
	}

	@Override
	public Iterator<Customer> getCustomersIterator(boolean onlyWithDebt) {
		if (onlyWithDebt) {
			return new DebtCustomerIterator();
		} else {
			return new AllCustomerIterator();
		}
	}
}
