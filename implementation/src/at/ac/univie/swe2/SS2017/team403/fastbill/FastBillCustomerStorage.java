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
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.ac.univie.swe2.SS2017.team403.BackOfficeSystem;
import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.Invoice;
import at.ac.univie.swe2.SS2017.team403.model.Iterator;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;
/**
 * 
 * the class is used to handle the connection with FastBill to get or store customer(s)
 *  
 *
 */
public class FastBillCustomerStorage implements CustomerStorage {

	private AbstractDataStorageFactory factory;

	public FastBillCustomerStorage(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}

	private enum QueryKind {
		GetAllCustomers, GetCustomerByRemoteId, GetCustomerByLocalId, CreateCustomer
	}

	/**
	 * The method connects to the FastBill and sends a HTTP-Request to get/creat Information about Customer
	 * 
	 * @param queryKind the kind of the query. 
	 * @param param the parameter which specified the customer 
	 * @return an array of the customers
	 * @throws IllegalArgumentException if the kind of query and the parameter not used
	 */
	private Customer[] sentQuery(QueryKind queryKind, Object param) throws IllegalArgumentException {
		try {
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

			switch (queryKind) {
			case GetAllCustomers:
				query.append("\"SERVICE\":\"customer.get\",");
				query.append("\"FILTER\":{}");
				break;
			case GetCustomerByRemoteId:
				query.append("\"SERVICE\":\"customer.get\",");
				query.append("\"FILTER\":{\"CUSTOMER_ID\":\"" + param.toString() + "\"}");
				break;
			case GetCustomerByLocalId:
				query.append("\"SERVICE\":\"customer.get\",");
				query.append("\"FILTER\":{\"CUSTOMER_NUMBER\":\"" + param.toString() + "\"}");
				break;
			case CreateCustomer:
				Customer newCustomer = (Customer) param;
				query.append("\"SERVICE\":\"customer.create\",");
				query.append("\"DATA\":{");
				query.append("\"CUSTOMER_TYPE\":\"consumer\",");
				query.append("\"CUSTOMER_NUMBER\":\"" + newCustomer.getLocalId() + "\",");
				query.append("\"LAST_NAME\":\"" + newCustomer.getLastName() + "\",");
				query.append("\"FIRST_NAME\":\"" + newCustomer.getFirstName() + "\",");
				query.append("\"EMAIL\":\"" + newCustomer.getEmail() + "\",");
				query.append("\"PHONE\":\"" + newCustomer.getPhone() + "\"");
				query.append("}");
				break;
			default:
				throw new IllegalArgumentException(
						"at.ac.univie.swe2.SS2017.team403.fastbill.FastBillCustomerStorage queryKind=" + queryKind
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

			// System.out.println(outputB);

			List<Customer> ret = new ArrayList<Customer>();

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(outputB.toString());

			if (((JSONObject) json.get("RESPONSE")).get("ERRORS") != null)
				throw new IllegalArgumentException(
						"FastBill error: " + ((JSONObject) json.get("RESPONSE")).get("ERRORS").toString());

			if (queryKind == QueryKind.GetAllCustomers || queryKind == QueryKind.GetCustomerByRemoteId
					|| queryKind == QueryKind.GetCustomerByLocalId) {
				JSONArray customers = (JSONArray) ((JSONObject) json.get("RESPONSE")).get("CUSTOMERS");
				for (Object customer : customers) {
					String remoteId = (String) ((JSONObject) customer).get("CUSTOMER_ID");
					String localId = (String) ((JSONObject) customer).get("CUSTOMER_NUMBER");
					String lastName = (String) ((JSONObject) customer).get("LAST_NAME");
					String firstName = (String) ((JSONObject) customer).get("FIRST_NAME");
					String email = (String) ((JSONObject) customer).get("EMAIL");
					String phone = (String) ((JSONObject) customer).get("PHONE");

					ret.add(new Customer(factory, localId, remoteId, lastName, firstName, email, phone));
				}
			} else if (queryKind == QueryKind.CreateCustomer) {
				String customerId = ((JSONObject) json.get("RESPONSE")).get("CUSTOMER_ID").toString();
				Customer newCustomer = (Customer) param;

				ret.add(new Customer(factory, newCustomer.getLocalId(), customerId, newCustomer.getLastName(),
						newCustomer.getFirstName(), newCustomer.getEmail(), newCustomer.getPhone()));
			}
			return ret.toArray(new Customer[ret.size()]);

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
	public Customer[] getCustomers() throws IllegalArgumentException {
		return sentQuery(QueryKind.GetAllCustomers, null);
	}

	@Override
	public Customer getCustomerByLocalId(String localId) throws IllegalArgumentException {
		Customer[] results = sentQuery(QueryKind.GetCustomerByLocalId, localId);
		if (results == null || results.length == 0)
			throw new IllegalArgumentException("The User with localId=" + localId + " does not exist");
		else if (results.length != 1)
			throw new IllegalArgumentException("There are many Users with localId=" + localId);
		return results[0];
	}

	@Override
	public Customer getCustomerByRemoteId(String remoteId) throws IllegalArgumentException {
		Customer[] results = sentQuery(QueryKind.GetCustomerByRemoteId, remoteId);
		if (results == null || results.length == 0)
			throw new IllegalArgumentException("The User with remoteId=" + remoteId + " does not exist");
		else if (results.length != 1)
			throw new IllegalArgumentException("There are many Users with remoteId=" + remoteId);
		return results[0];
	}

	@Override
	public String addCustomer(Customer customer) throws IllegalArgumentException {
		Customer result = sentQuery(QueryKind.CreateCustomer, customer)[0];
		return result.getRemoteId();
	}

	 
	/**
	 * inner class, which are used for Iterator Pattern
	 * 
	 *
	 */
	private class AllCustomerIterator implements Iterator<Customer> {
		private List<Customer> instanceStorage;
		private int index = 0;

		AllCustomerIterator() {
			getCustomers();
			instanceStorage = new ArrayList<Customer>(Arrays.asList(getCustomers()));
		}

		@Override
		public boolean hasNext() {
			return (index < instanceStorage.size());
		}

		@Override
		public int count() {
			return instanceStorage.size();
		}

		@Override
		public Customer next() {
			if (this.hasNext()) {
				return instanceStorage.get(index++);
			} else {
				return null;
			}
		}
	}

	// 
	/**
	 * inner class, which are used for Iterator Pattern
	 * 
	 *
	 */
	private class DebtCustomerIterator implements Iterator<Customer> {
		private List<Customer> instanceStorage;
		private int index = 0;

		DebtCustomerIterator() {
			getCustomers();
			instanceStorage = new ArrayList<Customer>();
			for (Customer customer : getCustomers()) {
				boolean hasDebt = false;

				search: for (Subscription subscription : customer.getSubscriptions()) {
					for (Invoice invoice : subscription.getInvoices()) {
						if (invoice.isUnpaid()) {
							hasDebt = true;
							break search;
						}
					}
				}

				if (hasDebt) {
					instanceStorage.add(customer);
				}
			}
		}

		@Override
		public boolean hasNext() {
			return (index < instanceStorage.size());
		}

		@Override
		public int count() {
			return instanceStorage.size();
		}

		@Override
		public Customer next() {
			if (this.hasNext()) {
				return instanceStorage.get(index++);
			} else {
				return null;
			}
		}
	}

	@Override
	public Iterator<Customer> getCustomersIterator(boolean onlyWithDebt) {
		if (onlyWithDebt) {
			return new DebtCustomerIterator();
		} else {
			return new AllCustomerIterator();
		}
	}

}
