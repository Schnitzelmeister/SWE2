package at.ac.univie.swe2.SS2017.team403.model;

/**
 * This interface is used to handle all operations connected to Subscriptions.
 */
public interface SubscriptionStorage {
	public Subscription[] getSubscriptions();
	public Subscription[] getSubscriptionsByCustomer(Customer customer);
	public Subscription[] getSubScriptionByRemoteId(String remoteId);
}
