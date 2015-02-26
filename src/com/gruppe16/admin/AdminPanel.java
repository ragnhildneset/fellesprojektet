package com.gruppe16.admin;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.gruppe16.database.DBConnect;

public class AdminPanel extends Application implements Initializable {

	public static void main(String[] args) {
		launch(args);
	}

	@FXML Button e_add;
	@FXML TextField e_id;
	@FXML TextField e_fname;
	@FXML TextField e_lname;
	@FXML TextField e_mail;
	@FXML TextField e_user;
	@FXML PasswordField e_pass;
	
	@FXML Button r_add;
	@FXML TextField r_id;
	@FXML TextField r_name;
	@FXML TextField r_bid;
	@FXML TextArea r_desc;
	@FXML TextField r_cap;

	@FXML Button b_add;
	@FXML TextField b_id;
	@FXML TextField b_name;
	@FXML TextArea b_desc;
	@FXML TextField b_lat;
	@FXML TextField b_long;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		e_add.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try{
					int _e_id = Integer.parseInt(e_id.getText());
					String _e_fname = e_fname.getText();
					String _e_lname = e_lname.getText();
					String _e_mail = e_mail.getText();
					String _e_user = e_user.getText();
					String _e_pass = e_pass.getText();
					String e_query = "insert into Employee(employeeid, givenName, surname, email) VALUES ( ?, ?, ?, ? )";
					PreparedStatement e = DBConnect.getConnection().prepareStatement(e_query);
					e.setInt(1, _e_id);
					e.setString(2, _e_fname);
					e.setString(3, _e_lname);
					e.setString(4, _e_mail);
					e.execute();
					String u_query = "insert into UserAndID(employeeid, username, password) VALUES ( ?, ?, ? )";
					e = DBConnect.getConnection().prepareStatement(u_query);
					e.setInt(1, _e_id);
					e.setString(2, _e_user);
					e.setString(3, _e_pass);
					e.execute();
					System.out.println("Added new user and employee.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		r_add.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try{
					int _r_id = Integer.parseInt(r_id.getText());
					String _r_name = r_name.getText();
					String _r_desc = r_desc.getText();
					int _r_bin = Integer.parseInt(r_bid.getText());
					int _r_cap = Integer.parseInt(r_cap.getText());
					String r_query = "insert into Room(roomNumber, buildingID, capacity, roomName, description) VALUES ( ?, ?, ?, ?, ? )";
					PreparedStatement e = DBConnect.getConnection().prepareStatement(r_query);
					e.setInt(1, _r_id);
					e.setInt(2, _r_bin);
					e.setInt(3, _r_cap);
					e.setString(4, _r_name);
					e.setString(5, _r_desc);
					e.execute();
					System.out.println("Added new room.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		b_add.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try{
					int _b_id = Integer.parseInt(b_id.getText());
					String _b_name = b_name.getText();
					String _b_desc = b_desc.getText();
					float _b_lat = Float.parseFloat(b_lat.getText());
					float _b_long = Float.parseFloat(b_long.getText());
					String b_query = "insert into Building(buildingID, latitude, longitude, name, description) VALUES ( ?, ?, ?, ?, ? )";
					PreparedStatement e = DBConnect.getConnection().prepareStatement(b_query);
					e.setInt(1, _b_id);
					e.setFloat(2, _b_lat);
					e.setFloat(3, _b_long);
					e.setString(4, _b_name);
					e.setString(5, _b_desc);
					e.execute();
					System.out.println("Added new building.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public void start(Stage arg0) throws Exception {
		try{
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/gruppe16/admin/mainPane.fxml")));
			arg0.setScene(scene);
			arg0.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
