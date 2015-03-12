package com.gruppe16.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.Building;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Notif;
import com.gruppe16.entities.Room;
import com.gruppe16.main.Login;

public class DBConnect {
	
	private static Connection con = null;
	
	private static ArrayList<Notif> notifs = null;
	public static ArrayList<Notif> getInvites(){
		if(notifs != null){
			return notifs;
		}
		notifs = new ArrayList<Notif>();
		String q = "select A.Title, A.description, A.totime, A.fromtime, A.appdate, E.givenName, E.surname, R.status, R.alarm, R.appid\n"
				+ "from AppointmentAndEmployee as R, Appointment as A, Employee as E\n"
				+ "where R.employeeid = "+Login.getCurrentUserID()+" and A.appointmentID = R.appid and E.employeeid = A.ownerid;";
		try{
			PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) p.executeQuery();
			while(rs.next()){
				notifs.add(new Notif(rs.getString("A.Title"),
						rs.getString("A.description"),
						rs.getTime("A.totime").toString(),
						rs.getTime("A.fromtime").toString(),
						rs.getDate("A.appdate").toString(),
						String.valueOf(rs.getInt("R.alarm")),
						rs.getString("E.surname") + ", " + rs.getString("E.givenName"),
						rs.getInt("R.appid"),
						rs.getInt("R.status")));
			}
			return notifs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static HashMap<Integer,Employee> getEmployees(){

		String q = "SELECT E.employeeid, E.givenName, E.surname, E.email, U.username FROM Employee as E, UserAndID as U WHERE U.employeeid = E.employeeid;";

		HashMap<Integer, Employee> map = new HashMap<Integer, Employee>();
		try{
			PreparedStatement p = getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) p.executeQuery();
			while(rs.next()){
				int key = rs.getInt("E.employeeid");
				Employee e = new Employee(key,rs.getString("E.givenName"), rs.getString("E.surname"), rs.getString("E.email"), rs.getString("username"));
				map.put(key, e);
			}return map;
		}catch(Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	public static HashMap<Integer, Building> getBuildings(){
		String q = "SELECT buildingID, name, description FROM Building;";
		HashMap<Integer, Building> map = new HashMap<Integer, Building>();
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				int key = rs.getInt("buildingID");
				Building b = new Building(Integer.parseInt(rs.getString("buildingID")), rs.getString("name"), rs.getString("description"));
				map.put(key, b);
			}return map;
		} catch (Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	
	public static HashMap<Integer, Room> getRooms(){
		String q = "SELECT roomNumber, capacity, roomName, description, buildingID, B.buildingName FROM Room, Building as B WHERE B.BuildingID = buildingID;";
		HashMap<Integer, Room> map = new HashMap<Integer, Room>();
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				int key = rs.getInt("roomNumber");
				Room r = new Room(rs.getInt("roomNumber"), rs.getInt("capacity"), rs.getString("roomName"), rs.getString("description"), rs.getInt("buildingID"), rs.getString("B.buildingName"));
				map.put(key, r);
			}return map;
		} catch (Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	public static boolean deleteRoom(int key){
		String q = "delete from Room where roomNumber = " + key + ";";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addAppointment(String title, String description, Date date, Time fromTime, Time toTime, Employee host) {
		String query = "INSERT INTO Appointment (title, description, appdate, fromtime, totime, ownerid) VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement e = getConnection().prepareStatement(query);
			e.setString(1, title);
			e.setString(2, description);
			e.setDate(3, date);
			e.setTime(4, fromTime);
			e.setTime(5, toTime);
			e.setInt(6, host.getEmployeeID());
			e.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<Integer, Appointment> getAppointments(){
		String q = "SELECT appointmentID, title, description, appdate, totime, fromtime, ownerid FROM Appointment ";
		HashMap<Integer, Appointment> map = new HashMap<Integer, Appointment>();
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
				while(rs.next()){
					int key = rs.getInt("appointmentID");
					Appointment a = new Appointment(Integer.parseInt(rs.getString("appointmentID")), rs.getString("title"), rs.getString("description"), LocalDate.parse(rs.getString("appdate")), LocalTime.parse(rs.getString("totime")), LocalTime.parse(rs.getString("fromtime")), Integer.parseInt(rs.getString("ownerid")), LocalTime.now());
					map.put(key, a);
				}return map;
		}catch (Exception e) {
			e.printStackTrace();
			
		}return null;
	}
	
	public static Connection getConnection(){
		
		if(con == null){
			DBLogin.login();
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			
			try {
				con = DriverManager.getConnection(DBLogin.getURL(), DBLogin.getUser(), DBLogin.getPass());
			} catch (SQLException e) {
				System.err.println("Username, password or the url for the database is wrong.");
			}
		}
		
		return con;
	}

	public static boolean deleteEmployee(int employeeID) {
		String q = "delete from Employee where employeeid = " + employeeID + ";";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteAppointment(int appointmentID) {
		String q = "delete from Appointment where appointmentID = " + appointmentID + ";";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
