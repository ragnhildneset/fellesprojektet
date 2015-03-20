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

/**
 * Main calendar window.
 * 
 * @author Gruppe 16
 */
public class CalendarMain extends Application {
	private static String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

	/**
	 * The main pane of the window. mainPane.getCenter() is the calendar.
	 */
	@FXML
	private BorderPane mainPane;

	/**
	 * The group list view. A list view of check boxes.
	 */
	@FXML
	private ListView<CheckBox> groupListView;

	/**
	 * Next date button. Shows the next month in the calendar view, or the next day in the day planer.
	 */
	@FXML
	private ImageView nextDateBtn;

	/**
	 * Previous date button. Shows the previous month in the calendar view, or the previous day in the day planer.
	 */
	@FXML
	private ImageView prevDateBtn;

	/**
	 * Finds another employee's calendar.
	 */
	@FXML
	private Button findCalendarBtn;

	/**
	 * When in the calendar view, this shows the current month. When in day planer, this jumps back to the calendar.
	 */
	@FXML
	private Button backToCalendarBtn;

	/**
	 * Notification button. Pressing this will show the notifications in a popup-window.
	 */
	@FXML
	private Button notificationBtn;

	/**
	 * Button for selecting all the groups.
	 */
	@FXML
	private Button selectAllGroupsBtn;

	/**
	 * Button for deselecting all the groups.
	 */
	@FXML
	private Button selectNoneGroupsBtn;

	/**
	 * Add appointment button. Shows the add appointment dialog when clicked.
	 */
	@FXML
	private Button addAppointmentBtn;

	/**
	 * Goes back to the current user's calendar when clicked.
	 */
	@FXML
	private Button myCalendarBtn;

	/**
	 * Refresh button. Forces a sync with the application with the database.
	 */
	@FXML
	private Button refreshBtn;

	/**
	 * Current month/day label.
	 */
	@FXML
	private Label monthLabel;

	/**
	 * Current year label.
	 */
	@FXML
	private Label yearLabel;

	/**
	 * Current user label.
	 */
	@FXML
	private Label calendarNameLabel;
	
	/**
	 * Now viewing label.
	 */
	@FXML
	private Label nowViewingLabel;
	
	/**
	 * Notification count label.
	 */
	@FXML
	private Label notificationLabel;

	/**
	 * Notification bubble. Hidden when there's no more notifications.
	 */
	@FXML
	private Circle notificationCircle;

	/**
	 * Main scene.
	 */
	private Scene scene;

	/**
	 * Main stage.
	 */
	private Stage stage;

	/**
	 * The calendar viewer. Shown by calling mainPane.setCenter(calendarView);
	 */
	private CalendarView calendarView; 

	/**
	 * The day plan viewer. Shown by calling mainPane.setCenter(dayPlanView);
	 */
	private DayPlanView dayPlanView;
	
	/**
	 * The current calendar view interface. This is either a CalendarView or a DayPlanView.
	 */
	private CalendarViewInterface calendarViewInterface = null;

	/**
	 * Currently viewed employee.
	 */
	static private Employee employee;

	/**
	 * The notification popup menu.
	 */
	private Popup notificationMenu = null;

	/**
	 * The notifications accordion pane.
	 */
	private Accordion accordion = null;
	
	/**
	 * Are we viewing group calendars?
	 */
	private static boolean group = false;

	/**
	 * Selected groups.
	 */
	static private ArrayList<Group> selectedGroups = new ArrayList<Group>(); 
	
	/**
	 * Called when monthLabel and yearLabel needs to update.
	 */
	private Runnable onUpdateLabels;

	/**
	 * Default constructor. Logs in as the administrator. Used for debug purposes.
	 */
	public CalendarMain() {
		// DEBUG: Run as admin
		Login.login("admin", "passord");
		CalendarMain.employee = Login.getCurrentUser();
	}

	/**
	 * Shows the calendar as employee
	 * @param employee The employee to show the calendar for
	 */
	public CalendarMain(Employee employee) {
		CalendarMain.employee = employee;
	}

	@SuppressWarnings("deprecation")
	@Override
	/**
	 * Shows the calendar in the given stage.
	 * @param stage The stage to use.
	 */
	public void start(Stage stage) throws Exception {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				System.out.println("Closing window.");
				System.exit(0);
			}
		});

		URL url = getClass().getResource("/com/gruppe16/main/CalendarMain.fxml");

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(url);
		fxmlLoader.setController(this);
		try {
			scene = new Scene((Parent)fxmlLoader.load(url.openStream()), 1002, 741);
			scene.getRoot().setStyle("-fx-background-color: linear-gradient(#FFFFFF, #EEEEEE)");
			scene.getStylesheets().add("/com/gruppe16/main/CalendarView.css");

			this.stage = stage;
			stage.setScene(scene);
			stage.setResizable(false);
			stage.centerOnScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		calendarNameLabel.setText("Welcome, " + employee.getFirstName() + "!");

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
		scheduleRefresh();
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
							calendarViewInterface.showAppointments(getGroupAppointments());
							redraw();
						}
					});
					AddAppointment.start(newStage, scene.getWindow(), calendarViewInterface.getDate());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					calendarViewInterface.showAppointments(getGroupAppointments());
					redraw();
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
		
		refreshBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				refreshBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		refreshBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				refreshBtn.setEffect(null);
			}
		});

		refreshBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				refreshBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		refreshBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				refreshBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		myCalendarBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				myCalendarBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		myCalendarBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				myCalendarBtn.setEffect(null);
			}
		});

		myCalendarBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				myCalendarBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		myCalendarBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				myCalendarBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
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
							calendarView.showAppointments(DBConnect.getActiveAppointmentsFromEmployee(newEmployee));
							for(CheckBox cb : groupListView.getItems()){
								cb.setSelected(false);
							}
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
		refreshBtn.setTooltip(new Tooltip("Refresh"));
		myCalendarBtn.setTooltip(new Tooltip("Show my calendar"));

		calendarView.showAppointments(DBConnect.getActiveAppointmentsFromEmployee(Login.getCurrentUser()));

		for(CheckBox cb : groupListView.getItems()){
			Group g = Employee.getFromName(cb.getText());
			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
					if(arg2 && !selectedGroups.contains(g)){
						selectedGroups.add(g);
					} else {
						selectedGroups.remove(g);
					}
					calendarView.showAppointments(getGroupAppointments());
					if(dayPlanView.getDate() != null) dayPlanView.showAppointments(getGroupAppointments(), group);
				}

			});
		}

		nextDateBtn.setOnMouseClicked(event -> {
			calendarViewInterface.incDate();
			yearLabel.setText(Integer.toString(calendarViewInterface.getDate().getYear()));
			onUpdateLabels.run();
			redraw();
		});

		prevDateBtn.setOnMouseClicked(event -> {
			calendarViewInterface.decDate();
			yearLabel.setText(Integer.toString(calendarViewInterface.getDate().getYear()));
			onUpdateLabels.run();
			redraw();
		});

		backToCalendarBtn.setOnAction(event -> {
			showCalendar(calendarViewInterface instanceof CalendarView ? new Date() : dayPlanView.getDate());
		});

		refreshBtn.setOnAction(event -> {
			refresh();
		});
		
		myCalendarBtn.setOnAction(event -> {
			setEmployee(Login.getCurrentUser());
		});
	}

	/**
	 * Returns the appointments for the given group configuration.
	 * @return List of the appointments that fit the group configuration.
	 */
	public static ArrayList<Appointment> getGroupAppointments() {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		if(!selectedGroups.isEmpty()) {
			group = true;
			appointments.clear();
			for(Group g : selectedGroups) {
				appointments = (ArrayList<Appointment>) ListOperations.union(appointments, DBConnect.getGroupApp(g));
			}
		} else {
			appointments = DBConnect.getActiveAppointmentsFromEmployee(employee);
			group = false;
		}
		return appointments;
	}

	/**
	 * Shows the calendar in the mainPane.
	 * @param date The date to show the calendar for.
	 */
	private void showCalendar(Date date) {
		calendarViewInterface = calendarView;
		calendarView.showAppointments(getGroupAppointments());
		calendarView.setDate(date);
		mainPane.setCenter(calendarView);
		onUpdateLabels = new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				monthLabel.setText(MONTH_NAMES[calendarViewInterface.getDate().getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
			}
		};
		onUpdateLabels.run();
	}

	/**
	 * Shows the day planer in the mainPane.
	 * @param date The date to show the day planer for.
	 */
	@SuppressWarnings("deprecation")
	void showDayPlan(Date date) {
		calendarViewInterface = dayPlanView;
		dayPlanView.setDate(date);
		dayPlanView.showAppointments(getGroupAppointments(), group);
		mainPane.setCenter(dayPlanView);
		yearLabel.setText(Integer.toString(calendarView.getYear()));
		onUpdateLabels = new Runnable() {
			@Override
			public void run() {
				Date date = calendarViewInterface.getDate();
				monthLabel.setText(MONTH_NAMES[date.getMonth()] + " " + date.getDate());
				yearLabel.setText(Integer.toString(calendarView.getYear()));
			}
		};
		onUpdateLabels.run();
	}

	/**
	 * Updates the group list
	 */
	private void updateGroups() {

		ObservableList<CheckBox> items = FXCollections.observableArrayList();

		for(Group g : Employee.getGroups()){			
			CheckBox djd = new CheckBox(g.name);
			items.add(djd);
		}

		groupListView.setItems(items);
	}
	
	/**
	 * Updates the notifications.
	 */
	private void updateNotifications() {
		for(Appointment appointment : DBConnect.getActiveAppointmentsFromEmployee(Login.getCurrentUser())){
			TitledPane newPane = null;
			for(TitledPane pane : accordion.getPanes()) {
				if(pane.getUserData() instanceof Appointment && ((Appointment)pane.getUserData()).getID() == appointment.getID()) {
					newPane = pane;
					break;
				}
			}

			String alarmStr = "Appointment " + appointment.getTitle() + " in " + String.valueOf((appointment.getFromTime().toSecondOfDay() - LocalTime.now().toSecondOfDay())/60) + " minutes.";
			if(newPane != null) {
				((Label)newPane.getContent()).setText(alarmStr);
				continue;
			}

			if(LocalDate.now().equals(appointment.getAppDate())){
				if(appointment.getFromTime().toSecondOfDay() - LocalTime.now().toSecondOfDay() < 3600 &&
						appointment.getFromTime().toSecondOfDay() - LocalTime.now().toSecondOfDay() > 0) {
					TitledPane pane = new TitledPane("Alarm for " + appointment.getTitle(), new Label(alarmStr));
					pane.setUserData(appointment);
					accordion.getPanes().add(pane);
				}
			}
		}

		for(Notif notification : DBConnect.getInvites()) {
			if(notification.status > 0){
				continue;
			}

			boolean found = false;
			for(TitledPane pane : accordion.getPanes()) {
				if(pane.getUserData() instanceof Notif && ((Notif)pane.getUserData()).appointmentID == notification.appointmentID) {
					found = true;
					break;
				}
			}

			if(found) {
				continue;
			}

			NotificationView notificationView = new NotificationView(notification);
			TitledPane pane = new TitledPane("", notificationView);

			Label titleLabel = new Label("Invitation for " + notification.title);
			titleLabel.setPrefWidth(250.0);

			Runnable onAccept = new Runnable() {
				@Override
				public void run() {
					notification.accept();
					accordion.getPanes().remove(pane);
					updateNotificationCounter();
				}
			};
			notificationView.setOnAccept(onAccept);

			Runnable onDecline = new Runnable() {
				@Override
				public void run() {
					notification.decline();
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

			pane.setUserData(notification);
			pane.setGraphic(hbox);
			pane.setAnimated(false);
			accordion.getPanes().add(pane);
		}

		updateNotificationCounter();
	}

	/**
	 * Updates the notification counter.
	 */
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

	/**
	 * Get the main scene.
	 * @return The main scene.
	 */
	public Scene getScene(){
		return scene;
	}

	/**
	 * Set the current employee and show their calendar.
	 * @param employee the employee
	 */
	public void setEmployee(Employee employee) {
		CalendarMain.employee = employee;

		// Update calendar label and title
		if(employee.getID() == Login.getCurrentUserID()) {
			myCalendarBtn.setVisible(false);
			stage.setTitle("My Calendar");
			nowViewingLabel.setText("");
		}
		else {
			myCalendarBtn.setVisible(true);
			stage.setTitle(employee.getFirstName() + " " + employee.getLastName() + "'s Calendar");
			nowViewingLabel.setText("Now viewing " + employee.getName() + "'s calendar.");
		}

		// Show calendar and redraw
		showCalendar(new Date());
		redraw();
	}

	/**
	 * Get the current employee
	 * @return The current employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * Syncs the calendar gui with the content of the database
	 */
	private void refresh() {
		if(calendarViewInterface instanceof CalendarView) {
			showCalendar(calendarView.getDate());
		}
		else {
			showDayPlan(dayPlanView.getDate());
		}
		updateNotifications();
	}

	/**
	 * Starts the refresh loop
	 */
	private void scheduleRefresh() {
		new Timer().schedule( 
				new TimerTask() {
					@Override
					public void run() {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								refresh();
								scheduleRefresh();
							}
						});

					}
				},
				30000
				);
	}

	/**
	 * Forces a redraw of the window
	 */
	public void redraw() {
		stage.setScene(null);
		stage.setScene(scene);
	}

	/**
	 * Debug launch function
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
