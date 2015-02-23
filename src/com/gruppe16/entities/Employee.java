package com.gruppe16.entities;

public class Employee {
	String name, email, cellphoneNumber;
	
	Employee(String name, String email, String cellphoneNumber){
		setName(name);
		setEmail(email);
		setCellphoneNumber(cellphoneNumber);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCellphoneNumber() {
		return cellphoneNumber;
	}
	public void setCellphoneNumber(String cellphoneNumber) {
		this.cellphoneNumber = cellphoneNumber;
	}
}
