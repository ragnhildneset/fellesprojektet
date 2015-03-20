package com.gruppe16.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;

/**
 * The Class EmployeeFinder. A GUI for finding and selecting an employee.
 * 
 * @author Gruppe 16
 */
public class EmployeeFinder {
	
	/** The OK button. Sets the employee variable to the currently selected employee.*/
	@FXML
	private Button OKBtn;
	
	/** The cancel button. Closes the window without selecting an employee. */
	@FXML
	private Button cancelBtn;
	
	/** The first name text field. Used to search for an employee by first name.*/
	@FXML
	private TextField givenNameTextField;
	
	/** The last name text field. Used to search for an employee by last name. */
	@FXML
	private TextField surNameTextField;
	
	/** The employee list. */
	@FXML
	private ListView<Employee> employeeListView;
	
	/** The selected employee. Null before an employee is selected.*/
	private Employee employee;
	
	/** The cached employees. A collection with the current employees in the database, cached.*/
	private Collection<Employee> cachedEmployees;
	
	/**
	 * Instantiates a new employee finder, and sets employee to null.
	 */
	public EmployeeFinder() {
		employee = null;
	}
	
	/**
	 * Show.
	 *
	 * @param stage the stage
	 * @param owner the owner
	 */
	public void show(Stage stage, Window owner) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setController(this);
		Scene scene = null;
		try {
			scene = new Scene((Parent)fxmlLoader.load(Employee.class.getResource("/com/gruppe16/main/EmployeeFinder.fxml").openStream()));
			stage.setResizable(false);
			stage.initStyle(StageStyle.UTILITY);
			if(owner != null) {
				stage.initOwner(owner);
				stage.initModality(Modality.WINDOW_MODAL);
			}
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cachedEmployees = DBConnect.getEmployeeList();
		employeeListView.setItems(FXCollections.observableArrayList(cachedEmployees));
		
		OKBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				employee = employeeListView.getSelectionModel().getSelectedItem();
				stage.close();
			}
		});
		
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
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

		employeeListView.setOnMouseClicked(event -> {
			if(event.getClickCount() == 2) {
				employee = employeeListView.getSelectionModel().getSelectedItem();
				stage.close();
			}
		});
		
		updateEmployeeList();
	}
	
	/**
	 * Updates the employee list.
	 */
	private void updateEmployeeList() {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		for(Employee e : cachedEmployees) {
			if(e.getFirstName().toLowerCase().contains(givenNameTextField.getText().toLowerCase()) && e.getLastName().toLowerCase().contains(surNameTextField.getText().toLowerCase())) {
				employees.add(e);
			}
		}
		employeeListView.setItems(FXCollections.observableArrayList(employees));
		employeeListView.getSelectionModel().select(0);
	}
	
	/**
	 * Gets the employee.
	 *
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}
}
