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
		productStorage.add( new Product(factory,"Produkt2","productId2") );
		productStorage.add( new Product(factory,"Produkt3","productId3") );
	}
	
	@Override
	public Product[] getProducts() {
		return productStorage.toArray( new Product[productStorage.size()] );
	}

	@Override
	public void addProduct(Product product) throws IllegalArgumentException {
		for (Product p : productStorage) {
			if (p.getProductId().equals(product.getProductId())) {
				throw new IllegalArgumentException("The Product "+ p.getName()+" already exists!");
			}
		}	
		product.setFactory(factory);
		productStorage.add(product);	
	}
}
