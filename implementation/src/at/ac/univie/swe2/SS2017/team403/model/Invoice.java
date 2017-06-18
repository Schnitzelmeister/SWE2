package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
/**
 * 
 * this class contains information about an invoice
 * an invoice can be storage via factory
 * an invoice has an invoice id  and local id
 *
 */
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
	
	/**
	 * method for setting  the way of the storing an invoice
	 * @param factory the factory which stores the invoice
	 */
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	/**
	 * to set the local id of an invoice
	 * @param localId local id of the invoice
	 */
	public void setId(String localId) {
		this.localId = localId;
	}
	/**
	 * to get the local id of an invoice
	 * @return local id of an invoice
	 */
	public String getId() {
		return localId;
	}
	
	/**
	 * the method returns true if the invoice has not been paid 
	 * @return true if the invoice has not been paid, otherwise returns false
	 */
	public boolean isUnpaid() {
		return true;
	}

	/**
	 * 
	 * @return the invoice id of the invoice
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * to set the invoice id
	 * @param invoiceId the invoice id if the invoice.
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
}
