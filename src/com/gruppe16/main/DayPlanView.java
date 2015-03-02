package com.gruppe16.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.Employee;
import com.gruppe16.main.AppointmentBox.panelColors;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class DayPlanView extends ScrollPane {
	
	private Date date;
	private Pane appointmentPane;
	private Label dateTitle;
	//private HBox[] timeBoxes = new HBox[24];
	
	public DayPlanView(CalendarMain mainPane){
		setPrefSize(800, 625);
		setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		VBox bottomPane = new VBox();
		bottomPane.setPrefSize(803, 1500);
		bottomPane.setStyle("-fx-background-color: #FFFFFF;");
		
		//Create Title Pane
		Separator separator = new Separator();
		VBox title = new VBox();
		title.setPrefSize(790,60);
		title.setAlignment(Pos.TOP_CENTER);
		Label dateTitle = new Label();
		dateTitle.setFont(new Font(24));
		title.getChildren().add(dateTitle);
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
		bottomPane.getChildren().addAll(title, separator, lowerPane);
		
		//Create Hour Panes
		for(int i = 0; i < 24; i++){
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
			//timeBoxes[i] = timeBox;
		}
		
		setContent(bottomPane);
		
		this.setPannable(true);
		this.appointmentPane = appointmentPane;
		addAppointment(LocalTime.of(1, 00), LocalTime.of(3, 30), "test01", panelColors.RED);
		addAppointment(LocalTime.of(3, 00), LocalTime.of(5, 10), "test02", panelColors.GREEN);
		addAppointment(LocalTime.of(1, 00), LocalTime.of(3, 00), "test03", panelColors.TURQUOISE);
		addAppointment(LocalTime.of(2, 00), LocalTime.of(2, 30), "test04", panelColors.BLUE);
		addAppointment(LocalTime.of(7, 00), LocalTime.of(9, 00), "test05", panelColors.GREY);
		addAppointment(LocalTime.of(3, 00), LocalTime.of(5, 30), "test06", panelColors.ORANGE);
		addAppointment(LocalTime.of(6, 00), LocalTime.of(7, 30), "test07", panelColors.GREEN);
		addAppointment(LocalTime.of(12, 00), LocalTime.of(15, 00), "test08", panelColors.PURPLE);
		addAppointment(LocalTime.of(13, 15), LocalTime.of(13, 45), "test09", panelColors.BROWN);
		addAppointment(LocalTime.of(18, 00), LocalTime.of(19, 00), "test10", panelColors.BLUE);
		addAppointment(LocalTime.of(15, 10), LocalTime.of(15, 20), "test11", panelColors.GREY);
		addAppointment(LocalTime.of(22, 40), LocalTime.of(23, 53), "test12", panelColors.RED);
		addAppointment(LocalTime.of(10, 00), LocalTime.of(11, 50), "test13", panelColors.ORANGE);
		arrangeAppointments();
		requestLayout();
	}
	
	public void setDate(Date date){
		this.date = date;
		dateTitle.setText(this.date.toString());
	}
	
	public Date getDate() {
		return new Date();
	}
	
	void nextDay() {
		Date date = this.date;
		date.setDate(this.date.getDate()+1);
		setDate(date);
	}
	
	void prevDay() {
		Date date = this.date;
		date.setDate(this.date.getDate()-1);
		setDate(date);
	}
	
	public void addAppointment(LocalTime start, LocalTime end, String name, panelColors color){
		AppointmentBox appointmentBox = new AppointmentBox(start, end, name, color);
		appointmentPane.getChildren().add(appointmentBox);
	}
	
	private void arrangeAppointments() {
		//Create Array of AppointmentBoxes currently in Pane.
		ArrayList<AppointmentBox> appointmentBoxes = new ArrayList<AppointmentBox>();

		for (Node aBoxes : appointmentPane.getChildren()) {
			if(aBoxes instanceof AppointmentBox) {
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
				i++;
			}
		}
		
	}
	
	public void showAppointments(Employee e) {
		//NOTHING :D
	}
	
}

