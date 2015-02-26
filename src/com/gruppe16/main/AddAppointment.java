package com.gruppe16.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.Employee;

public class AddAppointment extends Application implements Initializable {
	
	@FXML
	private Button sendid;
	@FXML
	private TextField tilid;
	@FXML
	private TextField room;
	@FXML
	private TextField fraid;
	@FXML
	private TextField formaal;
	@FXML
	private DatePicker dateid;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		sendid.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try{
					Appointment.addNew(formaal.getText(), room.getText(), dateid.getValue(), 
							fraid.getText() + ":00", tilid.getText() + ":00");
					stage.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static Stage stage;
	
	@Override
	public void start(Stage arg0) throws Exception {
		stage = arg0;
		AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/com/gruppe16/main/AddAppointment.fxml"));
		Scene scene = new Scene(root, 600, 509);
		arg0.setScene(scene);
		arg0.show();
	}
	static int Owner_EID;
	public static void main(String[] args){
		Employee.initialize();
		if(!Login.login("abc", "def")){
			System.exit(2);
		}
		launch(args);
	}

}
