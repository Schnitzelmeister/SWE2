package at.ac.univie.swe2.SS2017.team403.fastbill;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.datagenerator.CustomerGenarator;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

public class FastBillDataStorageFactory implements AbstractDataStorageFactory {

	private FastBillCustomerStorage customerStorage = new FastBillCustomerStorage(this);

	@Override
	public CustomerStorage CreateCustomerStorage() {
		return customerStorage;
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
