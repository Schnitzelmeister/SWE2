package at.ac.univie.swe2.SS2017.team403.datagenerator;

import at.ac.univie.swe2.SS2017.team403.model.Product;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;

public class ProductGenerator implements ProductStorage {
	TestDataStorageFactory factory;
	
	public ProductGenerator(TestDataStorageFactory factory){
		
	}
	
	@Override
	public Product[] getProducts() {
		return null;
	}
}
