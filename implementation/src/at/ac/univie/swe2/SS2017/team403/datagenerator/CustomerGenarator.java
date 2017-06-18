package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.Iterator;
import at.ac.univie.swe2.SS2017.team403.model.IteratorCreator;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;
/**
 * 
 *the class is used to generate and store some customer and handle with them.
 *
 */
public class CustomerGenarator implements CustomerStorage {
	private AbstractDataStorageFactory factory;
	private List<Customer> storage;
	
	public CustomerGenarator(AbstractDataStorageFactory factory) {
		this.factory = factory;
		storage = new ArrayList<Customer>();
		storage.add( new Customer(factory, "1","111","Zinatulin", "Ayrat", "ayrat@gmail.com", "12345678") );
		storage.add( new Customer(factory, "2","222","Pektas", "Tarik", "tarik@gmail.com", "23456789") );
		storage.add( new Customer(factory, "3","333","Ornetsmueller", "Raphael", "raphael@gmail.com", "34567890") );
		storage.add( new Customer(factory, "4","444","Zvonek", "Jakub", "jakub@gmail.com", "45678901") );
	}

	@Override
	public Customer[] getCustomers() throws IllegalArgumentException {
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
			if (c.getRemoteId().equals(remoteId)){
				return c;
			}	
		}
		throw new IllegalArgumentException("The User with remoteId=" + remoteId + " does not exist");
	}

	public Subscription[] getSubscriptionByCustomer(String localCustomerId){
		
		
		return null;
	}
	
	@Override
	public String addCustomer(Customer customer) throws IllegalArgumentException {
		for (Customer c : storage) {
			if (c.getLocalId().equals(customer.getLocalId()) || c.getRemoteId().equals(customer.getRemoteId())) {
				throw new IllegalArgumentException("The User with localId=" + customer.getLocalId() + " and remoteId=" + customer.getRemoteId() + " exist");
			}
		}
		customer.setFactory(factory);
		storage.add(customer);
		return customer.getLocalId();
	}

	/**
	 * 
	 *This class is used to access the elements of the class customer
	 *in sequential manner without any need to know its underlying representation.
	 *
	 */
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

	/**
	 * 
	 *This class is used to access the elements of the class customer who has debt
	 *in sequential manner without any need to know its underlying representation.
	 *
	 */
	private class DebtCustomerIterator implements Iterator<Customer> {
		private int index = 0;
		
		private boolean getNext() {
			while (index < storage.size()) {
				for (Subscription subscription : storage.get(index).getSubscriptions()) {
					for (Invoice invoice : subscription.getInvoices()) {
						if (invoice.isUnpaid()) {
							return true;
						}
					}
				}
				++index;
			}
			return false;
		}

		
		@Override
		public boolean hasNext() {
			return (index < storage.size());
		}

		
		@Override
		public int count() {
			return -1;
		}
		
		@Override
		public Customer next() {
			if(this.getNext()) {
				return storage.get(index);
			} else {
				return null;
			}
		}
	}

	@Override
	public Iterator<Customer> getCustomersIterator(boolean onlyWithDebt) {
		IteratorCreator<Customer> creator;
		if (onlyWithDebt) {
			creator = new DebtCustomerIteratorCreator();
		} else {
			creator = new AllCustomerIteratorCreator();
		}
		
		return creator.factoryMethod();
	}
	
	private class DebtCustomerIteratorCreator extends IteratorCreator<Customer> {
	    @Override
	    public Iterator<Customer> factoryMethod() { return new DebtCustomerIterator(); }
	}

	private class AllCustomerIteratorCreator extends IteratorCreator<Customer> {
	    @Override
	    public Iterator<Customer> factoryMethod() { return new AllCustomerIterator(); }
	}
}
