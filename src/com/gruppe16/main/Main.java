package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Main extends Application implements Initializable {
	@FXML
	private TextField user;
	
	@FXML
	private PasswordField pass;
	
	@FXML
	private Button loginBtn;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Pane pane;
	
	static private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(Login.login(user.getText(), pass.getText())) {
					try {
						CalendarMain calendar = new CalendarMain(Login.getCurrentUser());
						stage.close();
						calendar.start(new Stage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					if(!user.focusedProperty().get()) user.setEffect(new InnerShadow(4.0, Color.RED));
					if(!pass.focusedProperty().get()) pass.setEffect(new InnerShadow(4.0, Color.RED));
				}
			}
		});
		
		cancelBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
			
		});
		
		user.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) user.setEffect(null);
			}
		});
		
		pass.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) pass.setEffect(null);
			}
		});
		
		loginBtn.setDefaultButton(true);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Main.stage = stage;
		try{
			Scene scene = new Scene( (Parent) FXMLLoader.load(getClass().getResource("/com/gruppe16/main/Login.fxml")));
			stage.initStyle(StageStyle.UTILITY);
			stage.setTitle("Calendar login");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
