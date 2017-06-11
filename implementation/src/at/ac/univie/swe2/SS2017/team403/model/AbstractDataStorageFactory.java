package at.ac.univie.swe2.SS2017.team403.model;

/**
 * This factory-interface is used to create storages.
 * Abstract Factory Pattern
 */
public interface AbstractDataStorageFactory {
    public CustomerStorage CreateCustomerStorage();
    public InvoiceStorage CreateInvoiceStorage();
    public PlanStorage CreatePlanStorage();
    public ProductStorage CreateProductStorage();
    public SubscriptionStorage CreateSubscriptionStorage();
}
