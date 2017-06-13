package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;

public class Invoice {
	private AbstractDataStorageFactory factory = null;
	private String localId;
	private String invoiceId;
	
	public Invoice(AbstractDataStorageFactory factory, String localId, String invoiceId){
		this.factory = factory;
		this.localId = localId;
		this.invoiceId = invoiceId;
	}
	
	public Invoice(String localId, String invoiceId){
		this.localId = localId;
		this.invoiceId = invoiceId;
	}
	
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	public void setId(String localId) {
		this.localId = localId;
	}
	
	public String getId() {
		return localId;
	}
	
	public boolean isUnpaid() {
		return true;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
}
