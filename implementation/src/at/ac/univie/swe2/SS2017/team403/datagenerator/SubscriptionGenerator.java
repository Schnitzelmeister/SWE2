package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.Plan;
import at.ac.univie.swe2.SS2017.team403.model.Product;
import at.ac.univie.swe2.SS2017.team403.model.Subscription;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStateEnum;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

public class SubscriptionGenerator implements SubscriptionStorage {
	TestDataStorageFactory factory;
	List<Subscription> subscriptionStorage;
	
	public SubscriptionGenerator(TestDataStorageFactory factory){
		this.factory = factory;
		subscriptionStorage = new ArrayList<Subscription>();
		subscriptionStorage.add( new Subscription(factory,"Subscription1",new Plan(factory,"6666", "Premium", 66.66, 
				new Product(factory,"Produkt1","productId1"),"planId6"),System.currentTimeMillis()+1000000000,null,
				SubscriptionStateEnum.Active,"1","subscriptionId1"));
		subscriptionStorage.add( new Subscription(factory,"Subscription2",new Plan(factory,"2222", "Premium", 22.22, 
				new Product(factory,"Produkt2","productId2"),"planId2") ,System.currentTimeMillis()+1000000000,null,
				SubscriptionStateEnum.Active,"2","subscriptionId2"));
		subscriptionStorage.add( new Subscription(factory,"Subscription3",new Plan(factory,"3333", "Basic", 33.33,
				new Product(factory,"Produkt3","productId3"),"planId3"),System.currentTimeMillis()+1000000000,null,
				SubscriptionStateEnum.Active,"3","subscriptionId3"));
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
	for (Subscription s : subscriptionStorage) {
		if (s.getSubscriptionId().equals(subscription.getSubscriptionId())){
			throw new IllegalArgumentException("The Subscription "+ s.getName()+" already exists!");
		}
	}
	subscription.setFactory(factory);
	subscriptionStorage.add(subscription);
	}

}
