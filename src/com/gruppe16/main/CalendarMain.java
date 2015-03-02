package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class CalendarMain extends Application {
	static String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	
	@FXML
	private BorderPane mainPane;
	
	@FXML
	private ListView<HBox> groupListView;
	
	@FXML
	private Button nextDateBtn;
	
	@FXML
	private Button prevDateBtn;
	
	@FXML
	private Button findCalendarBtn;
	
	@FXML
	private Button backToCalendarBtn;
	
	@FXML
	private Button selectAllGroupsBtn;
	
	@FXML
	private Button selectNoneGroupsBtn;
	
	@FXML
	private Button addAppointmentBtn;
	
	@FXML
	private Label monthLabel;
	
	@FXML
	private Label yearLabel;
	
	@FXML
	private Label calendarNameLabel;
	
	private Scene scene;
	
	private Stage primaryStage;
	
	private CalendarView calendarView; 

	private DayPlanView dayPlanView;
	
	private LocalDate someDate = LocalDate.now();
	
	private Employee employee;
	
	public CalendarMain() {
		// DEBUG: Use first employee
		this.employee = DBConnect.getEmployees().values().iterator().next();
	}
	
	public CalendarMain(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL url = getClass().getResource("/com/gruppe16/main/MainPane.fxml");
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(url);
		fxmlLoader.setController(this);
		try {
			scene = new Scene((Parent)fxmlLoader.load(url.openStream()), 1005, 750);
			scene.getRoot().setStyle("-fx-background-color: linear-gradient(#FFFFFF, #EEEEEE)");
			
			this.primaryStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle(employee.getFirstName() + "'s Calendar");
			primaryStage.centerOnScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		calendarNameLabel.setText(employee.getName());

		calendarView = new CalendarView(this);
		dayPlanView = new DayPlanView(this);
		
		showCalendar(new Date());
		updateGroups();

		primaryStage.show();
		redraw();

		selectAllGroupsBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				for(HBox hbox : groupListView.getItems()) {
					CheckBox checkBox = (CheckBox)hbox.getChildren().get(0);
					checkBox.setSelected(true);
				}
			}
		});

		selectNoneGroupsBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				for(HBox hbox : groupListView.getItems()) {
					CheckBox checkBox = (CheckBox)hbox.getChildren().get(0);
					checkBox.setSelected(false);
				}
			}
		});
		
		addAppointmentBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				AddAppointment appointment = new AddAppointment(scene.getWindow());
				try {
					appointment.start(new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	void showCalendar(Date date) {
		calendarView.setDate(date);
		mainPane.setCenter(calendarView);
		
		backToCalendarBtn.setVisible(false);
		
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
		dayPlanView.setDate(date);
		dayPlanView.showAppointments(employee);
		mainPane.setCenter(dayPlanView);
		
		backToCalendarBtn.setVisible(true);
		
		monthLabel.setText(date.toString());
		yearLabel.setText(Integer.toString(calendarView.getYear()));

		backToCalendarBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				showCalendar(dayPlanView.getDate());
			}
		});

		nextDateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				dayPlanView.nextDay();
				monthLabel.setText(date.toString());
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});

		prevDateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				dayPlanView.prevDay();
				monthLabel.setText(date.toString());
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});
	}
	
	void updateGroups() {
		ObservableList<HBox> items = FXCollections.observableArrayList();
		
		// Testkode
		HBox hbox = new HBox();
		hbox.getChildren().add(new CheckBox("Group 1"));
		items.add(hbox);
		
		hbox = new HBox();
		hbox.getChildren().add(new CheckBox("Group 2"));
		items.add(hbox);
		
		hbox = new HBox();
		CheckBox checkBox = new CheckBox("Group 3");
		checkBox.setPadding(new Insets(0,70,0,0));
		hbox.getChildren().add(checkBox);
		
		ImageView button = new ImageView("http://findicons.com/files/icons/2226/matte_basic/16/edit.png");
		button.setScaleX(0.75);
		button.setScaleY(0.75);
		hbox.getChildren().add(button);
		
		items.add(hbox);
		
		groupListView.setItems(items);
	}
	
	// HAX
	public void redraw() {
		primaryStage.setScene(null);
		primaryStage.setScene(scene);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
