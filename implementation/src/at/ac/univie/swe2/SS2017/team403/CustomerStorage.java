package at.ac.univie.swe2.SS2017.team403;

import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Customer;

public interface CustomerStorage {
	public List<Customer> getCustomers();
	public Customer getCustomerByLocalId(String localId) throws IllegalArgumentException;
	public Customer getCustomerByRemoteId(String remoteId) throws IllegalArgumentException;
}
