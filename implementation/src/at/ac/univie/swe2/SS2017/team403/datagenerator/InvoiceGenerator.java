package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.model.Iterator;
/**
 * 
 *the class is used to generate and store some invoices and handle with them.
 *
 */
public class InvoiceGenerator implements InvoiceStorage {
	TestDataStorageFactory factory;
	List<Invoice> invoiceStorage;
	String invoiceId;
	String remoteId;
	
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
	public Iterator<Invoice> getInvoicesByRemoteId(String remoteId) throws IllegalArgumentException {
		return new AllInvoicesByRemoteIdIterator(remoteId);
	}

	
	private class AllInvoicesByRemoteIdIterator implements Iterator<Invoice> {
		private int index = 0;
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();
		
		AllInvoicesByRemoteIdIterator(String id){
			for (Invoice i:invoiceStorage){
				if(i.getId().equals(id)){
					invoices.add(i);
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return (index < invoices.size());
		}
		
		@Override
		public int count() {
			return invoices.size();
		}
		
		@Override
		public Invoice next() {
			if(this.hasNext()) {
				return invoices.get(index++);
			} else {
				return null;
			}
		}
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
