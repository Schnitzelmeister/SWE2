package at.ac.univie.swe2.SS2017.team403.model;

/**
 * This interface is used to handle all operations connected to Plans.
 */
public interface PlanStorage {
	public Plan[] getPlans();
	public void addPlan(Plan plan) throws IllegalArgumentException;
	public Iterator<Plan> getPlanByProductNameIterator(String productName);
}
