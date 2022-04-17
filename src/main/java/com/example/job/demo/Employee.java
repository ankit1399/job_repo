package com.example.job.demo;

import javax.validation.constraints.Pattern;

public class Employee {

	
	private Long id;
	private String descr;
	private String name;
	private Long id2;
	private Long id3;
	private Long id4;
	private Long id5;
	
	private String abc;
	@Pattern(regexp = "[a-zA-Z@]*")
	private String company;
	private Long id7;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getName() {
		return name;
	}
//	public Employee(Long id, String desc, String name, Long id2, Long id3, Long id4, Long id5, String abc,
//			String company, Long id7) {
//		super();
//		this.id = id;
//		this.desc = desc;
//		this.name = name;
//		this.id2 = id2;
//		this.id3 = id3;
//		this.id4 = id4;
//		this.id5 = id5;
//		this.abc = abc;
//		this.company = company;
//		this.id7 = id7;
//	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId2() {
		return id2;
	}
	public void setId2(Long id2) {
		this.id2 = id2;
	}
	public Long getId3() {
		return id3;
	}
	public void setId3(Long id3) {
		this.id3 = id3;
	}
	public Long getId4() {
		return id4;
	}
	public void setId4(Long id4) {
		this.id4 = id4;
	}
	public Long getId5() {
		return id5;
	}
	public void setId5(Long id5) {
		this.id5 = id5;
	}
	public String getAbc() {
		return abc;
	}
	public void setAbc(String abc) {
		this.abc = abc;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Long getId7() {
		return id7;
	}
	public void setId7(Long id7) {
		this.id7 = id7;
	}
	
	
	
}
