package at.ac.univie.swe2.SS2017.team403.model;

/**
 * This interface is used to handle all operations connected to Invioces.
 */
public interface InvoiceStorage {
	public Invoice[] getInvoices();
	public Invoice[] getInvoicesByRemoteId(String remoteId) throws IllegalArgumentException;
	public Invoice[] getLatestInvoiceByRemoteId(String remoteId) throws IllegalArgumentException;
	public Invoice[] getSubscriptionExpiredInvoices() throws IllegalArgumentException; 
}
