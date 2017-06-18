package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Iterator;
import at.ac.univie.swe2.SS2017.team403.model.Plan;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.model.Product;

/**
 * 
 *the class is used to generate and store some plans and handle with them.
 *
 */
public class PlanGenerator implements PlanStorage {
	AbstractDataStorageFactory factory;
	List<Plan> planStorage;
	
	public PlanGenerator(AbstractDataStorageFactory factory){
		this.factory = factory;
		planStorage = new ArrayList<Plan>();
		planStorage.add( new Plan(factory,"2222", "Premium", 22.22, new Product(factory,"Produkt2","productId2"), "planId2") );
		planStorage.add( new Plan(factory,"3333", "Basic", 33.33, new Product(factory,"Produkt3","productId3"), "planId3") );
		planStorage.add( new Plan(factory,"4444", "Deluxe", 44.44, new Product(factory,"Produkt3","productId3"), "planId4") );
		planStorage.add( new Plan(factory,"5555", "Basic", 55.55, new Product(factory,"Produkt2","productId2"), "planId5") );
		planStorage.add( new Plan(factory,"6666", "Premium", 66.66, new Product(factory,"Produkt1","productId1"), "planId6") );
	}
	
	@Override
	public Plan[] getPlans() {
		return planStorage.toArray( new Plan[planStorage.size()] );
	}
	
	private class AllPlansByProductNameIterator implements Iterator<Plan> {
		private int index = 0;
		ArrayList<Plan> plans = new ArrayList<Plan>();
		
		AllPlansByProductNameIterator(String productName){
			for (Plan p:planStorage){
				if(p.getProduct().getName().equals(productName)){
					plans.add(p);
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return (index < plans.size());
		}
		
		@Override
		public int count() {
			return plans.size();
		}
		
		@Override
		public Plan next() {
			if(this.hasNext()) {
				return plans.get(index++);
			} else {
				return null;
			}
		}
	}

	@Override
	public void addPlan(Plan plan) throws IllegalArgumentException {
		for (Plan p : planStorage)
			if (p.getPlanId().equals(plan.getPlanId())){
				throw new IllegalArgumentException("The Plan "+ p.getName()+" already exists!");
			}	
		plan.setFactory(factory);
		planStorage.add(plan);
		
	}

	@Override
	public Iterator<Plan> getPlanByProductNameIterator(String productName) {
		return new AllPlansByProductNameIterator(productName);
	}

}