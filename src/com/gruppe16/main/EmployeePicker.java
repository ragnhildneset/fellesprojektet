package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class EmployeePicker implements Initializable {
	@FXML
	private Button OKBtn;
	
	@FXML
	private Button addBtn;
	
	@FXML
	private Button removeBtn;
	
	@FXML
	private TextField givenNameTextField;
	
	@FXML
	private TextField sirNameTextField;
	
	@FXML
	private ListView<Employee> employeeListView;
	
	@FXML
	private ListView<Employee> attendingListView;
	
	private static Stage stage;
	private static AddAppointment addAppointment;
	private static Collection<Employee> currentAttendees;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		attendingListView.setItems(FXCollections.observableArrayList(currentAttendees));
		
		OKBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addAppointment.setAttendees(attendingListView.getItems());
				stage.close();
			}
		});
		
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Employee e = employeeListView.getSelectionModel().getSelectedItem();
				if(e != null) {
					attendingListView.getItems().add(e);
					employeeListView.getItems().remove(e);
				}
			}
		});
		
		removeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Employee e = attendingListView.getSelectionModel().getSelectedItem();
				if(e != null) {
					attendingListView.getItems().remove(e);
					employeeListView.getItems().add(e);
				}
			}
		});
		
		givenNameTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				updateEmployeeList();
			}
		});
		
		sirNameTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				updateEmployeeList();
			}
		});
		
		updateEmployeeList();
	}
	
	private void updateEmployeeList() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		for(Employee e : AddAppointment.cachedEmployees) {
			if(e.getFirstName().toLowerCase().contains(givenNameTextField.getText().toLowerCase()) && e.getLastName().toLowerCase().contains(sirNameTextField.getText().toLowerCase()) &&
					!attendingListView.getItems().contains(e)) {
				employees.add(e);
			}
		}
		employeeListView.setItems(FXCollections.observableArrayList(employees));
		employeeListView.getSelectionModel().select(0);
	}
	
	public static void start(Stage stage, Window owner, AddAppointment addApp) throws IOException {
		EmployeePicker.stage = stage;
		EmployeePicker.addAppointment = addApp;
		EmployeePicker.currentAttendees = addApp.getAttendees();
		Scene scene = new Scene((Parent)FXMLLoader.load(EmployeePicker.class.getResource("/com/gruppe16/main/EmployeePicker.fxml")));
		stage.setResizable(false);
		stage.initStyle(StageStyle.UTILITY);
		if(owner != null) {
			stage.initOwner(owner);
			stage.initModality(Modality.WINDOW_MODAL);
		}
		stage.setScene(scene);
		stage.show();
	}
}
