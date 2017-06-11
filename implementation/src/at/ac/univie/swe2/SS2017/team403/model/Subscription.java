package at.ac.univie.swe2.SS2017.team403.model;

import java.util.Date;
import java.util.List;

public class Subscription implements Billing {
	private String name;
	private Plan plan;
	private Date validTill;
	private List<Invoice> invoices;
	private SubscriptionStateEnum state;
	
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
