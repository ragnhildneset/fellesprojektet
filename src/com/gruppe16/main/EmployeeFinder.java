package com.gruppe16.main;

import java.io.IOException;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class EmployeeFinder implements Initializable {
	@FXML
	private Button addBtn;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private TextField givenNameTextField;
	
	@FXML
	private TextField sirNameTextField;
	
	@FXML
	private ListView<String> employeeListView;
	
	private static Stage stage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
	}
	
	public static void start(Stage stage, Window owner) throws IOException {
		EmployeeFinder.stage = stage;
		Scene scene = new Scene((Parent)FXMLLoader.load(EmployeeFinder.class.getResource("/com/gruppe16/main/EmployeeFinder.fxml")));
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
