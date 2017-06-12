package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Product;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;

public class ProductGenerator implements ProductStorage {
	TestDataStorageFactory factory;
	List<Product> productStorage;
	
	public ProductGenerator(TestDataStorageFactory factory){
		this.factory = factory;
		productStorage = new ArrayList<Product>();
		productStorage.add( new Product(factory,"Produkt1") );
		productStorage.add( new Product(factory,"Produkt2") );
		productStorage.add( new Product(factory,"Produkt3") );
	}
	
	@Override
	public Product[] getProducts() {
		return productStorage.toArray( new Product[productStorage.size()] );
	}
}
