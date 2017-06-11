package at.ac.univie.swe2.SS2017.team403.model;

import java.util.List;

public class Customer {
	private String localId;
	private String remoteId;
	private String lastName;
	private List<Subscription> subscriptions;
	
	public String getLocalId() {
		return localId;
	}

	//a remote identifer in FastBill
	public String getRemoteId() {
		return remoteId;
	}

	public String getLastName() {
		return lastName;
	}
	
	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}
	
	public Customer(String localId, String remoteId, String lastName) {
		this.localId = localId;
		this.remoteId = remoteId;
		this.lastName = lastName;
	}
}
