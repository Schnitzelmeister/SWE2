package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;

public class InvoiceGenerator implements InvoiceStorage {
	TestDataStorageFactory factory;
	List<Invoice> invoiceStorage;
	
	public InvoiceGenerator(TestDataStorageFactory factory){
		this.factory = factory;
		// TODO Auto-generated method stub
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
	public Invoice[] getPaymentPendingInvoices() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
