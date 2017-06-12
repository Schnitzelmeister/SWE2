package at.ac.univie.swe2.SS2017.team403.model;

import java.util.Date;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;

public class Subscription implements Billing {
	private AbstractDataStorageFactory factory = null;
	private String name;
	private Plan plan;
	private Date validTill;
	private List<Invoice> invoices;
	private SubscriptionStateEnum state;
	
	public Subscription(AbstractDataStorageFactory factory, String name, Plan plan, Date validTill, List<Invoice> invoices, 
			SubscriptionStateEnum state){
		this.factory = factory;
		this.name = name;
		this.plan = plan;
		this.validTill = validTill;
		this.invoices = invoices;
		this.state = state;
	}
	
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	public String getName() {
		return name;
	}

	public Plan getPlan() {
		return plan;
	}
	
	public Date getValidTill() {
		return validTill;
	}
	
	public List<Invoice> getInvoices() {
		return invoices;
	}
	
	public SubscriptionStateEnum getState() {
		return state;
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
