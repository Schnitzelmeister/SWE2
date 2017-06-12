package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;

public class InvoiceGenerator implements InvoiceStorage {
	TestDataStorageFactory factory;
	List<Invoice> invoiceStorage;
	
	public InvoiceGenerator(TestDataStorageFactory factory){
		this.factory = factory;
		invoiceStorage = new ArrayList<Invoice>();
		invoiceStorage.add( new Invoice(factory,"1") );
		invoiceStorage.add( new Invoice(factory,"2") );
		invoiceStorage.add( new Invoice(factory,"3") );
		invoiceStorage.add( new Invoice(factory,"4") );
	}
	
	@Override
	public Invoice[] getInvoices() {
		return invoiceStorage.toArray( new Invoice[invoiceStorage.size()] );
	}

	@Override
	public Invoice[] getInvoicesByRemoteId(String remoteId) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Invoice[] getLatestInvoiceByRemoteId(String remoteId) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Invoice[] getSubscriptionExpiredInvoices() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
