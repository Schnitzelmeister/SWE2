package at.ac.univie.swe2.SS2017.team403.model;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;

public class Invoice {
	private AbstractDataStorageFactory factory = null;
	private String localId;
	
	public Invoice(AbstractDataStorageFactory factory, String localId){
		this.factory = factory;
		this.localId = localId;
	}
	
	public Invoice(String localId){
		this.localId = localId;
	}
	
	public void setFactory(AbstractDataStorageFactory factory) {
		this.factory = factory;
	}
	
	public void setId(String localId) {
		this.localId = localId;
	}
	
	public String getId() {
		return localId;
	}
	
	public boolean isUnpaid() {
		return true;
	}
}
