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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.AppointmentAndEmployee;
import com.gruppe16.entities.Building;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Employee.Group;
import com.gruppe16.entities.Notif;
import com.gruppe16.entities.Room;
import com.gruppe16.main.Login;
import com.gruppe16.util.ListOperations;

/**
 * The Class DBConnect. Connects to the database, after getting log in information.
 * Has several functions for changing and retrieving data from the database.
 * 
 * @author Gruppe 16
 */
public class DBConnect {
	
	/** The connection url. */
	private static Connection con = null;

	/**
	 * Gets the groups.
	 */
	public static void getGroups(){
		try{
			String q = "select G.groupID, G.name, E.employeeid from Grouup as G, GroupMember as E, Employee as D where D.employeeid = E.employeeid and E.groupid = G.groupid;";
			PreparedStatement p = getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) p.executeQuery();
			while(rs.next()){
				int employeeid = rs.getInt("E.employeeid");
				int groupid = rs.getInt("G.groupID");
				String name = rs.getString("G.name");
				Employee.addToGroup(groupid, name, employeeid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the invited employees in the database.
	 *
	 * @return the invited employees
	 */
	public static ArrayList<Notif> getInvites(){
		ArrayList<Notif> notifications = new ArrayList<Notif>();
		String q = "select A.Title, A.description, A.totime, A.fromtime, A.appdate, E.givenName, E.surname, R.status, R.alarm, R.appid\n"
				+ "from AppointmentAndEmployee as R, Appointment as A, Employee as E\n"
				+ "where R.employeeid = "+Login.getCurrentUserID()+" and A.appointmentID = R.appid and E.employeeid = A.ownerid;";
		try{
			PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) p.executeQuery();
			while(rs.next()){
				notifications.add(new Notif(rs.getString("A.Title"),
						rs.getString("A.description"),
						rs.getTime("A.totime").toString(),
						rs.getTime("A.fromtime").toString(),
						rs.getDate("A.appdate").toString(),
						String.valueOf(rs.getInt("R.alarm")),
						rs.getString("E.surname") + ", " + rs.getString("E.givenName"),
						rs.getInt("R.appid"),
						rs.getInt("R.status")));
			}
			return notifications;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the employees from the database.
	 *
	 * @return the employees
	 */
	public static HashMap<Integer, Employee> getEmployees() {
//		if(_employees != null){
//			return (ArrayList<Employee>) ListOperations.hashToList(_employees);
//		}
		String q = "SELECT E.employeeid, E.givenName, E.surname, E.email, U.username FROM Employee as E, UserAndID as U WHERE U.employeeid = E.employeeid;";
		HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();
		try{
			PreparedStatement p = getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) p.executeQuery();
			while(rs.next()){
				int key = rs.getInt("E.employeeid");
				Employee e = new Employee(key,rs.getString("E.givenName"), rs.getString("E.surname"), rs.getString("E.email"), rs.getString("username"));
				employees.put(key, e);
			}
			return employees;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets a list of employees.
	 *
	 * @return the employee list
	 */
	public static Collection<Employee> getEmployeeList() {
		return getEmployees().values();
	}
	
	/**
	 * Adds a room reservation to the database, setting the appointment ID, room ID and building ID.
	 *
	 * @param appointmentID the appointment ID
	 * @param roomID the room ID
	 * @param BuildingID the building ID
	 */
	public static void addRoomReservation(int appointmentID, int roomID, int BuildingID){
		String q = "INSERT INTO RoomReservation(appid, roomid, BuildingID) VALUES (?, ?, ?)";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setInt(1, appointmentID);
			s.setInt(2, roomID);
			s.setInt(3,  BuildingID);
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the buildings from the database.
	 *
	 * @return the buildings
	 */
	private static HashMap<Integer, Building> getBuildings(){
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
	
	
	/**
	 * Gets the rooms from the database.
	 *
	 * @return the rooms
	 */
	public static HashMap<Integer, Room> getRooms(){
		String q = "SELECT roomNumber, capacity, roomName, R. description, R.buildingID, B.name FROM Room as R, Building as B WHERE B.BuildingID = R.buildingID;";
		HashMap<Integer, Room> map = new HashMap<Integer, Room>();
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				int key = rs.getInt("roomNumber");
				Room r = new Room(rs.getInt("roomNumber"), rs.getInt("capacity"), rs.getString("roomName"), rs.getString("R.description"), rs.getInt("buildingID"), rs.getString("B.name"));
				map.put(key, r);
			}return map;
		} catch (Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	/**
	 * Gets the room reserved for an appointment.
	 *
	 * @param appointment the appointment
	 * @return the room
	 */
	public static Room getRoom(Appointment  appointment){
		String q = "SELECT roomNumber, capacity, roomName, R. description, R.buildingID, B.name, appid FROM Room as R, RoomReservation, Building as B WHERE B.BuildingID = R.buildingID AND roomid = roomNumber AND appid = '" + appointment.getID() +"';";
		Room room = null;
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				room = new Room(rs.getInt("roomNumber"), rs.getInt("capacity"), rs.getString("roomName"), rs.getString("R.description"), rs.getInt("buildingID"), rs.getString("B.name"));
			}return room;
		} catch (Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	/**
	 * Delete the room with the room ID.
	 *
	 * @param roomID the room ID
	 * @return true, if successful
	 */
	public static boolean deleteRoom(int roomID){
		String q = "delete from Room where roomNumber = " + roomID + ";";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Delete a room reservation, specified with the appointment ID and the room ID.
	 *
	 * @param appointmentID the appointment ID
	 * @param roomID the room ID
	 * @return true, if successful
	 */
	public static boolean deleteRoomReservation(int appointmentID, int roomID){
		String q = "delete from RoomReservation where roomid = " + roomID + " AND appid = " + appointmentID + ";";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Finds available rooms within the specified time frame. 
	 * Also specifies the current appointment, so said appointment is available for reservation during edit.
	 *
	 * @param appointmentDate the appointment date
	 * @param fromTime the time the appointment is scheduled to begin
	 * @param toTime the time the appointment is scheduled to end
	 * @param appointment the appointment
	 * @return a list of available rooms
	 */
	public static List<Room> findRoom(LocalDate appointmentDate, LocalTime fromTime, LocalTime toTime, Appointment appointment){
		String dateString = "" + appointmentDate.getYear() + "-" + appointmentDate.getMonthValue() + "-" + appointmentDate.getDayOfMonth();
		String totimeString = "" + toTime.getHour() + ":" + toTime.getMinute() + ":" + toTime.getSecond();
		String fromtimeString = "" + fromTime.getHour() + ":" + fromTime.getMinute() + ":" + fromTime.getSecond();
		List<Room> available = new ArrayList<Room>();
		String q = "select *\n"+
				"from Room as D\n"+
				"where (D.roomNumber, D.BuildingID) not in (\n"+
				"select R.roomNumber, R.BuildingID\n"+
				"from Room as R, RoomReservation as E, Appointment as A\n"+
				"where  E.appid = A.appointmentID\n"+
				"and E.BuildingID = R.BuildingID\n"+
				"and E.roomid = R.roomNumber\n"+
				"and A.appdate = '"+dateString+"'\n"+
				"and E.appid != '"+ appointment.getID() +"'\n"+
				"and ('"+fromtimeString+"' between A.fromtime and A.totime or '"+totimeString+"' between A.fromtime and A.totime)\n"+ 
				");";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				available.add(new Room(rs.getInt("roomNumber"), rs.getInt("capacity"), rs.getString("roomName"), rs.getString("description"), rs.getInt("buildingID"), DBConnect.getBuildings().get(rs.getInt("BuildingID")).getName()));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return available;
		
	}
	
	/**
	 * Finds available rooms within the specified time frame. 
	 *
	 * @param appointmentDate the appointment date
	 * @param fromTime the time the appointment is scheduled to begin
	 * @param toTime the time the appointment is scheduled to end
	 * @return a list of available rooms
	 */
	public static List<Room> findRoom(LocalDate appointmentDate, LocalTime fromTime, LocalTime toTime){
		String dateString = "" + appointmentDate.getYear() + "-" + appointmentDate.getMonthValue() + "-" + appointmentDate.getDayOfMonth();
		String totimeString = "" + toTime.getHour() + ":" + toTime.getMinute() + ":" + toTime.getSecond();
		String fromtimeString = "" + fromTime.getHour() + ":" + fromTime.getMinute() + ":" + fromTime.getSecond();
		List<Room> available = new ArrayList<Room>();
		String q = "select *\n"+
				"from Room as D\n"+
				"where (D.roomNumber, D.BuildingID) not in (\n"+
				"select R.roomNumber, R.BuildingID\n"+
				"from Room as R, RoomReservation as E, Appointment as A\n"+
				"where  E.appid = A.appointmentID\n"+
				"and E.BuildingID = R.BuildingID\n"+
				"and E.roomid = R.roomNumber\n"+
				"and A.appdate = '"+dateString+"'\n"+
				"and ('"+fromtimeString+"' between A.fromtime and A.totime or '"+totimeString+"' between A.fromtime and A.totime)\n"+ 
				");";
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
			while(rs.next()){
				available.add(new Room(rs.getInt("roomNumber"), rs.getInt("capacity"), rs.getString("roomName"), rs.getString("description"), rs.getInt("buildingID"), DBConnect.getBuildings().get(rs.getInt("BuildingID")).getName()));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return available;
		
	}

	/**
	 * Gets an ArrayList of AppointmentAndEmployee relations.
	 *
	 * @return an ArrayList of AppointmentAndEmployee
	 */
	public static ArrayList<AppointmentAndEmployee> getAppointmentAndEmployee() {
		String query = "SELECT * FROM AppointmentAndEmployee";
		ArrayList<AppointmentAndEmployee> ae = new ArrayList<AppointmentAndEmployee>();
		try{
			PreparedStatement e = getConnection().prepareStatement(query);
			ResultSet rs = (ResultSet) e.executeQuery();
			while(rs.next()){
				ae.add(new AppointmentAndEmployee(rs.getInt("appid"), rs.getInt("employeeid"), rs.getInt("status"), rs.getInt("alarm"), rs.getString("farge")));

			} 
		}catch (SQLException e){
			e.printStackTrace();
		}
		return ae;
	}

	/**
	 * Gets one specific AppointmentAndEmployee relation, according to the appointment and the employee.
	 *
	 * @param appointment the appointment
	 * @param employee the employee
	 * @return an AppointmentAndEmployee object.
	 */
	public static AppointmentAndEmployee getAppointmentAndEmployee(Appointment appointment, Employee employee) {
		String query = "SELECT * FROM AppointmentAndEmployee WHERE appid = " + appointment.getID() + " AND employeeid = " + employee.getID() + ";";
		try{
			PreparedStatement e = getConnection().prepareStatement(query);
			ResultSet rs = (ResultSet) e.executeQuery();
			while(rs.next()){
				return new AppointmentAndEmployee(rs.getInt("appid"), rs.getInt("employeeid"), rs.getInt("status"), rs.getInt("alarm"), rs.getString("farge"));
			} 
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the status of an invitation for the appointment to the employee.
	 *
	 * @param appointmentID the appointment ID
	 * @param employeeID the employee ID
	 * @return the status
	 */
	public static int getStatus(int appointmentID, int employeeID){
		String query = "SELECT appid, employeeid, status FROM AppointmentAndEmployee WHERE employeeid = '" + employeeID + "' AND appid = '" + appointmentID + "';";
		int state = -1;
		try{
			PreparedStatement e = getConnection().prepareStatement(query);
			ResultSet rs = (ResultSet) e.executeQuery();
			while(rs.next()){
				state = rs.getInt("status");
			}	
		} catch (SQLException e){
			e.printStackTrace();
		}
		return state;
	}
	
	/**
	 * Adds an appointment to the database.
	 *
	 * @param title the title of the appointment
	 * @param description the description of the appointment
	 * @param date the date the appointment is scheduled for
	 * @param fromTime the time the appointment will begin
	 * @param toTime the time the appointment will end
	 * @param owner the owner of the appointment
	 * @return the key to the appointment
	 */
	public static int addAppointment(String title, String description, Date date, Time fromTime, Time toTime, Employee owner) {
		String query = "INSERT INTO Appointment (title, description, appdate, fromtime, totime, ownerid) VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement e = getConnection().prepareStatement(query);
			e.setString(1, title);
			e.setString(2, description);
			e.setDate(3, date);
			e.setTime(4, fromTime);
			e.setTime(5, toTime);
			e.setInt(6, owner.getID());
			e.execute();
			ResultSet rs = e.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Gets all the appointments in the database.
	 *
	 * @return a HashMap of the appointments
	 */
	private static HashMap<Integer, Appointment> getAppointments() {
		String q = "SELECT appointmentID, title, description, appdate, totime, fromtime, ownerid FROM Appointment ";
		HashMap<Integer, Appointment> appointments = new HashMap<Integer, Appointment>();
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
				while(rs.next()) {
					int key = rs.getInt("appointmentID");
					Appointment a = new Appointment(Integer.parseInt(rs.getString("appointmentID")), rs.getString("title"), rs.getString("description"), LocalDate.parse(rs.getString("appdate")), LocalTime.parse(rs.getString("totime")), LocalTime.parse(rs.getString("fromtime")), Integer.parseInt(rs.getString("ownerid")));
					appointments.put(key, a);
				}
				return appointments;
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	/**
	 * Gets a connection to the database, by using DBLogin.
	 *
	 * @return the connection
	 */
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
	
	/**
	 * Edits a specific appointment, indicated by the appointment ID.
	 *
	 * @param appointmentID the appointment ID of the appointment to be edited
	 * @param title the title to be edited
	 * @param description the description to be edited
	 * @param appointmentDate the date to be edited
	 * @param toTime the time the appointment begins to be edited
	 * @param fromTime the time the appointment ends to be edited
	 * @return true, if successful
	 */
	public static boolean editAppointment(int appointmentID, String title, String description, Date appointmentDate, Time toTime, Time fromTime){
		String query = "UPDATE Appointment SET title='"+title+"',description='"+description+"',appdate='"+appointmentDate+"',totime='"+toTime+"',fromtime='"+fromTime+"' WHERE appointmentID='"+appointmentID+"';";
		try{
			PreparedStatement s = getConnection().prepareStatement(query);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * Delete an employee from the database.
	 *
	 * @param employeeID the employee ID of the employee to be deleted
	 * @return true, if successful
	 */
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
	
	/**
	 * Delete appointment from the database.
	 *
	 * @param appointmentID the appointment ID of the appointment to be deleted
	 * @return true, if successful
	 */
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
	
	/**
	 * Delete an AppointmentAndEmployee relation from the database.
	 *
	 * @param appointmentID the appointment ID
	 * @param employeeID the employee ID
	 * @return true, if successful
	 */
	public static boolean deleteAppointmentAndEmployee(int appointmentID, int employeeID) {
		String q = "delete from AppointmentAndEmployee where appid = " + appointmentID + " AND employeeid = " + employeeID + ";";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Invite an employee to an appointment.
	 *
	 * @param employee the employee to be invited
	 * @param appointmentID the appointment ID of the appointment the employee is invited to
	 * @return true, if successful
	 */
	public static boolean inviteEmployee(Employee employee, int appointmentID) {
		int status = 0;
		if(employee.getID() == Login.getCurrentUserID()){
			status = 1;
		}
		String q = "insert into AppointmentAndEmployee (appid, employeeid, status, alarm, farge) values (?, ?, ?, ?, ?);";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			s.setInt(1, appointmentID);
			s.setInt(2, employee.getID());
			s.setInt(3, status);
			s.setInt(4, 1);
			s.setString(5, "BLUE");
			s.execute();
			return true;
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sets the owner of an appointment in the database.
	 *
	 * @param employee the employee to be set as owner
	 * @param appointmentID the appointment ID
	 */
	public static void setOwnerOfAppointment(Employee employee, int appointmentID) {
		String q = "insert into AppointmentAndEmployee (appid, employeeid, status, alarm, farge) values (?, ?, ?, ?, ?);";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			s.setInt(1, appointmentID);
			s.setInt(2, employee.getID());
			s.setInt(3, 1);
			s.setInt(4, 0);
			s.setString(5, "BLUE");
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the color of an AppointmentAndEmployee relation as a string in the database.
	 *
	 * @param employee the employee
	 * @param appointmentID the appointment ID
	 * @param color the color for the appointment
	 * @return true, if successful
	 */
	public static boolean setColorOfAppointment(Employee employee, int appointmentID, String color){
		String query = "UPDATE AppointmentAndEmployee SET farge='"+color+"' WHERE appID='"+appointmentID+"' AND employeeid='"+employee.getID()+"';";
		try{
			PreparedStatement s = getConnection().prepareStatement(query);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the appointments of a specific group.
	 *
	 * @param group the group
	 * @return an ArrayList of the appointments for the specifed group
	 */
	public static ArrayList<Appointment> getGroupApp(Group group) {
		HashMap<Integer, Appointment> appointments = getAppointments();
		HashMap<Integer, ArrayList<Appointment>> groupAppointments = new HashMap<Integer, ArrayList<Appointment>>();
		try {
			String q = "select distinct GM.groupID, A.appointmentID\n"+
					"from Appointment as A, AppointmentAndEmployee as AAE, Employee as E, GroupMember as GM\n"+
					"where AAE.appid = A.appointmentID\n"+
					"and AAE.employeeid = E.employeeid\n"+
					"and GM.employeeid = E.employeeid;\n";
			PreparedStatement p = getConnection().prepareStatement(q);
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("GM.groupID");
				if(!groupAppointments.containsKey(id)) {
					groupAppointments.put(id, new ArrayList<Appointment>());
				}
				Appointment app = appointments.get(rs.getInt("A.appointmentID"));
				groupAppointments.get(id).add(app);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ArrayList<Appointment> hello = new ArrayList<Appointment>();
		for(Appointment p : groupAppointments.get(group.id)) {
			ArrayList<Employee> ejeje = getEmployeesFromAppointment(p);
			if(ListOperations.contains(ejeje, group.getMembers()) && ListOperations.contains(group.getMembers(), ejeje)) {
				hello.add(p);
			}
		}
		
		return hello;
	}
	
	/**
	 * Gets the active appointments of an employee.
	 *
	 * @param employee the employee
	 * @return an ArrayList of the active appointments of an employee
	 */
	public static ArrayList<Appointment> getActiveAppointmentsFromEmployee(Employee employee) {
		HashMap<Integer, Appointment> appointments = getAppointments();
		String query = "select AAE.appid, AAE.employeeid from AppointmentAndEmployee as "
				+ "AAE where AAE.employeeid = " + String.valueOf(employee.getID()) + " and AAE.status = 1;";
		ArrayList<Appointment> app = new ArrayList<Appointment>();
		try{
			PreparedStatement we = getConnection().prepareStatement(query);
			ResultSet rs = (ResultSet) we.executeQuery();
			while(rs.next()){
				if(appointments.containsKey(rs.getInt("AAE.appid"))){					
					app.add(appointments.get(rs.getInt("AAE.appid")));
				}
			}
			return app;
		}catch (SQLException wae){
			wae.printStackTrace();
		}
		return app;
	}
	
	/**
	 * Gets all the employees connected to an appointment.
	 *
	 * @param appointment the appointment
	 * @return an ArrayList of the employees
	 */
	private static ArrayList<Employee> getEmployeesFromAppointment(Appointment appointment) {
		HashMap<Integer, Employee> employees = getEmployees();
		String query = "select AAE.appid, AAE.employeeid from AppointmentAndEmployee as "
				+ "AAE where AAE.appid = " + String.valueOf(appointment.getID()) +";";
		ArrayList<Employee> app = new ArrayList<Employee>();
		try{
			PreparedStatement we = getConnection().prepareStatement(query);
			ResultSet rs = (ResultSet) we.executeQuery();
			while(rs.next()){
				app.add(employees.get(rs.getInt("AAE.employeeid")));
			}
			return app;
		}catch (SQLException wae){
			wae.printStackTrace();
		}
		return app;
	}
	
}
