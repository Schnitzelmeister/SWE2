package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Plan;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;
import at.ac.univie.swe2.SS2017.team403.model.Product;


public class PlanGenerator implements PlanStorage {
	TestDataStorageFactory factory;
	List<Plan> planStorage;
	
	public PlanGenerator(TestDataStorageFactory factory){
		this.factory = factory;
		planStorage = new ArrayList<Plan>();
		planStorage.add( new Plan(factory,"2222", "Premium", 22.22, new Product(factory,"Produkt2")) );
		planStorage.add( new Plan(factory,"3333", "Basic", 33.33, new Product(factory,"Produkt3")) );
		planStorage.add( new Plan(factory,"4444", "Deluxe", 44.44, new Product(factory,"Produkt3")) );
		planStorage.add( new Plan(factory,"5555", "Basic", 55.55, new Product(factory,"Produkt2")) );
		planStorage.add( new Plan(factory,"6666", "Premium", 66.66, new Product(factory,"Produkt1")) );
	}
	
	@Override
	public Plan[] getPlans() {
		return planStorage.toArray( new Plan[planStorage.size()] );
	}

	@Override
	public Plan[] getPlansByProductName(String productName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plan getPlanByPlanName(String planName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPlan(Plan plan) throws IllegalArgumentException {
		for (Plan p : planStorage)
			if (p.getName().equals(plan.getName()) && p.getProduct().getName().equals(plan.getProduct().getName()))
				throw new IllegalArgumentException("The Plan "+ p.getName()+" already exists!");
		
		plan.setFactory(factory);
		planStorage.add(plan);
		
	}

}