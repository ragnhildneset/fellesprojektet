package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.gruppe16.entities.Employee;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddEmployee extends Application implements Initializable {
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		try {
			Scene scene = new Scene((AnchorPane) FXMLLoader.load(Main.class.getResource("/com/gruppe16/main/addEmployee.fxml")));
			arg0.setScene(scene);
			arg0.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML Button button;
	@FXML TextField firstName;
	@FXML TextField lastName;
	@FXML TextField email;
	@FXML ListView list;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		Employee.initialize();
		
		ObservableList<String> listt = FXCollections.observableArrayList();
		String[] ls = Employee.getNames();
		listt.addAll(ls);
		list.setItems(listt);
	
		
		button.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try{
					Employee.addNew(firstName.getText(), lastName.getText(), email.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}

}
