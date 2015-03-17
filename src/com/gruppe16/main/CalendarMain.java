package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Employee.Group;
import com.gruppe16.entities.Notif;
import com.gruppe16.util.ListOperations;

public class CalendarMain extends Application {
	static String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	
	@FXML
	private BorderPane mainPane;
	
	@FXML
	private ListView<CheckBox> groupListView;
	
	@FXML
	private ImageView nextDateBtn;
	
	@FXML
	private ImageView prevDateBtn;
	
	@FXML
	private Button findCalendarBtn;
	
	@FXML
	private Button backToCalendarBtn;
	
	@FXML
	private Button notificationBtn;
	
	@FXML
	private Button refresh;
	
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
	
	@FXML
	private Label notificationLabel;
	
	@FXML
	private Circle notificationCircle;
	
	private Scene scene;
	
	private Stage stage;
	
	private CalendarView calendarView; 

	private DayPlanView dayPlanView;
	
	private Employee employee;

	private boolean calendarShown = true;
	
	private Popup notificationMenu = null;
	
	private Accordion accordion = null;

	private ArrayList<Appointment> appointments = new ArrayList<Appointment>(); 
	
	private ArrayList<Group> selectedGroups = new ArrayList<Group>();
	
	public CalendarMain() {
		// DEBUG: Use first employee
		Login.login("admin", "passord");
		this.employee = Login.getCurrentUser();
	}
	
	public CalendarMain(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>(){

			@Override
			public void handle(WindowEvent arg0) {
				System.out.println("Closing window.");
			}
			
		});
		
		URL url = getClass().getResource("/com/gruppe16/main/CalendarMain.fxml");
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(url);
		fxmlLoader.setController(this);
		try {
			scene = new Scene((Parent)fxmlLoader.load(url.openStream()), 1005, 750);
			scene.getRoot().setStyle("-fx-background-color: linear-gradient(#FFFFFF, #EEEEEE)");
			scene.getStylesheets().add("/com/gruppe16/main/CalendarView.css");
			
			this.stage = stage;
			stage.setScene(scene);
			stage.setResizable(false);
			stage.centerOnScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}

		calendarView = new CalendarView(this);
		dayPlanView = new DayPlanView(this);

		setEmployee(employee);
		updateGroups();
		
		accordion = new Accordion();
		notificationMenu = new Popup();
		notificationMenu.setAutoHide(true);
		notificationMenu.getContent().add(accordion);
		
		updateNotifications();
		
		stage.show();
		
		refresh();
		redraw();
		
		selectAllGroupsBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				for(CheckBox checkBox : groupListView.getItems()) {
					checkBox.setSelected(true);
				}
			}
		});

		selectNoneGroupsBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				for(CheckBox checkBox : groupListView.getItems()) {
					checkBox.setSelected(false);
				}
			}
		});
		
		addAppointmentBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				try {
					Stage newStage = new Stage();
					newStage.setOnHidden(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent event) {
							if(calendarShown) {
								calendarView.update();
							}
							else {
								dayPlanView.showAppointments(employee);
							}
							redraw();
						}
					});
					AddAppointment.start(newStage, scene.getWindow(), calendarShown ? new Date() : dayPlanView.getDate());
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

		notificationBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notificationBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		notificationBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notificationBtn.setEffect(null);
			}
		});

		notificationBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notificationBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		notificationBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notificationBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
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
		
		refresh.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				try {
					CalendarMain calendar = new CalendarMain(Login.getCurrentUser());
					calendar.start(new Stage());
					stage.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		findCalendarBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				EmployeeFinder employeeFinder = new EmployeeFinder();
				Stage newStage = new Stage();
				newStage.setOnHidden(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent arg0) {
						Employee newEmployee = employeeFinder.getEmployee();
						if(newEmployee != null) {
							setEmployee(newEmployee);
							calendarView.setAppointments(DBConnect.getAppointmentsFromEmployee(newEmployee));
							calendarView.update();
						}
					}
				});
				employeeFinder.show(newStage, scene.getWindow());
			}
		});
		
		notificationBtn.setOnAction(event -> {
			if(notificationMenu.isShowing()) notificationMenu.hide();
			else /*if(DBConnect.getNotifications().size() > 0)*/ {
				Point2D pos = notificationBtn.localToScreen(-315.0, 40.0);
				notificationMenu.show(scene.getWindow(), pos.getX(), pos.getY());
			}
		});
		
		notificationBtn.setTooltip(new Tooltip("Notifications"));
		backToCalendarBtn.setTooltip(new Tooltip("Back to calendar"));
		
		calendarView.setAppointments(DBConnect.getAppointmentsFromEmployee(Login.getCurrentUser()));
		
		for(CheckBox cb : groupListView.getItems()){
			
			Group g = Employee.getFromName(cb.getText());
			
			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> arg0,
						Boolean arg1, Boolean arg2) {
					if(arg2&&!selectedGroups.contains(g)){
						selectedGroups.add(g);
					} else {
						selectedGroups.remove(g);
					}
					appointments.clear();
					if(!selectedGroups.isEmpty()){
						appointments.clear();
						for(Group g : selectedGroups){
							appointments = (ArrayList<Appointment>) ListOperations.union(appointments, DBConnect.getGroupApp(g));
						}
					} else {
						appointments = DBConnect.getAppointmentsFromEmployee(employee);
					}
					calendarView.setAppointments(appointments);
					calendarView.update();
				}
				
			});
		}
		calendarView.update();
	}
	
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

		backToCalendarBtn.setOnAction(event -> {
			showCalendar(dayPlanView.getDate());
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
	
	private void updateGroups() {
		
		ObservableList<CheckBox> items = FXCollections.observableArrayList();
		
		for(Group g : Employee.getGroups()){			
			CheckBox djd = new CheckBox(g.name);
			items.add(djd);
		}
		
		groupListView.setItems(items);
	}

	private void updateNotifications() {
		for(Appointment appointment : DBConnect.getAppointmentsFromEmployee(Login.getCurrentUser())){
			boolean found = false;
			for(TitledPane pane : accordion.getPanes()) {
				if(pane.getUserData() instanceof Appointment && ((Appointment)pane.getUserData()).getID() == appointment.getID()) {
					found = true;
					break;
				}
			}
			
			if(found) {
				continue;
			}
			
			if(LocalDate.now().equals(appointment.getAppDate())){
				if(appointment.getFromTime().toSecondOfDay() - LocalTime.now().toSecondOfDay() < 3600 &&
						appointment.getFromTime().toSecondOfDay() - LocalTime.now().toSecondOfDay() > 0){
					Label label = new Label("Appointment " + appointment.getTitle() + " in " + String.valueOf((appointment.getFromTime().toSecondOfDay() - LocalTime.now().toSecondOfDay())/60) + " minutes.");
					TitledPane pane = new TitledPane("Alarm for " + appointment.getTitle(), label);
					pane.setUserData(appointment);
					accordion.getPanes().add(pane);
				}
			}
		}
		
		for(Notif notif : DBConnect.getInvites()) {
			if(notif.status > 0){
				continue;
			}
			
			boolean found = false;
			for(TitledPane pane : accordion.getPanes()) {
				if(pane.getUserData() instanceof Notif && ((Notif)pane.getUserData()).appid == notif.appid) {
					found = true;
					break;
				}
			}
			
			if(found) {
				continue;
			}
			
			NotificationView notificationView = new NotificationView(notif);
			TitledPane pane = new TitledPane("", notificationView);
			
			Label titleLabel = new Label("Invitation for " + notif.title);
			titleLabel.setPrefWidth(250.0);
			
			Runnable onAccept = new Runnable() {
				@Override
				public void run() {
					notif.accept();
					accordion.getPanes().remove(pane);
					updateNotificationCounter();
				}
			};
			notificationView.setOnAccept(onAccept);
			
			Runnable onDecline = new Runnable() {
				@Override
				public void run() {
					notif.decline();
					accordion.getPanes().remove(pane);
					updateNotificationCounter();
				}
			};
			notificationView.setOnDecline(onDecline);

			Button acceptBtn = new Button();
			acceptBtn.setTooltip(new Tooltip("Accept"));
			acceptBtn.setGraphic(new ImageView("http://findicons.com/files/icons/1581/silk/16/tick.png"));
			acceptBtn.setOnMouseClicked(event -> {
				onAccept.run();
			});
			
			Button declineBtn = new Button();
			declineBtn.setTooltip(new Tooltip("Decline"));
			declineBtn.setGraphic(new ImageView("http://findicons.com/files/icons/1715/gion/16/dialog_cancel.png"));
			declineBtn.setOnMouseClicked(event -> {
				onDecline.run();
			});
			
			HBox hbox = new HBox(titleLabel, acceptBtn, declineBtn);
			hbox.setAlignment(Pos.CENTER_LEFT);
			
			pane.setUserData(notif);
			pane.setGraphic(hbox);
			pane.setAnimated(false);
			accordion.getPanes().add(pane);
		}
		updateNotificationCounter();
	}
	
	private void updateNotificationCounter() {
		int notificationCount = accordion.getPanes().size();
		if(notificationCount == 0) {
			notificationLabel.setVisible(false);
			notificationCircle.setVisible(false);
		} else {
			notificationLabel.setVisible(true);
			notificationCircle.setVisible(true);
			if(notificationCount < 10) {
				notificationLabel.setText(String.valueOf(notificationCount));
			} else {
				notificationLabel.setText("9+");
			}
		}
	}
	
	public Scene getScene(){
		return scene;
	}
	
	// HAX
	public void setEmployee(Employee employee) {
		this.employee = employee;
		
		// Update calendar label and title
		if(employee.getEmployeeID() == Login.getCurrentUserID()) {
			stage.setTitle("My Calendar");
			calendarNameLabel.setText("Welcome, " + employee.getFirstName() + "!");
		}
		else {
			stage.setTitle(employee.getFirstName() + " " + employee.getLastName() + "'s Calendar");
			calendarNameLabel.setText(employee.getName());
		}
		
		// Show calendar and redraw
		showCalendar(new Date());
		redraw();
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
	public void refresh() {
		if(calendarShown) {
			showCalendar(calendarView.getDate());
		}
		else {
			showDayPlan(dayPlanView.getDate());
		}
		updateNotifications();
		
		new Timer().schedule( 
	        new TimerTask() {
	            @Override
	            public void run() {
	            	Platform.runLater(new Runnable() {
						@Override
						public void run() {
							refresh();
						}
	            	});
	            	
	            }
	        },
	        30000
		);
	}
	
	public void redraw() {
		// HAX
		stage.setScene(null);
		stage.setScene(scene);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
