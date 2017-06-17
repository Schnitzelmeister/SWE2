package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
/**
 * 
 * the class contains information about a customer.
 * an object of the class will be store via factory
 * 
 *
 */
public class Customer implements Billing, CustomerReportNotifier {
	private AbstractDataStorageFactory factory = null;
	private String localId;
	private String remoteId;
	private String lastName;
	private String firstName;
	private String email;
	private String phone;
	
	public Customer(AbstractDataStorageFactory factory, String localId, String remoteId, String lastName, String firstName, String email, String phone) {
		this.factory = factory;
		this.localId = localId;
		this.remoteId = remoteId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
	}


	public Customer(String localId, String remoteId, String lastName, String firstName, String email, String phone) {
		this.localId = localId;
		this.remoteId = remoteId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
	}
	
	/**
	 * to set the factory which is used to store the object
	 * @param factory
	 */
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * to get the local id
	 * @return the local id of the customer
	 */
	public String getLocalId() {
		return localId;
	}
	/**
	 * to set the local id
	 * @param localId the local id of the customer
	 */
	public void setLocalId(String localId) {
		this.localId = localId;
	}

	//a remote identifer in FastBill
	/**
	 * to get the remote identifer in FastBill
	 * @return remote identifier in FastBall
	 */
	public String getRemoteId() {
		return remoteId;
	}
	/**
	 * to set the remote identifier in FastBill
	 * @param remoteId
	 */
	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	/**
	 * to get the last name of the customer
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * to set the last name of the customer
	 * @param lastName the last name of the customer
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * to get the first name of the customer
	 * @return the first name of the customer
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * to set the first name of the customer
	 * @param firstName the first name of the customer
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * to get the e-mail address of the customer
	 * @return the e-mail address of the customer
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * to set the e-mail address of the customer
	 * @param email the e-mail address of the customer
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * to get the phone number of the customer
	 * @return the phone number of the customer
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * to set the phone number of the customer
	 * @param phone the phone number of the customer
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * to get an array of the subscriptions which belongs the customer
	 * @return an array of the subscriptions
	 */
	public Subscription[] getSubscriptions() {
		return factory.createSubscriptionStorage().getSubscriptionsByCustomer(this);
	}
	
	@Override
	public boolean billing() {
		return this.billing(null);
	}
	
	@Override
	public boolean billing(Payment[] payments) {
		boolean changed = false;
		for(Subscription subscription : getSubscriptions()) {
			if (subscription.billing(payments))
				changed = true;
		}
		
		return changed;
	}

	@Override
	public void send() {
		//send email
		System.out.println("send Email to " + this.email);
	}

	@Override
	public String toString() {
		return this.lastName + " " + this.firstName + " (localId=" + this.localId + ", remoteId=" + this.remoteId + ")";
	}
}
