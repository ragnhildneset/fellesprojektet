package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;

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

public class EmployeeFinder {
	@FXML
	private Button OKBtn;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private TextField givenNameTextField;
	
	@FXML
	private TextField surNameTextField;
	
	@FXML
	private ListView<Employee> employeeListView;
	
	private Employee employee;
	private Collection<Employee> cachedEmployees;
	
	public EmployeeFinder() {
		employee = null;
	}
	
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
		
		cachedEmployees = DBConnect.getEmployees().values();
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
	
	public Employee getEmployee() {
		return employee;
	}
}
