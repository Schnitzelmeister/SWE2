package at.ac.univie.swe2.SS2017.team403.model;

public class Customer implements Billing, CustomerReportNotifier {
	private AbstractDataStorageFactory factory = null;
	private String localId;
	private String remoteId;
	private String lastName;
	private String email;
	private String phone;
	
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}

	public String getLocalId() {
		return localId;
	}
	
	public void setLocalId(String localId) {
		this.localId = localId;
	}

	//a remote identifer in FastBill
	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Subscription[] getSubscriptions() {
		return factory.CreateSubscriptionStorage().getSubscriptionsByCustomer(this);
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
	
	public Customer(AbstractDataStorageFactory factory, String localId, String remoteId, String lastName, String email, String phone) {
		this.factory = factory;
		this.localId = localId;
		this.remoteId = remoteId;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	public Customer(String localId, String remoteId, String lastName, String email, String phone) {
		this.localId = localId;
		this.remoteId = remoteId;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	@Override
	public void send() {
		//send email
		System.out.println("send Email to " + this.email);
	}}
