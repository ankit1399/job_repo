package com.example.job.demo;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class getSetFieldSetMapper implements FieldSetMapper<Employee> {

	@Override
	public Employee mapFieldSet(FieldSet fieldSet) throws BindException {
		// TODO Auto-generated method stub
		Employee employee = new Employee();
		employee.setId(fieldSet.readLong("id"));
		employee.setDesc(fieldSet.readString("desc"));
		employee.setName(fieldSet.readString("name"));
		employee.setId2(fieldSet.readLong("id2"));
		employee.setId3(fieldSet.readLong("id3"));
		employee.setId4(fieldSet.readLong("id4"));
		employee.setId5(fieldSet.readLong("id5"));
		employee.setAbc(fieldSet.readString("abc"));
		employee.setCompany(fieldSet.readString("company"));
		employee.setId7(fieldSet.readLong("id7"));
		
		return employee;
	}

}
