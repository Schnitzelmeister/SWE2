package at.ac.univie.swe2.SS2017.team403.fastbill;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.simple.parser.ParseException;

import at.ac.univie.swe2.SS2017.team403.BackOfficeSystem;
import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;

public class FastBillInvoiceStorage implements InvoiceStorage {

	private FastBillDataStorageFactory factory;

	public FastBillInvoiceStorage(FastBillDataStorageFactory factory) {
		this.factory = factory;
	}

	private enum QueryKind {
		GetAllInvoices, GetInvoicesByRemoteId, GetLatestInvoiceByRemoteId, GetSubscriptionExpiredInvoices, AddInvoice
	}
	
	private Invoice[] sentQuery(QueryKind queryKind, Object param) throws IllegalArgumentException {
		try{
			URL url = new URL(BackOfficeSystem.FastBillWSURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			
			String encoded = Base64.getEncoder().encodeToString(
					(BackOfficeSystem.getSystem().getAPIEmail() + ":" + BackOfficeSystem.getSystem().getAPIKey())
							.getBytes(StandardCharsets.UTF_8));
			connection.setRequestProperty("Authorization", "Basic " + encoded);

			OutputStream os = connection.getOutputStream();
			StringBuilder query = new StringBuilder("{");
			
			
			switch(queryKind) {
			case GetAllInvoices:
				query.append("\"SERVICE\":\"invoice.get\",");
				query.append("\"FILTER\":{}");
				break;
			case GetInvoicesByRemoteId:
				query.append("\"SERVICE\":\"customer.get\",");
				query.append("\"FILTER\":{\"INVOICE_ID\":\"" + param.toString() + "\"}");
				break;
			case GetLatestInvoiceByRemoteId:
				//wozu brauchen wir das?
			case AddInvoice:	
				Invoice newInvoice = (Invoice) param;
				query.append("\"SERVICE\":\"invoice.create\",");
				query.append("\"DATA\":{");
				query.append("\"CUSTOMER_ID\":\"" + newInvoice.getId() + "\",");
				query.append("\"ITEMS\":\"1\"");
				query.append("}");
			default:
				throw new IllegalArgumentException(
						"at.ac.univie.swe2.SS2017.team403.fastbill.FastBillInvoiceStorage queryKind=" + queryKind
								+ " not implemented");	
			}
			
			query.append("}");
			
			os.write(query.toString().getBytes("UTF-8"));
			os.close();
			
			if (connection.getResponseCode() != 200)
				throw new IllegalArgumentException(
						"Failed to call Web Service : HTTP error code : " + connection.getResponseCode());
			//TODO den Rest implementiere ich heute noch ...
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("MalformedURLException: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("IOException: " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public Invoice[] getInvoices() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void addInvoice(Invoice invoice) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}
