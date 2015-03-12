package com.gruppe16.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gruppe16.database.DBConnect;
import com.gruppe16.main.Login;

public class Notif {

	public String title, desc, to, from, date, alarm, owner;
	public int appid, status;
	
	public final static int WAITING = 0, ACCEPTED = 1, DECLINED = 2;

	public Notif(String title, String desc, String to, String from,
			String date, String alarm, String owner, int appid, int status) {
		super();
		this.title = title;
		this.desc = desc;
		this.to = to;
		this.from = from;
		this.date = date;
		this.alarm = alarm;
		this.owner = owner;
		this.appid = appid;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Notif [title=" + title + ", desc=" + desc + ", to=" + to
				+ ", from=" + from + ", date=" + date + ", alarm=" + alarm
				+ ", owner=" + owner + "]";
	}
	
	public boolean accept(){
		try{
			String q = "update AppointmentAndEmployee\nset status = 1 where appid = " +appid+" and employeeid="+Login.getCurrentUserID();
			PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
			p.execute();
			status = ACCEPTED;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean decline(){
		try{
			String q = "update AppointmentAndEmployee\nset status = 2 where appid = " +appid+" and employeeid="+Login.getCurrentUserID();
			PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
			p.execute();
			status = DECLINED;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
		
}
