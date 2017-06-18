package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;

/**
 * 
 * the class contains the information about a plan. 
 * a plan will be store via a factory in fastbill. 
 * a plan hast a remoteid, name, rate and a plan id.
 * 
 *
 */
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
	/**
	 * to set the factory which store a plan
	 * @param factory the factory which store a product
	 */
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * the method for getting the name of a plan
	 * @return the name of a plan
	 */
	public String getName() {
		return name;
	}
	

	/**
	 * the price a user gets charged	each month for subscribing to that plan
	 * @return the montly rate
	 */
	public double getRate() {
		return rate;
	}
	
	
	/**
	 * the method for getting the remote id in FastBill
	 * @return a remote identifier in FastBill
	 */
	public String getRemoteId() {
		return remoteId;
	}
	
	//list of Products in this Plan
	/**
	 * A plan belongs a product.
	 * @return the product to which the plan belongs.
	 */
	public Product getProduct() {
		return product;
	}
	
	/**
	 * the method returns the plan id of a plan
	 * @return the plan id of a plan
	 */
	public String getPlanId() {
		return planId;
	}
	
	@Override
	public String toString() {
		return "Tarif " + name + " to  product " + this.product.toString();
	}
}
