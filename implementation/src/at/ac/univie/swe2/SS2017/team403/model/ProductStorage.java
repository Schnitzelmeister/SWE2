package at.ac.univie.swe2.SS2017.team403.model;

/**
 * This interface is used to handle all operations connected to Products.
 */
public interface ProductStorage {
	public Product[] getProducts(/* params */);
	public void addProduct(Product product) throws IllegalArgumentException;
}
