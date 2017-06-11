package at.ac.univie.swe2.SS2017.team403.model;

public class Invoice {
	private String localId;
	
	public String getId() {
		return localId;
	}
	
	public boolean isUnpaid() {
		return true;
	}
}
