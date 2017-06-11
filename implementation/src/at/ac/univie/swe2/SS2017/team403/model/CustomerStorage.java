package at.ac.univie.swe2.SS2017.team403.model;

/**
 * This interface is used to handle all operations connected to Customer.
 */
public interface CustomerStorage {
	public Customer[] getCustomers();
	public Customer getCustomerByLocalId(String localId) throws IllegalArgumentException;
	public Customer getCustomerByRemoteId(String remoteId) throws IllegalArgumentException;
	public void addCustomer(Customer customer) throws IllegalArgumentException;
	public Iterator<Customer> getCustomersIterator(boolean onlyWithDebt);
}
