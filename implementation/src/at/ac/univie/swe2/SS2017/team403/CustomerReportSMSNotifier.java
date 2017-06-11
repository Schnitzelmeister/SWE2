package at.ac.univie.swe2.SS2017.team403;

import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.CustomerReportNotifier;

/**
 * 
 * For Decorator Pattern
 */
public class CustomerReportSMSNotifier implements CustomerReportNotifier {

	private Customer customer;
	
	public CustomerReportSMSNotifier(Customer customer) {
		this.customer = customer;
	}
	
	@Override
	public void send() {
		// send sms
		System.out.println("send SMS to " + customer.getPhone());
	}

}
