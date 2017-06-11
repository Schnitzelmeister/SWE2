package at.ac.univie.swe2.SS2017.team403.model;

public class Customer implements Billing {
	private AbstractDataStorageFactory factory = null;
	private String localId;
	private String remoteId;
	private String lastName;
	
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

	public Subscription[] getSubscriptions() {
		return factory.CreateSubscriptionStorage().getSubscriptionsByCustomer(this);
	}
	
	public void billing() {
		for(Subscription subscription : getSubscriptions())
			subscription.billing();
	}
	
	public Customer(AbstractDataStorageFactory factory, String localId, String remoteId, String lastName) {
		this.factory = factory;
		this.localId = localId;
		this.remoteId = remoteId;
		this.lastName = lastName;
	}

	public Customer(String localId, String remoteId, String lastName) {
		this.localId = localId;
		this.remoteId = remoteId;
		this.lastName = lastName;
	}}
