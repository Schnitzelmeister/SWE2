package at.ac.univie.swe2.SS2017.team403.datagenerator;

import at.ac.univie.swe2.SS2017.team403.AbstractDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.ProductStorage;
import at.ac.univie.swe2.SS2017.team403.SubscriptionStorage;

public class TestDataStorageFactory implements AbstractDataStorageFactory {

	private CustomerGenarator customerGenarator = new CustomerGenarator();
	
	@Override
	public CustomerStorage CreateCustomerStorage() {
		return customerGenarator;
	}

	@Override
	public InvoiceStorage CreateInvoiceStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlanStorage CreatePlanStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductStorage CreateProductStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubscriptionStorage CreateSubscriptionStorage() {
		// TODO Auto-generated method stub
		return null;
	}

}
