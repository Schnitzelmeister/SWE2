package at.ac.univie.swe2.SS2017.team403.model;

public interface SubscriptionStorage {
	public Subscription[] getSubscriptions(/* params */);

	public Subscription[] getSubscriptionsByCustomer(Customer customer);
}
