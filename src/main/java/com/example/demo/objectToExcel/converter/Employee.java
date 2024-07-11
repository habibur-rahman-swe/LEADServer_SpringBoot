package com.example.demo.objectToExcel.converter;

public class Employee {
	private Long id;
	private String name;
	private String designation;
	private double salary;
	private String dateOfJoin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getDateOfJoin() {
		return dateOfJoin;
	}

	public void setDateOfJoin(String DateOfJoin) {
		this.dateOfJoin = DateOfJoin;
	}

	public Employee(Long id, String name, String designation, double salary, String DateOfJoin) {
		super();
		this.id = id;
		this.name = name;
		this.designation = designation;
		this.salary = salary;
		this.dateOfJoin = DateOfJoin;
	}

	public Employee() {
		super();
	}

}