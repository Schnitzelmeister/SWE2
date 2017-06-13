package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;

public class Product {
	private AbstractDataStorageFactory factory = null;
	private String name;
	
	public Product(AbstractDataStorageFactory factory, String name){
		this.factory = factory;
		this.name = name;
	}
	
	public Product(String name){
		this.name = name;
	}
	
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
