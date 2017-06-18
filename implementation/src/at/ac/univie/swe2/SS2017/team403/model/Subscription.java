package at.ac.univie.swe2.SS2017.team403.model;

import java.util.List;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
/**
 * 
 * this class contains the information about a subscription.
 * 
 *
 */
public class Subscription implements Billing {
	private AbstractDataStorageFactory factory = null;
	private String name;
	private Plan plan;
	private long validTill;
	private List<Invoice> invoices;
	private SubscriptionStateEnum state;
	private String localId;
	private String subscriptionId;
	private String localCustomerId;
	
	public Subscription(AbstractDataStorageFactory factory, String name, Plan plan, long validTill, List<Invoice> invoices, 
			SubscriptionStateEnum state, String localId, String subscriptionId, String localCustomerId){
		this.factory = factory;
		this.name = name;
		this.plan = plan;
		this.validTill = validTill;
		this.invoices = invoices;
		this.state = state;
		this.localId = localId;
		this.localCustomerId = localCustomerId;
	}
	
	public Subscription(String name, Plan plan, long validTill, List<Invoice> invoices, 
			SubscriptionStateEnum state, String localId, String subscriptionId, String localCustomerId){
		this.name = name;
		this.plan = plan;
		this.validTill = validTill;
		this.invoices = invoices;
		this.state = state;
		this.localId = localId;
		this.subscriptionId = subscriptionId;
		this.localCustomerId = localCustomerId;
	}
	/**
	 * the method for setting the factory of this specific subscription
	 * @param factory a factory 
	 */
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	public String getName() {
		return name;
	}

	public Plan getPlan() {
		return plan;
	}
	
	public long getValidTill() {
		return validTill;
	}
	
	public List<Invoice> getInvoices() {
		return invoices;
	}
	
	public SubscriptionStateEnum getState() {
		return state;
	}
	
	public String getLocalId(){
		return localId;
	}
	
	public String getSubscriptionId() {
		return subscriptionId;
	}
	
	public void setLocalCustomerId(String localCustomerId){
		this.localCustomerId=localCustomerId;
	}
	
	public String getLocalCustomerId(){
		return this.localCustomerId;
	}
	
	@Override
	public boolean billing() {
		return billing(null);
	}

	@Override
	public boolean billing(Payment[] payments) {
		// TODO Auto-generated method stub
		
		//process Payments
		if (payments != null) {
			
		}
		
		//generate new invoices
		
		return false;
	}


}
