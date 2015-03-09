package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.gruppe16.entities.Employee;

public class CalendarMain extends Application {
	static String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	
	@FXML
	private BorderPane mainPane;
	
	@FXML
	private ListView<HBox> groupListView;
	
	@FXML
	private ImageView nextDateBtn;
	
	@FXML
	private ImageView prevDateBtn;
	
	@FXML
	private Button findCalendarBtn;
	
	@FXML
	private ImageView backToCalendarBtn;
	
	@FXML
	private ImageView alertBtn;
	
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
	
	private Employee employee;
	
	public CalendarMain() {
		// DEBUG: Use first employee
		Login.login("a", "b");
		this.employee = Login.getCurrentUser();
	}
	
	public CalendarMain(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL url = getClass().getResource("/com/gruppe16/main/CalendarMain.fxml");
		
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
				try {
					AddAppointment.start(new Stage(), scene.getWindow(), calendarShown ? new Date() : dayPlanView.getDate());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		nextDateBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nextDateBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		nextDateBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nextDateBtn.setEffect(null);
			}
		});

		nextDateBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nextDateBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		nextDateBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				nextDateBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		prevDateBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				prevDateBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		prevDateBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				prevDateBtn.setEffect(null);
			}
		});

		prevDateBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				prevDateBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		prevDateBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				prevDateBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		alertBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				alertBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		alertBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				alertBtn.setEffect(null);
			}
		});

		alertBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				alertBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		alertBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				alertBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		backToCalendarBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				backToCalendarBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		backToCalendarBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				backToCalendarBtn.setEffect(null);
			}
		});

		backToCalendarBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				backToCalendarBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		backToCalendarBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				backToCalendarBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});
	}
	boolean calendarShown = true;
	
	void showCalendar(Date date) {
		calendarShown = true;
		
		calendarView.setDate(date);
		mainPane.setCenter(calendarView);
		
		backToCalendarBtn.setVisible(false);
		
		monthLabel.setText(MONTH_NAMES[calendarView.getMonth()]);
		yearLabel.setText(Integer.toString(calendarView.getYear()));

		nextDateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evnet) {
				calendarView.nextMonth();
				monthLabel.setText(MONTH_NAMES[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});

		prevDateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evnet) {
				calendarView.prevMonth();
				monthLabel.setText(MONTH_NAMES[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});
	}
	
	void showDayPlan(Date date) {
		calendarShown = false;
		
		dayPlanView.setDate(date);
		dayPlanView.showAppointments(employee);
		mainPane.setCenter(dayPlanView);
		
		backToCalendarBtn.setVisible(true);
		
		monthLabel.setText(MONTH_NAMES[date.getMonth()] + " " + date.getDate());
		yearLabel.setText(Integer.toString(calendarView.getYear()));

		backToCalendarBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evnet) {
				showCalendar(dayPlanView.getDate());
			}
		});

		nextDateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evnet) {
				dayPlanView.nextDay();
				monthLabel.setText(MONTH_NAMES[date.getMonth()] + " " + date.getDate());
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});

		prevDateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evnet) {
				dayPlanView.prevDay();
				monthLabel.setText(MONTH_NAMES[date.getMonth()] + " " + date.getDate());
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
		checkBox.setPadding(new Insets(0,80,0,0));
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
