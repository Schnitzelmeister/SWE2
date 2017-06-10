package at.ac.univie.swe2.SS2017.team403.model;


//Produkte in FastBill
public class Plan {
	private String remoteId;
	//a remote identifer in FastBill
	public String getRemoteId() {
		return remoteId;
	}
	
	private String name;
	private double rate;
	private Product product;
	
	public String getName() {
		return name;
	}
	
	//the price a user gets charged	each month for subscribing to that plan
	//UNIT_PRICE
	public double getRate() {
		return rate;
	}
	
	//list of Products in this Plan
	public Product getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return "Tarif " + name + " to  product " + this.product.toString();
	}
}
