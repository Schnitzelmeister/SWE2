package at.ac.univie.swe2.SS2017.team403.model;

/**
 * 
 * Composite Pattern with Customer and Subscription
 */
public interface Billing {
	//return true, when the Object is changed
	public boolean billing();
	//return true, when the Object is changed
	public boolean billing(Payment[] payments);
}
