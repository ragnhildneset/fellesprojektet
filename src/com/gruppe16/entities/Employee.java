package com.gruppe16.entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import com.gruppe16.database.DBConnect;

/**
 * The Class Employee.
 * 
 * @author Gruppe 16
 */
public class Employee {

	/** The employee's first name. */
	private SimpleStringProperty firstName;
	
	/** The employee's last name. */
	private SimpleStringProperty lastName;
	
	/** The employee's email. */
	private SimpleStringProperty email;
	
	/** The employee's username. */
	private SimpleStringProperty username;
	
	/** The employee's id. */
	private SimpleIntegerProperty id;
	
	/** The map of employee groups. */
	private static HashMap<Integer, Group> groupMap = null;
	
	/**
	 * Adds an employee to a group.
	 *
	 * @param groupID the group id 
	 * @param groupName the group name
	 * @param employeeID the employee ID
	 */
	public static void addToGroup(int groupID, String groupName, int employeeID){
		if(!groupMap.containsKey(groupID)){
			groupMap.put(groupID, new Group(groupID, groupName));
		}
		groupMap.get(groupID).members.add(DBConnect.getEmployees().get(employeeID));
	}
	
	/**
	 * Gets the group of the employee by group name.
	 *
	 * @param groupName the group name
	 * @return the group
	 */
	public static Group getFromName(String groupName){
		for(Map.Entry<Integer, Group> group : groupMap.entrySet()){
			if(group.getValue().name.contentEquals(groupName)){
				return group.getValue();
			}
		}
		return null;
	}
	
	/**
	 * The Class Group.
	 */
	public static class Group {
		
		/** The group id. */
		public int id; 
		
		/** The group name. */
		public String name;
		
		/** The members of a group. */
		private ArrayList<Employee> members = new ArrayList<Employee>();
		
		/**
		 * Instantiates a new group.
		 *
		 * @param id the group ID
		 * @param name the group name
		 */
		private Group(int id, String name){
				this.id = id;
				this.name = name;
			}
		
		/**
		 * Gets the members of the group.
		 *
		 * @return the members
		 */
		@SuppressWarnings("unchecked")
		public ArrayList<Employee> getMembers() {
			return (ArrayList<Employee>) members.clone();
		}
	}
	
	/**
	 * Gets all the groups.
	 *
	 * @return the groups
	 */
	public static ArrayList<Group> getGroups(){
		if(groupMap == null){
			groupMap = new HashMap<Integer, Group>();
			DBConnect.getGroups();
		}
		ArrayList<Group> e = new ArrayList<Group>();
		for(Map.Entry<Integer, Group> s : groupMap.entrySet()){
			e.add(s.getValue());
		}
		return e;
	}
	
	/**
	 * Instantiates a new employee.
	 *
	 * @param key the key
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param email the email
	 * @param username the username
	 */
	public Employee(int key, String firstName, String lastName, String email, String username) {
		this.id =  new SimpleIntegerProperty(key);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName =  new SimpleStringProperty(lastName);
		this.email =  new SimpleStringProperty(email);
		this.username = new SimpleStringProperty(username);
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return lastName.get() + ", " + firstName.get();
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName.get();
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName.get();
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email.get();
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username.get();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return id.get();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return getName();
	}

	/**
	 * Invite.
	 *
	 * @param appid the appid
	 * @return true, if successful
	 */
	public boolean invite(int appid) {
		return DBConnect.inviteEmployee(this, appid);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		return other == null ? false : ((Employee)other).getID() == this.getID();
	}
}
