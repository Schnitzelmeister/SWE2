package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.Plan;
import at.ac.univie.swe2.SS2017.team403.model.Product;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

public class SubscriptionGenerator implements SubscriptionStorage {
	TestDataStorageFactory factory;
	List<Subscription> subscriptionStorage;
	
	public SubscriptionGenerator(TestDataStorageFactory factory){
		this.factory = factory;
		subscriptionStorage = new ArrayList<Subscription>();
		subscriptionStorage.add( new Subscription(factory,"Subscription1",new Plan(factory,"1111", "", 11.11,new Product(factory,"Produkt1")),null,null,null));
		subscriptionStorage.add( new Subscription(factory,"Subscription2",new Plan(factory,"2222", "Premium", 22.22, new Product(factory,"Produkt2")),null,null,null));
		subscriptionStorage.add( new Subscription(factory,"Subscription3",new Plan(factory,"3333", "Basic", 33.33, new Product(factory,"Produkt3")),null,null,null));
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

	@Override
	public Subscription[] getSubScriptionByRemoteId(String remoteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSubscription(Subscription subscription) throws IllegalArgumentException {;
		
	}

}
