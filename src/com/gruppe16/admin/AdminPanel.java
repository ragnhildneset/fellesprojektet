package com.gruppe16.admin;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Room;

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
	
    @FXML private TableView<Room> roomlistTable;
    @FXML private TableColumn<Room, String> roomIDCol;
    @FXML private TableColumn<Room, String> capacityCol;
    @FXML private TableColumn<Room, String> roomnameCol;
    @FXML private TableColumn<Room, String> roomdescrCol;
    @FXML private TableColumn<Room, String> roombuildingidCol;
  
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Room> data = FXCollections.observableArrayList(DBConnect.getRooms().values());
		roomIDCol.setCellValueFactory(new PropertyValueFactory<Room, String>("roomid"));
		capacityCol.setCellValueFactory(new PropertyValueFactory<Room, String>("capacity"));
		roomnameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
		roomdescrCol.setCellValueFactory(new PropertyValueFactory<RoAom, String>("description"));
		roombuildingidCol.setCellValueFactory(new PropertyValueFactory<Room, String>("buildingid"));

		roomlistTable.setItems(data);
		
		
		
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
					AdminPanel.addUser(_e_id, _e_fname, _e_lname, _e_mail, _e_user, _e_pass);
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
					AdminPanel.addRoom(_r_id, _r_bin, _r_cap, _r_name, _r_desc);
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
					AdminPanel.addBuilding(_b_id, _b_lat, _b_long, _b_name, _b_desc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public void start(Stage arg0) throws Exception {
		try{
			Scene scene = new Scene( (Parent) FXMLLoader.load(getClass().getResource("/com/gruppe16/admin/mainPane.fxml")));
			arg0.setScene(scene);
			arg0.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addUser(int _e_id, String _e_fname, String _e_lname, String _e_mail, String _e_user, String _e_pass){
		try{
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addRoom(int _r_id, int _r_bin, int _r_cap, String _r_name, String _r_desc){
		String r_query = "insert into Room(roomNumber, buildingID, capacity, roomName, description) VALUES ( ?, ?, ?, ?, ? )";
		PreparedStatement e;
		try {
			e = DBConnect.getConnection().prepareStatement(r_query);
			e.setInt(1, _r_id);
			e.setInt(2, _r_bin);
			e.setInt(3, _r_cap);
			e.setString(4, _r_name);
			e.setString(5, _r_desc);
			e.execute();
			System.out.println("Added new room.");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}

	public static void addBuilding(int _b_id, float _b_lat, float _b_long, String _b_name, String _b_desc){
		try{
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
	
	public static void fillRoomTable(){
		
	}
	
}
