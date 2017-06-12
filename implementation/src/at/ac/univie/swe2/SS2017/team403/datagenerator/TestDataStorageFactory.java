package at.ac.univie.swe2.SS2017.team403.datagenerator;

import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

public class TestDataStorageFactory implements AbstractDataStorageFactory {

	private CustomerGenarator customerGenarator = new CustomerGenarator(this);
	private InvoiceGenerator invoiceGenerator = new InvoiceGenerator(this);
	private PlanGenerator planGenerator = new PlanGenerator(this);
	private ProductGenerator productGenerator = new ProductGenerator(this);
	private SubscriptionGenerator subscriptionGenerator = new SubscriptionGenerator(this);
	
	@Override
	public CustomerStorage CreateCustomerStorage() {
		return customerGenarator;
	}

	@Override
	public InvoiceStorage CreateInvoiceStorage() {
		return invoiceGenerator;
	}

	@Override
	public PlanStorage CreatePlanStorage() {
		return planGenerator;
	}

	@Override
	public ProductStorage CreateProductStorage() {
		return productGenerator;
	}

	@Override
	public SubscriptionStorage CreateSubscriptionStorage() {
		return subscriptionGenerator;
	}

}
