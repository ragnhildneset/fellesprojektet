package com.gruppe16.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.gruppe16.entities.Appointment;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
	private LocalDate date;
	private Pane appointmentPane;
	//private HBox[] timeBoxes = new HBox[24];
	
	public DayPlanView(LocalDate date){
		this.date = date;
		setPrefSize(800, 625);
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		VBox bottomPane = new VBox();
		bottomPane.setPrefSize(790, 1500);
		bottomPane.setStyle("-fx-background-color: #FFFFFF;");
		
		//Create Title Pane
		Separator separator = new Separator();
		VBox title = new VBox();
		title.setPrefSize(790,60);
		title.setAlignment(Pos.TOP_CENTER);
		Label dateTitle = new Label(this.date.toString());
		dateTitle.setFont(new Font(24));
		title.getChildren().add(dateTitle);
		
		//Create other Panes for Layout
		HBox lowerPane = new HBox();
		lowerPane.setPrefSize(790, 1500);
		VBox leftPane = new VBox();
		leftPane.setPrefSize(80, 1500);
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
		
/*		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(790, 1580);
		flowPane.setOrientation(Orientation.VERTICAL);
		flowPane.setStyle("-fx-background-color: #FFFFFF;");
		
		Separator separator = new Separator();
		HBox title = new HBox();
		title.setPrefSize(790,60);
		title.setAlignment(Pos.TOP_CENTER);
		Label dateTitle = new Label(this.date.toString());
		dateTitle.setFont(new Font(24));
		title.getChildren().add(dateTitle);
		flowPane.getChildren().addAll(title, separator);
		
		for(int i = 0; i < 24; i++){
			Separator sep = new Separator();
			HBox timeBox = new HBox();
			timeBox.setId("hour_" + i);
			timeBox.setPrefSize(790, 60);
			HBox timeBoxLeft = new HBox();
			timeBoxLeft.setPrefWidth(80);
			timeBoxLeft.setAlignment(Pos.TOP_RIGHT);
			Label time = new Label(i + ":00");
			time.setFont(new Font(20));
			time.setPadding(new Insets(3, 5, 0, 0));
			timeBoxLeft.getChildren().add(time);
			HBox timeBoxRight = new HBox();
			timeBoxRight.setPrefWidth(710);
			timeBox.getChildren().addAll(timeBoxLeft, timeBoxRight);
			flowPane.getChildren().addAll(timeBox, sep);
			
			timeBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					timeBox.setStyle("-fx-background-color: #CCCCFF;");
				}
			});
			
			timeBox.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					timeBox.setStyle("-fx-background-color: transparent;");
				}
			});
			timeBoxes[i] = timeBox;
		}
		
		setContent(flowPane);		*/
		this.setPannable(true);
		this.appointmentPane = appointmentPane;
		requestLayout();
	}
	
	public void addAppointment(LocalTime start, LocalTime end){
		
		int appointmentTime = (end.toSecondOfDay() - start.toSecondOfDay())/60;
		int appointmentStart = start.toSecondOfDay()/60;
		
		Pane appointmentBox = new Pane();
		appointmentBox.setPrefSize(710, appointmentTime);
		appointmentBox.relocate(0, appointmentStart);
		appointmentBox.setStyle("-fx-background-color: #FFCCCC; -fx-border-width: 1; -fx-border-color: #FF0000;");
		appointmentBox.getChildren().add(new Label("App_Title"));
		appointmentPane.getChildren().add(appointmentBox);
		//HBox.setHgrow(appointmentBox, Priority.ALWAYS);
		
		appointmentBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				appointmentBox.setCursor(Cursor.HAND);
				appointmentBox.setStyle("-fx-background-color: #FFAAAA; -fx-border-width: 1; -fx-border-color: #FF0000;");
			}
		});
		
		appointmentBox.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				appointmentBox.setCursor(Cursor.DEFAULT);
				appointmentBox.setStyle("-fx-background-color: #FFCCCC; -fx-border-width: 1; -fx-border-color: #FF0000;");
			}
		});
		
		appointmentBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				appointmentBox.setCursor(Cursor.HAND);
				appointmentPane.getChildren().remove(appointmentBox);
			}
		});
	}
	
	
}

