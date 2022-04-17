package com.example.job.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class myRowmapper implements RowMapper<Employee> {

	@Override
	public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Employee employee = new Employee();
		employee.setId(rs.getLong("id"));
		employee.setDescr(rs.getString("descr"));
		employee.setName(rs.getString("name"));
		employee.setId2(rs.getLong("id2"));
		employee.setId3(rs.getLong("id3"));
		employee.setId4(rs.getLong("id4"));
		employee.setId5(rs.getLong("id5"));
		employee.setAbc(rs.getString("abc"));
		employee.setCompany(rs.getString("company"));
		employee.setId7(rs.getLong("id7"));
		
		return employee;
	}

}
