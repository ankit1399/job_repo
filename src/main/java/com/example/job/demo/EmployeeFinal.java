package com.example.job.demo;

import org.springframework.beans.BeanUtils;

public class EmployeeFinal extends Employee {

	private String uuid;

	public EmployeeFinal(String id) {
		super();
		this.uuid = id;
	}

	public EmployeeFinal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeFinal(Employee item) {
		// TODO Auto-generated constructor stub
		BeanUtils.copyProperties(item, this);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String string) {
		this.uuid = string;
	}

}
