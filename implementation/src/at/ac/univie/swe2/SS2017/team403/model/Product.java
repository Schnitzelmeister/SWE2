package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;

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
	
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	public String getName() {
		return name;
	}
	
	public String getProductId() {
		return productId;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
