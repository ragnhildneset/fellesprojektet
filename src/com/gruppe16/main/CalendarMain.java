package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CalendarMain extends Application implements Initializable {
	static String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	
	@FXML
	private BorderPane mainPane;
	
	@FXML
	private VBox calendarGroupPane;
	
	@FXML
	private Button nextDateBtn;
	
	@FXML
	private Button prevDateBtn;
	
	@FXML
	private Button findCalendarBtn;
	
	@FXML
	private Label monthLabel;
	
	@FXML
	private Label yearLabel;
	
	private CalendarGroupSelector calendarGroupList;
	
	private Scene scene;
	
	private Stage primaryStage;
	
	private CalendarView calendarView; 
	
	private CopyOfDayPlanView dayPlan;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL url = getClass().getResource("/com/gruppe16/main/mainPane.fxml");
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(url);
		fxmlLoader.setController(this);
		try {
			scene = new Scene((Parent)fxmlLoader.load(url.openStream()), 1000, 750);
			scene.getRoot().setStyle("-fx-background-color: linear-gradient(#FFFFFF, #EEEEEE)");
			
			this.primaryStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("name's Calendar");
			primaryStage.show();

			redraw();
		} catch (IOException e) {
			e.printStackTrace();
		}

		calendarView = new CalendarView(this);
		dayPlan = new CopyOfDayPlanView(this);
		
		showCalendar(new Date());
		
		calendarGroupList = new CalendarGroupSelector(scene);
		calendarGroupList.setup(calendarGroupPane);
	}
	
	void showCalendar(Date date) {
		calendarView.setDate(date);
		mainPane.setCenter(calendarView);
		
		monthLabel.setText(MONTH_NAMES[calendarView.getMonth()]);
		yearLabel.setText(Integer.toString(calendarView.getYear()));

		nextDateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				calendarView.nextMonth();
				monthLabel.setText(MONTH_NAMES[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});

		prevDateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				calendarView.prevMonth();
				monthLabel.setText(MONTH_NAMES[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});
	}
	
	void showDayPlan(Date date) {
		dayPlan.setDate(date);
		mainPane.setCenter(dayPlan);
		
		monthLabel.setText(date.toString());
		yearLabel.setText(Integer.toString(calendarView.getYear()));

		nextDateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				dayPlan.nextDay();
				monthLabel.setText(date.toString());
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});

		prevDateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				dayPlan.prevDay();
				monthLabel.setText(date.toString());
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});
	}
	
	// HAX
	public void redraw() {
		primaryStage.setScene(null);
		primaryStage.setScene(scene);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
