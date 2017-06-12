package at.ac.univie.swe2.SS2017.team403.model;

public interface InvoiceStorage {
	public Invoice[] getInvoices();
	public Invoice[] getInvoicesByRemoteId(String remoteId) throws IllegalArgumentException;
	public Invoice[] getLatestInvoiceByRemoteId(String remoteId) throws IllegalArgumentException;
}
