package com.gruppe16.main;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

import com.gruppe16.database.DBConnect;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Notification extends Application implements Initializable {

	@FXML Button c_attend;
	@FXML Button c_decline;
	
	@FXML Label c_title;
	@FXML Label c_desc;
	@FXML Label c_to;
	@FXML Label c_from;
	@FXML Label c_date;
	@FXML Label c_status;
	
	private Notif current = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		current = notifications.poll();
		c_title.setText(current.title);
		c_desc.setText(current.desc);
		c_to.setText(current.to);
		c_from.setText(current.from);
		c_date.setText(current.date);
		c_status.setText(current.owner);
		
		System.out.println(current);
		
		c_attend.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String q = "update Request\n"
						+"set status = 1\n"
						+"where employeeid = "+Login.getCurrentUserID()+";";
				try{
					PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
					p.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Platform.exit();
			}
		});
		c_decline.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String q = "update Request\n"
						+"set status = 2\n"
						+"where employeeid = "+Login.getCurrentUserID()+";";
				try{
					PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
					p.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Platform.exit();
			}
		});
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Login.login("admin", "passord");
		getInvites(Login.getCurrentUserID());
		Pane pane = (Pane) FXMLLoader.load(getClass().getResource("/com/gruppe16/main/notif_cell.fxml"));
		Scene scene = new Scene(pane);
		arg0.initStyle(StageStyle.UNDECORATED);
		arg0.setScene(scene);
		arg0.show();
	}
	
	static Queue<Notif> notifications = new LinkedList<Notif>();
	
	public static void getInvites(int key){
		String q = "select A.Title, A.description, A.totime, A.fromtime, A.appdate, E.givenName, E.surname, R.status, R.alarm\n"
				+ "from Request as R, Appointment as A, Employee as E\n"
				+ "where R.employeeid = "+key+" and A.appointmentID = R.appid and R.status = 0 and E.employeeid = A.ownerid;";
		System.out.println(q);
		
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
						rs.getString("E.surname") + ", " + rs.getString("E.givenName")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Notif getNext(){
		if(!notifications.isEmpty())
			return notifications.poll();
		return null;
	}

	public static void main(String[] args){
		launch(args);
	}
	
	public static class Notif {
		String title, desc, to, from, date, alarm, owner;

		public Notif(String title, String desc, String to, String from,
				String date, String alarm, String owner) {
			super();
			this.title = title;
			this.desc = desc;
			this.to = to;
			this.from = from;
			this.date = date;
			this.alarm = alarm;
			this.owner = owner;
		}

		@Override
		public String toString() {
			return "Notif [title=" + title + ", desc=" + desc + ", to=" + to
					+ ", from=" + from + ", date=" + date + ", alarm=" + alarm
					+ ", owner=" + owner + "]";
		}
		
	}
	
}
