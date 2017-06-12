package at.ac.univie.swe2.SS2017.team403.datagenerator;

import java.util.List;

import at.ac.univie.swe2.SS2017.team403.model.Plan;
import at.ac.univie.swe2.SS2017.team403.model.PlanStorage;

public class PlanGenerator implements PlanStorage {
	TestDataStorageFactory factory;
	List<Plan> planStorage;
	
	public PlanGenerator(TestDataStorageFactory factory){
		this.factory = factory;
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

}