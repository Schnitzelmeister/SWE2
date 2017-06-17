package at.ac.univie.swe2.SS2017.team403.datagenerator;

import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;
import at.ac.univie.swe2.SS2017.team403.model.InvoiceStorage;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.model.ProductStorage;
import at.ac.univie.swe2.SS2017.team403.model.SubscriptionStorage;

/**
 * This factory-interface is used to create storages.
 * Abstract Factory Pattern
 */
public interface AbstractDataStorageFactory {
    public CustomerStorage createCustomerStorage();
    public InvoiceStorage createInvoiceStorage();
    public PlanStorage createPlanStorage();
    public ProductStorage createProductStorage();
    public SubscriptionStorage createSubscriptionStorage();
}
