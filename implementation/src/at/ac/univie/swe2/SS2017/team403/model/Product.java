package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
/**
 * 
 * This class contains the information about a product. 
 * A product has an id and a name. the factory is the way to storage the product.
 *
 */
public class Product {
	private AbstractDataStorageFactory factory = null;
	private String name;
	private String productId;
	
	public Product(AbstractDataStorageFactory factory, String name, String productId){
		this.factory = factory;
		this.name = name;
		this.productId = productId;
	}
	
	public Product(String name, String productId){
		this.name = name;
		this.productId = productId;
	}
	/**
	 * to set the factory which store a product
	 * @param factory the factory which store a product
	 */
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * getting the name of a product
	 * @return the name of a product
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @return the product id of a product
	 */
	public String getProductId() {
		return productId;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
