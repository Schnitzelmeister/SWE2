package at.ac.univie.swe2.SS2017.team403.fastbill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.ac.univie.swe2.SS2017.team403.BackOfficeSystem;
import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;

/**
 * 
 * the class is used to handle the connection with FastBill to get or store invoice(s)
 *  
 *
 */
public class FastBillInvoiceStorage implements InvoiceStorage {

	private FastBillDataStorageFactory factory;

	public FastBillInvoiceStorage(FastBillDataStorageFactory factory) {
		this.factory = factory;
	}

	private enum QueryKind {
		GetAllInvoices, GetInvoicesByRemoteId, GetLatestInvoiceByRemoteId, GetSubscriptionExpiredInvoices, AddInvoice
	}
	
	/**
	 * The method connects to the FastBill and sends a HTTP-Request to get/creat Information about invoices
	 * 
	 * @param queryKind the kind of the query
	 * @param param the parameter which specified the invoice 
	 * @return an array of the invoices
	 * @throws IllegalArgumentException if the kind of query and the parameter not used
	 */
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
			
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			// get Data from Server
			String output;
			StringBuilder outputB = new StringBuilder();
			while ((output = br.readLine()) != null)
				outputB.append(output);
			connection.disconnect();	
			
			
			List<Invoice> ret = new ArrayList<Invoice>();
	
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(outputB.toString());
			
			if (((JSONObject) json.get("RESPONSE")).get("ERRORS") != null)
				throw new IllegalArgumentException(
						"FastBill error: " + ((JSONObject) json.get("RESPONSE")).get("ERRORS").toString());

			if (queryKind == QueryKind.GetAllInvoices || queryKind == QueryKind.GetInvoicesByRemoteId
					|| queryKind == QueryKind.GetLatestInvoiceByRemoteId) {
				JSONArray invoices = (JSONArray) ((JSONObject) json.get("RESPONSE")).get("INVOICES");
				for (Object invoice : invoices) {
					String remoteId = (String) ((JSONObject) invoice).get("INVOICE_ID");
					String localId = (String) ((JSONObject) invoice).get("INVOICE_NUMBER");

					ret.add(new Invoice(factory, localId, remoteId));
				}
			} else if (queryKind == QueryKind.AddInvoice) {
				String invoiceId = ((JSONObject) json.get("RESPONSE")).get("INVOICE_ID").toString();
				Invoice newInvoice = (Invoice) param;
				
				ret.add( new Invoice(factory, newInvoice.getId(), newInvoice.getInvoiceId()) );	
			}
			return ret.toArray(new Invoice[ret.size()]);
			
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("ParseException: " + e.getMessage());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("MalformedURLException: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("IOException: " + e.getMessage());
		}
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
		sentQuery(QueryKind.AddInvoice, invoice);		
	}

}
