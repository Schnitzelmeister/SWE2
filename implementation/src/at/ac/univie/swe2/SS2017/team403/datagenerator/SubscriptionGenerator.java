package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.Invoice;
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
		List dummyInvoiceList = new ArrayList<Invoice>(); 
		dummyInvoiceList.add( new Invoice("1", "1") );
		dummyInvoiceList.add( new Invoice("2", "2") );
		
		subscriptionStorage.add( new Subscription(factory,"Subscription1",new Plan(factory,"6666", "Premium", 66.66, 
				new Product(factory,"Produkt1","productId1"),"planId6"),System.currentTimeMillis()+1000000000,dummyInvoiceList,
				SubscriptionStateEnum.Active,"1","subscriptionId1", "1"));
		subscriptionStorage.add( new Subscription(factory,"Subscription2",new Plan(factory,"2222", "Premium", 22.22, 
				new Product(factory,"Produkt2","productId2"),"planId2") ,System.currentTimeMillis()+1000000000,null,
				SubscriptionStateEnum.Active,"2","subscriptionId2", "2"));
		subscriptionStorage.add( new Subscription(factory,"Subscription3",new Plan(factory,"3333", "Basic", 33.33,
				new Product(factory,"Produkt3","productId3"),"planId3"),System.currentTimeMillis()+1000000000,null,
				SubscriptionStateEnum.Active,"3","subscriptionId3", "3"));
	}

	@Override
	public Subscription[] getSubscriptions() {
		return subscriptionStorage.toArray( new Subscription[subscriptionStorage.size()] );
	}

	@Override
	public Subscription[] getSubscriptionsByCustomer(Customer customer) {
		String customerLocalId = customer.getLocalId();
		List subscriptionList = new ArrayList<Subscription>();
		for(Subscription s : subscriptionStorage){
			if(s.getLocalCustomerId().equals(customerLocalId))
				subscriptionList.add(s);
		}
		return (Subscription[]) subscriptionList.toArray( new Subscription[subscriptionList.size()] ); 
	}

	@Override
	public Subscription[] getSubScriptionByRemoteId(String remoteId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Invoice[] getInvoicesForCustomer(Customer customer){
		String customerLocalId = customer.getLocalId();
		for(Subscription s : subscriptionStorage){
			if(s.getLocalCustomerId().equals(customerLocalId))
				return (Invoice[]) s.getInvoices().toArray( new Invoice[s.getInvoices().size()] );
		}
		throw new IllegalArgumentException("Der Kunde hat keine Subscriptions");
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
