package com.example.job.demo;

import org.springframework.batch.core.SkipListener;

public class skipListener implements SkipListener<Employee, EmployeeFinal> {

	@Override
	public void onSkipInRead(Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSkipInWrite(EmployeeFinal item, Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSkipInProcess(Employee item, Throwable t) {
		System.out.println("Skipping proccesing of item with id " +item.getId());

	}

}
