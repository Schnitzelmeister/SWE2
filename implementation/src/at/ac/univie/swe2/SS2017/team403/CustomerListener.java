package at.ac.univie.swe2.SS2017.team403;

import at.ac.univie.swe2.SS2017.team403.model.Customer;

/**
 * 
 * Observer Pattern
 */
public interface CustomerListener {
	void afterCustomerAdded(Customer customer);
	void afterCustomerChanged(Customer customer);
}
