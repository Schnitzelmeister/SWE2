package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Product;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;

public class ProductGenerator implements ProductStorage {
	TestDataStorageFactory factory;
	List<Product> productStorage;
	
	public ProductGenerator(TestDataStorageFactory factory){
		this.factory = factory;
	}
	
	@Override
	public Product[] getProducts() {
		return productStorage.toArray( new Product[productStorage.size()] );
	}
}
