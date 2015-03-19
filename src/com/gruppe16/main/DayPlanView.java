package com.gruppe16.main;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.AppointmentAndEmployee;
import com.gruppe16.entities.Employee;
import com.gruppe16.main.AppointmentBox.PanelColors;


public class DayPlanView extends VBox implements CalendarViewInterface {
	
	private static String[] DAY_NAMES = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
	private Date date;
	private Pane appointmentPane;
	private Label dateTitle;
	private CalendarMain mPane;
	
	public DayPlanView(CalendarMain mainPane){
		setPrefSize(800, 625);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		mPane = mainPane;
		
		VBox bottomPane = new VBox();
		bottomPane.setPrefSize(800, 1500);
		bottomPane.setStyle("-fx-background-color: #FFFFFF;");
		
		//Create Title Pane
		VBox title = new VBox();
		title.setPrefSize(790,60);
		title.setAlignment(Pos.TOP_CENTER);
		Label dateTitle = new Label();
		dateTitle.setFont(new Font(24));
		title.getChildren().add(dateTitle);
		title.setStyle("-fx-background-color: #FFFFFF;");
		this.dateTitle = dateTitle;
		
		//Create other Panes for Layout
		HBox lowerPane = new HBox();
		lowerPane.setPrefSize(790, 1500);
		VBox leftPane = new VBox();
		leftPane.setPrefSize(80, 1500);
		leftPane.setPadding(new Insets(0,5,0,0));
		VBox rightPane = new VBox();
		rightPane.setPrefSize(710, 1500);
		Pane appointmentPane = new Pane();
		appointmentPane.setPrefSize(710,1500);
		rightPane.getChildren().add(appointmentPane);
		lowerPane.getChildren().addAll(leftPane, rightPane);
		bottomPane.getChildren().add(lowerPane);
		
		//Create Hour Panes
		for(int i = 0; i < 24; i++){
			final int hour = i;
			VBox timeBoxLeft = new VBox();
			timeBoxLeft.setPrefSize(80, 60);;
			timeBoxLeft.setAlignment(Pos.TOP_RIGHT);
			Label time = new Label(i + ":00");
			time.setFont(new Font(20));
			time.setPadding(new Insets(3, 5, 0, 0));
			timeBoxLeft.getChildren().add(time);
			leftPane.getChildren().addAll(timeBoxLeft);
			
			timeBoxLeft.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					timeBoxLeft.setStyle("-fx-background-color: #CCCCFF; -fx-border-width: 1;");
				}
			});
			
			timeBoxLeft.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					timeBoxLeft.setStyle("-fx-background-color: transparent;");
				}
			});
			
			timeBoxLeft.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent evnet) {
					try {
						Stage newStage = new Stage();
						newStage.setOnHidden(new EventHandler<WindowEvent>() {
							@Override
							public void handle(WindowEvent event) {
								showAppointments(CalendarMain.getGroupAppointments());
								mPane.redraw();
							}
						});
						Date dateTime = getDate();
						dateTime.setHours(hour);
						AddAppointment.start(newStage, mPane.getScene().getWindow(), dateTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		scrollPane.setContent(bottomPane);
		scrollPane.setPannable(true);
		
		getChildren().addAll(title, scrollPane);
		this.appointmentPane = appointmentPane;
		requestLayout();
	}
	
	public void setDate(Date date){
		this.date = date;
		this.date.setHours(0);
		dateTitle.setText(DAY_NAMES[this.date.getDay()]);
	}
	
	public Date getDate() {
		return date;
	}
	
	public void incDate() {
		Date date = this.date;
		date.setDate(this.date.getDate()+1);
		setDate(date);
		showAppointments(CalendarMain.getGroupAppointments());
	}
	
	public void decDate() {
		Date date = this.date;
		date.setDate(this.date.getDate()-1);
		setDate(date);
		showAppointments(CalendarMain.getGroupAppointments());
	}
	
	public CalendarMain getMainPane() {
		return mPane;
	}
	
	public void addAppointment(Appointment appointment, AppointmentAndEmployee appointmentAndEmployee){
		AppointmentBox appointmentBox = new AppointmentBox(appointment, appointmentAndEmployee, this);
		appointmentPane.getChildren().add(appointmentBox);
	}
	
	public void addAppointment(Appointment appointment){
		AppointmentBox appointmentBox = new AppointmentBox(appointment, this);
		appointmentPane.getChildren().add(appointmentBox);
	}
	
	private void arrangeAppointments() {
		//Create Array of AppointmentBoxes currently in Pane.
		ArrayList<AppointmentBox> appointmentBoxes = new ArrayList<AppointmentBox>();

		for (Node aBoxes : appointmentPane.getChildren()) {
			if(aBoxes instanceof AppointmentBox) {
				((AppointmentBox) aBoxes).toDefaultSize();
				appointmentBoxes.add((AppointmentBox) aBoxes);
			}
		}
		
		//Create Array of overlapping Appointments
		ArrayList<ArrayList<AppointmentBox>> overlapArray = new ArrayList<ArrayList<AppointmentBox>>();
		while (!appointmentBoxes.isEmpty()){
			AppointmentBox a = appointmentBoxes.remove(0);
			boolean newBox = true;
			for (AppointmentBox aNext : appointmentBoxes) {
				if(a.getEnd().isAfter(aNext.getStart()) ^ a.getStart().isAfter(aNext.getEnd())) {
					
					//Check if in overlapArray already
					newBox = false;
					boolean contained = false;
					for (ArrayList<AppointmentBox> o : overlapArray) {
						if(o.contains(a)){
							if(!o.contains(aNext)){
								o.add(aNext);
							}
							contained = true;
							break;
						}
						else if (o.contains(aNext)){
							if(!o.contains(a)){
								o.add(a);
							}
							contained = true;
							break;
						}
					}
					//Add new array if not in overlapArray
					if(!contained){
						ArrayList<AppointmentBox> overlappingBoxes = new ArrayList<AppointmentBox>();
						overlappingBoxes.add(a);
						overlappingBoxes.add(aNext);
						overlapArray.add(overlappingBoxes);
					}
				}
				else if(a.getDuration() < 40 && aNext.getDuration() < 40) {
					if(a.getStart().plusMinutes(40).isAfter(aNext.getStart()) ^ a.getStart().isAfter(aNext.getStart().plusMinutes(40))) {
						//Check if in overlapArray already
						newBox = false;
						boolean contained = false;
						for (ArrayList<AppointmentBox> o : overlapArray) {
							if(o.contains(a)){
								if(!o.contains(aNext)){
									o.add(aNext);
								}
								contained = true;
								break;
							}
							else if (o.contains(aNext)){
								if(!o.contains(a)){
									o.add(a);
								}
								contained = true;
								break;
							}
						}
						if(!contained){
							ArrayList<AppointmentBox> overlappingBoxes = new ArrayList<AppointmentBox>();
							overlappingBoxes.add(a);
							overlappingBoxes.add(aNext);
							overlapArray.add(overlappingBoxes);
						}
					}
				}
				else if(a.getDuration() < 40) {
					if(a.getStart().plusMinutes(40).isAfter(aNext.getStart()) ^ a.getStart().plusMinutes(40).isAfter(aNext.getEnd())) {
						//Check if in overlapArray already
						newBox = false;
						boolean contained = false;
						for (ArrayList<AppointmentBox> o : overlapArray) {
							if(o.contains(a)){
								if(!o.contains(aNext)){
									o.add(aNext);
								}
								contained = true;
								break;
							}
							else if (o.contains(aNext)){
								if(!o.contains(a)){
									o.add(a);
								}
								contained = true;
								break;
							}
						}
						if(!contained){
							ArrayList<AppointmentBox> overlappingBoxes = new ArrayList<AppointmentBox>();
							overlappingBoxes.add(a);
							overlappingBoxes.add(aNext);
							overlapArray.add(overlappingBoxes);
						}
					}
				}
				else if(aNext.getDuration() < 40) {
					if(a.getEnd().isAfter(aNext.getStart()) ^ a.getStart().isAfter(aNext.getStart().plusMinutes(40))) {
						//Check if in overlapArray already
						newBox = false;
						boolean contained = false;
						for (ArrayList<AppointmentBox> o : overlapArray) {
							if(o.contains(a)){
								if(!o.contains(aNext)){
									o.add(aNext);
								}
								contained = true;
								break;
							}
							else if (o.contains(aNext)){
								if(!o.contains(a)){
									o.add(a);
								}
								contained = true;
								break;
							}
						}
						if(!contained){
							ArrayList<AppointmentBox> overlappingBoxes = new ArrayList<AppointmentBox>();
							overlappingBoxes.add(a);
							overlappingBoxes.add(aNext);
							overlapArray.add(overlappingBoxes);
						}
					}
				}
			}
			
			
			//new Overlap array if no conflicts
			if (newBox) {
				boolean contains = false;
				for (ArrayList<AppointmentBox> o : overlapArray) {
					if(o.contains(a)){
						contains = true;
						break;
						}
				}
				if (!contains){
					ArrayList<AppointmentBox> overlappingBoxes = new ArrayList<AppointmentBox>();
					overlappingBoxes.add(a);
					overlapArray.add(overlappingBoxes);
				}
			}
		}
		
		
		
		
		while (!overlapArray.isEmpty()){
			ArrayList<AppointmentBox> overlaps = overlapArray.remove(0);
			int i = 0;
			for(AppointmentBox app : overlaps){
				double newWidth = app.getPrefWidth()/overlaps.size();
				app.setPrefWidth(newWidth);
				app.setLayoutX(newWidth*i);
				app.updateLabels();
				i++;
			}
		}
	}
	
	public void showAppointments(Collection<Appointment> appointments) {
		showAppointments(appointments, false);
	}
	
	public void showAppointments(Collection<Appointment> appointmentss, boolean group) {
		removeAppointments();
		ArrayList<AppointmentAndEmployee> appAndEmp = DBConnect.getAppointmentAndEmployee();
		LocalDate date = this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		for(Appointment a : appointmentss){
			if(date.equals(a.getAppDate())) appointments.add(a);
		}
		if(!group){
			for (AppointmentAndEmployee currentApp : appAndEmp) {
				if(currentApp.getStatus() == 1){
					int appID = currentApp.getAppointmentID();
					for (Appointment app : appointments){
						if(appID == app.getID() && app.getAppDate().equals(date)){
							boolean notContained = true;
							for (Node aBoxes : appointmentPane.getChildren()) {
								if(aBoxes instanceof AppointmentBox) {
									if(((AppointmentBox) aBoxes).getID() == app.getID()){
										notContained = false;
										break;
									}
								}
							}	
							if(notContained) addAppointment(app, currentApp);
						}
					}
					
				}
			}
			arrangeAppointments();
		}
		else{
			for (Appointment app : appointments){
				if(app.getAppDate().equals(date)){
					boolean notContained = true;
					for (Node aBoxes : appointmentPane.getChildren()) {
						if(aBoxes instanceof AppointmentBox) {
							if(((AppointmentBox) aBoxes).getID() == app.getID()){
								notContained = false;
								break;
							}
						}
					}
					AppointmentAndEmployee AAE = DBConnect.getAppointmentAndEmployee(app, Login.getCurrentUser());
					if(notContained) if(AAE == null) addAppointment(app); else addAppointment(app, AAE);
				}
			}
			arrangeAppointments();
		}
	}

	
	private void removeAppointments(){
		ArrayList<AppointmentBox> appointmentBoxes = new ArrayList<AppointmentBox>();
		for (Node aBoxes : appointmentPane.getChildren()) {
			if(aBoxes instanceof AppointmentBox) {
				appointmentBoxes.add((AppointmentBox) aBoxes);
			}
		}
		appointmentPane.getChildren().removeAll(appointmentBoxes);
	}
	
	
}

