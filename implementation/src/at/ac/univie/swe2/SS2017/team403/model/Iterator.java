package at.ac.univie.swe2.SS2017.team403.model;
/**
 * 
 * a generic iterator class
 *
 * @param <T> Type of an object which uses the class to iterate
 */
public interface Iterator<T> {
	   public boolean hasNext();
	   public int count();
	   public T next();
}
