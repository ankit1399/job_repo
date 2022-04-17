package com.example.job.demo;

import java.util.UUID;

import org.springframework.batch.item.ItemProcessor;

public class EmployeeFinalItemProcessor implements ItemProcessor<Employee, EmployeeFinal> {

	@Override
	public EmployeeFinal process(Employee item) throws Exception {
		// TODO Auto-generated method stub
		EmployeeFinal employeeFinal= new EmployeeFinal(item);
		
		
		
		employeeFinal.setUuid(this.getUuid());
		return employeeFinal;
	}
	
	public String getUuid() {
		
		if (Math.random()<.10)
		{
			throw new RuntimeException();
		}
		
		return UUID.randomUUID().toString();
	}

}
