package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Application;
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
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Notif;

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
	private ImageView notifyBtn;
	
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
		//showCalendar(new Date());
		updateGroups();
		accordion = new Accordion();
		notificationMenu = new Popup();
		notificationMenu.setAutoHide(true);
		notificationMenu.getContent().add(accordion);
		setupNotifications();
		updateNotif();
		stage.show();
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

		notifyBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notifyBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
			}
		});

		notifyBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notifyBtn.setEffect(null);
			}
		});

		notifyBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notifyBtn.setEffect(new ColorAdjust(0.0, 0.0, -0.2, 0.0));
			}
		});

		notifyBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				notifyBtn.setEffect(new ColorAdjust(0.0, 0.0, 0.2, 0.0));
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
		
		findCalendarBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				EmployeeFinder employeeFinder = new EmployeeFinder();
				Stage newStage = new Stage();
				newStage.setOnHidden(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent arg0) {
						Employee newEmployee = employeeFinder.getEmployee();
						if(newEmployee != null) {
							setEmployee(newEmployee);
						}
					}
				});
				employeeFinder.show(newStage, scene.getWindow());
			}
		});
		
		notifyBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(notificationMenu.isShowing()) notificationMenu.hide();
				else /*if(DBConnect.getNotifications().size() > 0)*/ {
					Point2D pos = notifyBtn.localToScreen(-270.0, 40.0);
					notificationMenu.show(scene.getWindow(), pos.getX(), pos.getY());
				}
			}
		});
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
	
	private void updateGroups() {
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

	private void setupNotifications() {
		int c = 0;
		for(Notif n : DBConnect.getInvites()) {
			if(n.status > 0){
				continue;
			}
			c++;
			NotificationView notificationView = new NotificationView(n);
			TitledPane pane = new TitledPane("", notificationView);
			
			Label titleLabel = new Label("Invitation for '" + n.title + "'");
			titleLabel.setPrefWidth(140.0);
			
			Runnable onAccept = new Runnable() {
				@Override
				public void run() {
					n.accept();
					accordion.getPanes().remove(pane);
					n_count--;
					updateNotif();
					//DBConnect.acceptNotification(n);
				}
			};
			notificationView.setOnAccept(onAccept);
			
			Runnable onDecline = new Runnable() {
				@Override
				public void run() {
					n.decline();
					n_count--;
					updateNotif();
					accordion.getPanes().remove(pane);
				}
			};
			notificationView.setOnDecline(onDecline);

//			Button acceptBtn = new Button("Accept");
//			acceptBtn.setOnMouseClicked(event -> {
//				onAccept.run();
//			});
//			
//			Button declineBtn = new Button("Decline");
//			declineBtn.setOnMouseClicked(event -> {
//				onDecline.run();
//			});
			
			HBox hbox = new HBox(titleLabel);
			hbox.setAlignment(Pos.CENTER_LEFT);

//			HBox.setMargin(hbox.getChildren().get(0), new Insets(0.0, 0.0, 0.0, 10.0));
//			HBox.setMargin(hbox.getChildren().get(2), new Insets(0.0, 0.0, 0.0, 10.0));
			
			pane.setGraphic(hbox);
			pane.setAnimated(false);
			accordion.getPanes().add(pane);
		}
		n_count = c;
	}
	private int n_count = 0;
	private void updateNotif(){
		System.out.println("c: " + n_count);
		if(n_count==0){
			notificationLabel.setVisible(false);
			notificationCircle.setVisible(false);
		} else {
			notificationLabel.setVisible(true);
			notificationCircle.setVisible(true);
			if(n_count < 10){
				notificationLabel.setText(String.valueOf(n_count));
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
	
	public void redraw() {
		// HAX
		stage.setScene(null);
		stage.setScene(scene);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
