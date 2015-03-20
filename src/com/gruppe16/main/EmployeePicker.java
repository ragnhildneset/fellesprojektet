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

// TODO: Auto-generated Javadoc
/**
 * The Class EmployeePicker. A GUI for picking employees to invite to an appointment.
 *
 * @author Gruppe 16
 */
public class EmployeePicker implements Initializable {
	
	/** The OK button. Closes the window and adds the currently selected employees to the AddAppointment window.*/
	@FXML
	private Button OKBtn;
	
	/** The add button. Adds the selected employee from the employees list to the attending employees list. */
	@FXML
	private Button addBtn;
	
	/** The add all button. Adds all of the currently displayed employees in the employees list to the attending employees list. */
	@FXML
	private Button addAllBtn;
	
	/** The remove button. Removes the selected employee from the attending employees list.*/
	@FXML
	private Button removeBtn;
	
	/** The remove all button. Removes all the employees from the attending employees list.*/
	@FXML
	private Button removeAllBtn;
	
	/** The employee groups. Displays the selected group in the employees list. */
	@FXML
	private ChoiceBox<String> e_group;
	
	/** The first name text field. */
	@FXML
	private TextField givenNameTextField;
	
	/** The last name text field. */
	@FXML
	private TextField surNameTextField;
	
	/** The employees list. */
	@FXML
	private ListView<Employee> employeeListView;
	
	/** The attending employees list. */
	@FXML
	private ListView<Employee> attendingListView;

	/** The available employees, from the database.*/
	private Collection<Employee> availableEmployees = DBConnect.getEmployeeList();
	
	/** The stage. */
	private static Stage stage;
	
	/** The AddAppointment object. */
	private static AddAppointment addAppointment;
	
	/** The default attendees, from the AddAppointment. */
	private static ArrayList<Employee> defaultAttendees;
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
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
	
	/**
	 * Adds the selected employees.
	 */
	private void addSelectedEmployee() {
		Employee e = employeeListView.getSelectionModel().getSelectedItem();
		if(e != null) {
			attendingListView.getItems().add(e);
			employeeListView.getItems().remove(e);
		}
	}
	
	/**
	 * Removes the selected employees.
	 */
	private void removeSelectedEmployee() {
		Employee e = attendingListView.getSelectionModel().getSelectedItem();
		if(e != null) {
			attendingListView.getItems().remove(e);
			employeeListView.getItems().add(e);
		}
	}
	
	/**
	 * Updates the employees list.
	 */
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
	
	/**
	 * Start.
	 *
	 * @param stage the stage
	 * @param owner the owner
	 * @param addAppointment the AddAppointment
	 * @param attendees the attendees
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void start(Stage stage, Window owner, AddAppointment addAppointment, ArrayList<Employee> attendees) throws IOException {
		EmployeePicker.stage = stage;
		EmployeePicker.addAppointment = addAppointment;
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
