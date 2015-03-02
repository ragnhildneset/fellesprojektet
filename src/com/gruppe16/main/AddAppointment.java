package com.gruppe16.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.Employee;

public class AddAppointment extends Application implements Initializable {
	@FXML
	private Button sendBtn;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button searchForRoomBtn;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private TextArea descriptionTextArea;
	
	@FXML
	private TextField attendeesTextField;

	@FXML
	private TextField fromTextField;
	
	@FXML
	private TextField toTextField;
	
	@FXML
	private TextField roomTextField;

	@FXML
	private TextField titleTextField;

	private static Stage stage;
	
	private Window owner;
	
	public AddAppointment() {
		this.owner = null;
	}
	
	public AddAppointment(Window owner) {
		this.owner = owner;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sendBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				try{
					//Appointment.addNew(formaal.getText(), description.getText(), dateid.getValue(), 
					//		fraid.getText() + ":00", tilid.getText() + ":00");
					stage.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		AddAppointment.stage = stage;
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("/com/gruppe16/main/AddAppointment.fxml"));
		Scene scene = new Scene(root);
		stage.setResizable(false);
		stage.initStyle(StageStyle.UTILITY);
		if(owner != null) {
			stage.initOwner(owner);
			stage.initModality(Modality.WINDOW_MODAL);
		}
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
