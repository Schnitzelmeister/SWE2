package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

public class SubscriptionGenerator implements SubscriptionStorage {
	TestDataStorageFactory factory;
	List<Subscription> subscriptionStorage;
	
	public SubscriptionGenerator(TestDataStorageFactory factory){
		this.factory = factory;
	}

	@Override
	public Subscription[] getSubscriptions() {
		return subscriptionStorage.toArray( new Subscription[subscriptionStorage.size()] );
	}

	@Override
	public Subscription[] getSubscriptionsByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

}
