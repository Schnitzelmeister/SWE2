package at.ac.univie.swe2.SS2017.team403.fastbill;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.datagenerator.CustomerGenarator;
import at.ac.univie.swe2.SS2017.team403.datagenerator.InvoiceGenerator;
import at.ac.univie.swe2.SS2017.team403.datagenerator.PlanGenerator;
import at.ac.univie.swe2.SS2017.team403.datagenerator.ProductGenerator;
import at.ac.univie.swe2.SS2017.team403.datagenerator.SubscriptionGenerator;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

public class FastBillDataStorageFactory implements AbstractDataStorageFactory {

	private FastBillCustomerStorage customerStorage = new FastBillCustomerStorage(this);
	private FastBillInvoiceStorage invoiceStorage = new FastBillInvoiceStorage(this); 
	
	//use local versions
	private PlanStorage planGenerator = new at.ac.univie.swe2.SS2017.team403.datagenerator.PlanGenerator(this);
	private ProductStorage productGenerator = new at.ac.univie.swe2.SS2017.team403.datagenerator.ProductGenerator(this);
	
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
		return planGenerator;
	}

	@Override
	public ProductStorage createProductStorage() {
		return productGenerator;
	}

	@Override
	public SubscriptionStorage createSubscriptionStorage() {
		// TODO Auto-generated method stub
		return null;
	}

}
