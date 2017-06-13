package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;

//Produkte in FastBill
public class Plan {
	private AbstractDataStorageFactory factory = null;
	private String remoteId;
	private String name;
	private double rate;
	private Product product;
	private String planId;
	
	public Plan(AbstractDataStorageFactory factory, String remoteId, String name, Double rate, Product product, String planId){
		this.factory = factory;
		this.remoteId = remoteId;
		this.name = name;
		this.rate = rate;
		this.product = product;
		this.planId = planId;
	}
	
	public Plan(String remoteId, String name, Double rate, Product product, String planId){
		this.remoteId = remoteId;
		this.name = name;
		this.rate = rate;
		this.product = product;
		this.planId = planId;
	}
	
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	public String getName() {
		return name;
	}
	
	//the price a user gets charged	each month for subscribing to that plan
	//UNIT_PRICE
	public double getRate() {
		return rate;
	}
	
	//a remote identifer in FastBill
	public String getRemoteId() {
		return remoteId;
	}
	
	//list of Products in this Plan
	public Product getProduct() {
		return product;
	}
	
	public String getPlanId() {
		return planId;
	}
	
	@Override
	public String toString() {
		return "Tarif " + name + " to  product " + this.product.toString();
	}
}
