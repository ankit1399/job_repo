package com.example.job.demo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class EmployeeitemPreparedStatementSetter implements ItemPreparedStatementSetter<Employee> {

	@Override
	public void setValues(Employee item, PreparedStatement ps) throws SQLException {
		ps.setLong(1, item.getId());
		ps.setString(2, item.getDescr());
		ps.setString(3, item.getName());
		ps.setLong(4, item.getId2());
		ps.setLong(5, item.getId3());
		ps.setLong(6, item.getId4());
		ps.setLong(7, item.getId5());
		ps.setString(8, item.getAbc());
		ps.setString(9, item.getCompany());
		ps.setLong(10, item.getId7());
		
		

	}

}
