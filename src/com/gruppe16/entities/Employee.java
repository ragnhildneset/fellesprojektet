package com.gruppe16.entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import com.gruppe16.database.DBConnect;

public class Employee {
	
	private static HashMap<Integer, Group> map = null;
	public static void addToGroup(int groupID, String g_name, int employeeid){
		if(!map.containsKey(groupID)){
			map.put(groupID, new Group(groupID, g_name));
		}
		map.get(groupID).members.add(getEmployee(employeeid));
	}
	
	public static Group getFromName(String input){
		for(Map.Entry<Integer, Group> e : map.entrySet()){
			if(e.getValue().name.contentEquals(input)){
				return e.getValue();
			}
		}
		return null;
	}
	
	public static Group getFromID(int id){
		return map.get(id);
	}
	
	public static class Group {
		public int id; public String name;
		private ArrayList<Employee> members = new ArrayList<Employee>();
		Group(int i, String n){
			this.id = i;
			this.name = n;
		}
		@SuppressWarnings("unchecked")
		public ArrayList<Employee> getMembers() {
			return (ArrayList<Employee>) members.clone();
		}
	}
	
	public static ArrayList<Group> getGroups(){
		if(map == null){
			map = new HashMap<Integer, Group>();
			DBConnect.getGroups();
		}
		ArrayList<Group> e = new ArrayList<Group>();
		for(Map.Entry<Integer, Group> s : map.entrySet()){
			e.add(s.getValue());
		}
		return e;
	}

	SimpleStringProperty firstName;
	SimpleStringProperty lastName;
	SimpleStringProperty email;
	SimpleStringProperty username;
	SimpleIntegerProperty employeeid;
	
	private static HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();
	
	public static ArrayList<Employee> getAll(){
		ArrayList<Employee> e = new ArrayList<Employee>();
		for(Employee r : employees.values()){
			e.add(r);
		}
		return e;
	}
	
	public static String[] getNames(){
		String[] ls = new String[employees.size()];
		int c = 0;
		for(Map.Entry<Integer, Employee> e : employees.entrySet()){
			ls[c] = e.getValue().getName();
			c++;
		}
		return ls;
	}
	
	public static Employee getEmployee(int key){
		return employees.get(key);
	}
	
	
	public Employee(int key, String firstName, String lastName, String email, String username) {
		employees.put(key, this);
		this.employeeid =  new SimpleIntegerProperty(key);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName =  new SimpleStringProperty(lastName);
		this.email =  new SimpleStringProperty(email);
		this.username = new SimpleStringProperty(username);
	}
	
	public String getName(){
		return lastName.get() + ", " + firstName.get();
	}

	public String getFirstName() {
		return firstName.get();
	}

	public String getLastName() {
		return lastName.get();
	}

	public String getEmail() {
		return email.get();
	}

	public String getUsername() {
		return username.get();
	}

	public int getEmployeeID() {
		return employeeid.get();
	}

	public String toString(){
		return getName();
	}

	public boolean invite(int appid) {
		return DBConnect.inviteEmployee(this, appid);
	}
	
	@Override
	public boolean equals(Object other){
		if(other == null){
			return false;
		}
		Employee e = (Employee) other;
		if(e.getEmployeeID() == this.getEmployeeID()){
			return true;
		}
		return false;
	}
		
	
}
