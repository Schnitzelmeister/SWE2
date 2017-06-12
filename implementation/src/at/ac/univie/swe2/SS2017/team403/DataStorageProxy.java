package at.ac.univie.swe2.SS2017.team403;

import at.ac.univie.swe2.SS2017.team403.datagenerator.AbstractDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.datagenerator.TestDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.fastbill.FastBillDataStorageFactory;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;

/**
 * This class is used to handle all Storages.
 * Proxy Pattern
 */
public class DataStorageProxy {
	private AbstractDataStorageFactory factory;
	
	public DataStorageProxy(boolean productive) {
		if (productive) {
			factory = new FastBillDataStorageFactory();
		}
		else {
			factory = new TestDataStorageFactory();
		}
	}
	
	public CustomerStorage getCustomerStorage() {
		return factory.CreateCustomerStorage();
	}
}
