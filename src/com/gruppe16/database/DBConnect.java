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

public class DBConnect {
	
	private static Connection con = null;

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
	
	private static HashMap<Integer, Employee> _employees = null;
	public static ArrayList<Employee> getEmployees(){
		if(_employees != null){
			return (ArrayList<Employee>) ListOperations.hashToList(_employees);
		}
		String q = "SELECT E.employeeid, E.givenName, E.surname, E.email, U.username FROM Employee as E, UserAndID as U WHERE U.employeeid = E.employeeid;";
		_employees = new HashMap<Integer, Employee>();
		try{
			PreparedStatement p = getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) p.executeQuery();
			while(rs.next()){
				int key = rs.getInt("E.employeeid");
				Employee e = new Employee(key,rs.getString("E.givenName"), rs.getString("E.surname"), rs.getString("E.email"), rs.getString("username"));
				_employees.put(key, e);
			}return (ArrayList<Employee>) ListOperations.hashToList(_employees);
		}catch(Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	public static void addRoomReservation(int appid, int roomid, int BuildingID){
		String q = "INSERT INTO RoomReservation(appid, roomid, BuildingID) VALUES (?, ?, ?)";
		try {
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			s.setInt(1, appid);
			s.setInt(2, roomid);
			s.setInt(3,  BuildingID);
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	public static List<Room> findRoom(LocalDate appdate, LocalTime fromtime, LocalTime totime, Appointment app){
		String dateString = "" + appdate.getYear() + "-" + appdate.getMonthValue() + "-" + appdate.getDayOfMonth();
		String totimeString = "" + totime.getHour() + ":" + totime.getMinute() + ":" + totime.getSecond();
		String fromtimeString = "" + fromtime.getHour() + ":" + fromtime.getMinute() + ":" + fromtime.getSecond();
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
				"and E.appid != '"+ app.getID() +"'\n"+
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
	
	public static List<Room> findRoom(LocalDate appdate, LocalTime fromtime, LocalTime totime){
		String dateString = "" + appdate.getYear() + "-" + appdate.getMonthValue() + "-" + appdate.getDayOfMonth();
		String totimeString = "" + totime.getHour() + ":" + totime.getMinute() + ":" + totime.getSecond();
		String fromtimeString = "" + fromtime.getHour() + ":" + fromtime.getMinute() + ":" + fromtime.getSecond();
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
	

	public static ArrayList<AppointmentAndEmployee> getAppointmentAndEmployee(){
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
	
	public static Employee getEmployeeFromID(int id){
		String query = "SELECT * FROM Employee JOIN UserAndID ON(Employee.employeeid = UserAndID.employeeid) WHERE Employee.employeeid = '" + id + "'";
		Employee employee = null;
		try{
			PreparedStatement e = getConnection().prepareStatement(query);
			ResultSet rs = (ResultSet) e.executeQuery();
			while(rs.next()){
				employee = new Employee(rs.getInt("employeeid"), rs.getString("givenName"), rs.getString("surname"), rs.getString("email"), rs.getString("username"));
			}	
		} catch (SQLException e){
			e.printStackTrace();
		}
		return employee;
	}
	
	public static int addAppointment(String title, String description, Date date, Time fromTime, Time toTime, Employee host) {
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
			ResultSet rs = e.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private static HashMap<Integer, Appointment> appointments = null;
	public static HashMap<Integer, Appointment> getAppointments(){
		if(appointments != null){
			return appointments;
		}
		String q = "SELECT appointmentID, title, description, appdate, totime, fromtime, ownerid FROM Appointment ";
		appointments = new HashMap<Integer, Appointment>();
		try{
			PreparedStatement s = DBConnect.getConnection().prepareStatement(q);
			ResultSet rs = (ResultSet) s.executeQuery();
				while(rs.next()){
					int key = rs.getInt("appointmentID");
					Appointment a = new Appointment(Integer.parseInt(rs.getString("appointmentID")), rs.getString("title"), rs.getString("description"), LocalDate.parse(rs.getString("appdate")), LocalTime.parse(rs.getString("totime")), LocalTime.parse(rs.getString("fromtime")), Integer.parseInt(rs.getString("ownerid")), LocalTime.now());
					appointments.put(key, a);
				}return appointments;
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
	
	public static boolean editAppointment(int appointmentid, String title, String description, Date appdate, Time totime, Time fromtime){
		String query = "UPDATE Appointment SET title='"+title+"',description='"+description+"',appdate='"+appdate+"',totime='"+totime+"',fromtime='"+fromtime+"' WHERE appointmentID='"+appointmentid+"';";
		try{
			PreparedStatement s = getConnection().prepareStatement(query);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
	
	public static boolean deleteAppointmentAndEmployee(int appID, int empID) {
		String q = "delete from AppointmentAndEmployee where appid = " + appID + " AND employeeid = " + empID + ";";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean inviteEmployee(Employee employee, int appid) {
		int status = 0;
		if(employee.getEmployeeID() == Login.getCurrentUserID()){
			status = 1;
		}
		String q = "insert into AppointmentAndEmployee (appid, employeeid, status, alarm, farge) values (?, ?, ?, ?, ?);";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			s.setInt(1, appid);
			s.setInt(2, employee.getEmployeeID());
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
	
	public static void setOwnerOfAppointment(Employee employee, int appid) {
		String q = "insert into AppointmentAndEmployee (appid, employeeid, status, alarm, farge) values (?, ?, ?, ?, ?);";
		try{
			PreparedStatement s = getConnection().prepareStatement(q);
			s.setInt(1, appid);
			s.setInt(2, employee.getEmployeeID());
			s.setInt(3, 1);
			s.setInt(4, 0);
			s.setString(5, "BLUE");
			s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean setColorOfAppointment(Employee employee, int appid, String color){
		String query = "UPDATE AppointmentAndEmployee SET farge='"+color+"' WHERE appID='"+appid+"' AND employeeid='"+employee.getEmployeeID()+"';";
		try{
			PreparedStatement s = getConnection().prepareStatement(query);
			return s.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static HashMap<Integer, ArrayList<Appointment>> groupApp = null;
	public static ArrayList<Appointment> getGroupApp(Group group){
		getAppointments();
//		if(groupApp!=null){
//			return groupApp.get(group.id);
//		}
		groupApp = new HashMap<Integer, ArrayList<Appointment>>();
		try{
			String q = "select distinct GM.groupID, A.appointmentID\n"+
					"from Appointment as A, AppointmentAndEmployee as AAE, Employee as E, GroupMember as GM\n"+
					"where AAE.appid = A.appointmentID\n"+
					"and AAE.employeeid = E.employeeid\n"+
					"and GM.employeeid = E.employeeid;\n";
			PreparedStatement p = getConnection().prepareStatement(q);
			ResultSet rs = p.executeQuery();
			while(rs.next()){
				int g_id = rs.getInt("GM.groupID");
				if(!groupApp.containsKey(g_id)){
					groupApp.put(g_id, new ArrayList<Appointment>());
				}
				Appointment app = appointments.get(rs.getInt("A.appointmentID"));
				groupApp.get(g_id).add(app);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<Appointment> hello = new ArrayList<Appointment>();
		for(Appointment p : groupApp.get(group.id)){
			ArrayList<Employee> ejeje = getEmployeesFromAppointment(p);
			if(ListOperations.contains(ejeje, group.getMembers())
					&& ListOperations.contains(group.getMembers(), ejeje)){
				hello.add(p);
				System.out.println(p);
			}
		}
		return hello;
	}
	
	public static ArrayList<Appointment> getActiveAppointmentsFromEmployee(Employee e) {
		getAppointments();
		String query = "select AAE.appid, AAE.employeeid from AppointmentAndEmployee as "
				+ "AAE where AAE.employeeid = " + String.valueOf(e.getEmployeeID()) + " and AAE.status = 1;";
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
	
	public static ArrayList<Employee> getEmployeesFromAppointment(Appointment e) {
		getEmployees();
		String query = "select AAE.appid, AAE.employeeid from AppointmentAndEmployee as "
				+ "AAE where AAE.appid = " + String.valueOf(e.getID()) +";";
		ArrayList<Employee> app = new ArrayList<Employee>();
		try{
			PreparedStatement we = getConnection().prepareStatement(query);
			ResultSet rs = (ResultSet) we.executeQuery();
			while(rs.next()){
				app.add(_employees.get(rs.getInt("AAE.employeeid")));
			}
			return app;
		}catch (SQLException wae){
			wae.printStackTrace();
		}
		return app;
	}
	
	public static void update(){
		_employees.clear();
		appointments.clear();
		groupApp.clear();
		notifs.clear();
	}
	
//	public static void close(){
//		if(con != null) DBConnect.close();
//	}
}
