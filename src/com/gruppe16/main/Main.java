package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class Main extends Application implements Initializable {
	@FXML
	private TextField user;
	
	@FXML
	private TextField pass;
	
	@FXML
	private Button loginBtn;
	
	static private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(Login.login(user.getText(), pass.getText())) {
					try {
						CalendarMain calendar = new CalendarMain(Login.getCurrentUser());
						calendar.start(stage);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void start(Stage stage) throws Exception {
		Main.stage = stage;
		try{
			Scene scene = new Scene( (Parent) FXMLLoader.load(getClass().getResource("/com/gruppe16/main/login.fxml")));
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
