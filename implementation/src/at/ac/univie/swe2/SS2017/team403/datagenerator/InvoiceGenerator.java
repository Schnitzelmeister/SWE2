package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;

public class InvoiceGenerator implements InvoiceStorage {
	TestDataStorageFactory factory;
	List<Invoice> invoiceStorage;
	String invoiceId;
	String localId;
	
	public InvoiceGenerator(TestDataStorageFactory factory){
		this.factory = factory;
		invoiceStorage = new ArrayList<Invoice>();
		invoiceStorage.add( new Invoice(factory,"1","123") );
		invoiceStorage.add( new Invoice(factory,"2","234") );
		invoiceStorage.add( new Invoice(factory,"3","345") );
		invoiceStorage.add( new Invoice(factory,"4","456") );
	}
	
	@Override
	public Invoice[] getInvoices() {
		return invoiceStorage.toArray( new Invoice[invoiceStorage.size()] );
	}

	@Override
	public Invoice[] getInvoicesByRemoteId(String remoteId) throws IllegalArgumentException {
		ArrayList invoiceList = new ArrayList<Invoice>();
		for(Invoice i : invoiceStorage) {
			if(i.getInvoiceId().equals(localId))
				invoiceList.add(i);
		}
		if(invoiceList.size()==0) throw new IllegalArgumentException("Es konnte keine Invoice mit der RemoteID: "+remoteId+" gefunden werden");
		return  (Invoice[]) invoiceList.toArray(new Invoice[invoiceList.size()]);
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

	@Override
	public void addInvoice(Invoice invoice) throws IllegalArgumentException {
		for (Invoice i :invoiceStorage)
			if (i.getInvoiceId().equals(invoice.getInvoiceId())){
				throw new IllegalArgumentException("Invoice "+ i.getInvoiceId()+" already exists!");				
			}
		invoice.setFactory(factory);
		invoiceStorage.add(invoice);		
	}
	

}
