package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CalendarMain extends Application implements Initializable {
	
	@FXML
	private Pane mainView;
	
	@FXML
	private Button nextMonthBtn;
	
	@FXML
	private Button prevMonthBtn;
	
	@FXML
	private Label monthLabel;
	
	@FXML
	private Label yearLabel;

	private CalendarView calendarView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	Scene scene;
	Stage primaryStage;
	
	// HAX
	public void redraw() {
		primaryStage.setScene(null);
		primaryStage.setScene(scene);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL url = getClass().getResource("/com/gruppe16/main/mainPane.fxml");
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(url);
		fxmlLoader.setController(this);
		try {
			scene = new Scene((Parent)fxmlLoader.load(url.openStream()), 1280, 900);
			
			this.primaryStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

			redraw();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] months = {
				"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		};

		calendarView = new CalendarView();
		calendarView.setup(mainView);
		
		nextMonthBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				calendarView.nextMonth();
				monthLabel.setText(months[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});

		prevMonthBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				calendarView.prevMonth();
				monthLabel.setText(months[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});
		
		monthLabel.setText(months[calendarView.getMonth()]);
		yearLabel.setText(Integer.toString(calendarView.getYear()));
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}