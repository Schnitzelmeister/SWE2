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
		planStorage.add( new Plan(factory,"111", "", 11.11, new Product(factory,"Produkt1")) );
		planStorage.add( new Plan(factory,"222", "", 22.22, new Product(factory,"Produkt2")) );
		planStorage.add( new Plan(factory,"333", "", 33.33, new Product(factory,"Produkt3")) );
		planStorage.add( new Plan(factory,"444", "", 44.44, new Product(factory,"Produkt3")) );
		planStorage.add( new Plan(factory,"111", "", 55.55, new Product(factory,"Produkt2")) );
		planStorage.add( new Plan(factory,"222", "", 66.66, new Product(factory,"Produkt1")) );
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
		// TODO Auto-generated method stub
		
	}

}