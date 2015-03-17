package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Employee.Group;

public class EmployeePicker implements Initializable {
	@FXML
	private Button OKBtn;
	
	@FXML
	private Button addBtn;
	
	@FXML
	private Button addAllBtn;
	
	@FXML
	private Button removeBtn;
	
	@FXML
	private Button removeAllBtn;
	
	@FXML
	private ChoiceBox<String> e_group;
	
	@FXML
	private TextField givenNameTextField;
	
	@FXML
	private TextField surNameTextField;
	
	@FXML
	private ListView<Employee> employeeListView;
	
	@FXML
	private ListView<Employee> attendingListView;

	private Collection<Employee> availableEmployees = DBConnect.getEmployeeList();
	private static Stage stage;
	private static AddAppointment addAppointment;
	private static ArrayList<Employee> defaultAttendees;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ArrayList<String> g = new ArrayList<String>();
		g.add("All");
		for(Group v : Employee.getGroups()){
			g.add(v.name);
		}
		
		e_group.setItems(FXCollections.observableArrayList(g));
		e_group.getSelectionModel().selectFirst();
		e_group.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if((Integer) arg2 == 0){
					availableEmployees = DBConnect.getEmployeeList();
				} else {					
					String f = e_group.getItems().get((Integer) arg2);
					Group g = Employee.getFromName(f);
					availableEmployees = g.getMembers();
				}
				updateEmployeeList();
			}
		});
		
		attendingListView.getItems().addAll(defaultAttendees);
		
		OKBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addAppointment.setAttendees(attendingListView.getItems());
				stage.close();
			}
		});
		
		givenNameTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				updateEmployeeList();
			}
		});
		
		surNameTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				updateEmployeeList();
			}
		});
		
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				addSelectedEmployee();
			}
		});
		
		addAllBtn.setOnAction(event -> {
			for(Employee e : employeeListView.getItems()) {
				attendingListView.getItems().add(e);
			}
			employeeListView.getItems().clear();
		});
		
		removeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				removeSelectedEmployee();
			}
		});
		
		removeAllBtn.setOnAction(event -> {
			attendingListView.getItems().clear();
			updateEmployeeList();
		});
		
		employeeListView.setOnMouseClicked(event -> {
			if(event.getClickCount() == 2) {
				addSelectedEmployee();
			}
		});
		
		attendingListView.setOnMouseClicked(event -> {
			if(event.getClickCount() == 2) {
				removeSelectedEmployee();
			}
		});
		
		updateEmployeeList();
	}
	
	private void addSelectedEmployee() {
		Employee e = employeeListView.getSelectionModel().getSelectedItem();
		if(e != null) {
			attendingListView.getItems().add(e);
			employeeListView.getItems().remove(e);
		}
	}
	
	private void removeSelectedEmployee() {
		Employee e = attendingListView.getSelectionModel().getSelectedItem();
		if(e != null) {
			attendingListView.getItems().remove(e);
			employeeListView.getItems().add(e);
		}
	}
	
	private void updateEmployeeList() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		for(Employee employee : availableEmployees) {
			if(employee.getFirstName().toLowerCase().contains(givenNameTextField.getText().toLowerCase()) && employee.getLastName().toLowerCase().contains(surNameTextField.getText().toLowerCase()) &&
					!attendingListView.getItems().contains(employee) && !employee.equals(Login.getCurrentUser())) {
				employees.add(employee);
			}
		}
		employeeListView.setItems(FXCollections.observableArrayList(employees));
		employeeListView.getSelectionModel().select(0);
	}
	
	public static void start(Stage stage, Window owner, AddAppointment addApp, ArrayList<Employee> attendees) throws IOException {
		EmployeePicker.stage = stage;
		EmployeePicker.addAppointment = addApp;
		EmployeePicker.defaultAttendees = attendees;
		//EmployeePicker.currentAttendees = addApp.getAttendees();
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
