package com.gruppe16.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * The Class Main. The main class of the program.
 * 
 * @author Gruppe 16
 */
public class Main extends Application implements Initializable {
	
	/** The text field for inputing username */
	@FXML
	private TextField username;
	
	/** The text field for inputing password. */
	@FXML
	private PasswordField password;
	
	/** The login button. */
	@FXML
	private Button loginBtn;
	
	/** The cancel button. */
	@FXML
	private Button cancelBtn;
	
	/** The pane. */
	@FXML
	private Pane pane;
	
	/** The stage. */
	static private Stage stage;

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(Login.login(username.getText(), password.getText())) {
					try {
						CalendarMain calendar = new CalendarMain(Login.getCurrentUser());
						calendar.start(new Stage());
						stage.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					if(!username.focusedProperty().get()) username.setEffect(new InnerShadow(4.0, Color.RED));
					if(!password.focusedProperty().get()) password.setEffect(new InnerShadow(4.0, Color.RED));
				}
			}
		});
		
		cancelBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
			
		});
		
		username.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) username.setEffect(null);
			}
		});
		
		password.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) password.setEffect(null);
			}
		});
		
		loginBtn.setDefaultButton(true);
	}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Main.stage = stage;
		try{
			Scene scene = new Scene( (Parent) FXMLLoader.load(getClass().getResource("/com/gruppe16/main/Login.fxml")));
			stage.setResizable(false);
			stage.setTitle("Calendar login");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The main method, for launching the program.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
