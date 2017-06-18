package at.ac.univie.swe2.SS2017.team403.fastbill;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.datagenerator.CustomerGenarator;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

/**
 * 
 *The factory class is used to store data in FastBill
 *
 */
public class FastBillDataStorageFactory implements AbstractDataStorageFactory {

	private FastBillCustomerStorage customerStorage = new FastBillCustomerStorage(this);
	private FastBillInvoiceStorage invoiceStorage = new FastBillInvoiceStorage(this); 
	
	@Override
	public CustomerStorage createCustomerStorage() {
		return customerStorage;
	}

	@Override
	public InvoiceStorage createInvoiceStorage() {
		return invoiceStorage;
	}

	@Override
	public PlanStorage createPlanStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductStorage createProductStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubscriptionStorage createSubscriptionStorage() {
		// TODO Auto-generated method stub
		return null;
	}

}
