package com.gruppe16.main;

import java.time.LocalTime;

import com.gruppe16.entities.Appointment;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class AppointmentBox extends Pane {
	
	public enum panelColors {
	    RED("#FFCCCC", "#FFAAAA", "#FF0000"),
	    GREEN("#CCFFCC", "#AAFFAA", "#00FF00"),
	    BLUE("#CCCCFF", "#AAAAFF", "#0000FF"),
	    YELLOW("#FFFFCC", "#FFFFAA", "#DDDD00"),
	    BROWN("D8C2A0", "#C29653", "#7E4C00"),
	    PURPLE("#FFCCFF", "#FFAAFF", "#FF00FF"),
	    ORANGE("#FFB27F", "#FF9900", "#DE8500"),
	    TURQUOISE("#CCFFFF", "#AAFFFF", "#00FFFF"),
	    GREY("#CCCCCC", "#AAAAAA", "#000000")
	    ;
	    
	    private String styleHover;
	    private String styleDefault;
	    panelColors(String cMain, String cSecondary, String cBorder){
	    	styleDefault = "-fx-background-color: " + cMain + "; -fx-border-width: 1; -fx-border-color: " + cBorder + ";";
	    	styleHover = "-fx-background-color: " + cSecondary + "; -fx-border-width: 1; -fx-border-color: " + cBorder + ";";
	    }
	    
	    public String getDefault(){
	    	return styleDefault;
	    }
	    
	    public String getHover(){
	    	return styleHover;
	    }
	}
	
	private Appointment appointment;
	private double panelWidth;
	private double panelHeight;
	private double panelX;
	private double panelY;
	private boolean active = false;
	private panelColors color = panelColors.RED;
	
	public AppointmentBox(Appointment appointment, panelColors color){
		this.appointment = appointment;
		int ID = this.appointment.getID();
		LocalTime start = this.appointment.getFromTime();
		LocalTime end = this.appointment.getToTime();
		String name = this.appointment.getTitle();
		
		int appointmentTime = (end.toSecondOfDay() - start.toSecondOfDay())/60;
		int appointmentStart = start.toSecondOfDay()/60;
		setStyle(this.color.styleDefault);
		setPrefSize(710, Math.max(appointmentTime, 30));
		relocate(0, appointmentStart);
		Label titleLabel = new Label("Appointment: " + name);
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		Label descriptionTitleLabel = new Label("Description");
		descriptionTitleLabel.setLayoutY(25);
		descriptionTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		descriptionTitleLabel.setVisible(false);
		Label descriptionLabel = new Label(appointment.getDescription());
		descriptionLabel.setLayoutY(40);
		descriptionLabel.setVisible(false);
		getChildren().addAll(titleLabel, descriptionTitleLabel, descriptionLabel);
		setId(name);

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				setStyle(color.styleHover);
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.DEFAULT);
				setStyle(color.styleDefault);
			}
		});
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				if(!active){
					active = true;
					panelWidth = getPrefWidth();
					panelHeight = getPrefHeight();
					panelX = getLayoutX();
					panelY = getLayoutY();
					toFront();
					if(panelWidth < 400) setPrefWidth(400);
					if(panelHeight < 200) setPrefHeight(200);
					if(panelX > 310) setLayoutX(310);
					if(panelY > 1250) setLayoutY(1250);
					descriptionTitleLabel.setVisible(true);
					descriptionLabel.setVisible(true);
				}
				else{
					active = false;
					setPrefSize(panelWidth, panelHeight);
					setLayoutX(panelX);
					setLayoutY(panelY);
					descriptionTitleLabel.setVisible(false);
					descriptionLabel.setVisible(false);
				}
			}
		});
		}
	
	public void setColor(panelColors color){
		this.color = color;
	}
	
	public LocalTime getStart(){
		return appointment.getFromTime();
	}
	
	public LocalTime getEnd(){
		return appointment.getToTime();
	}
	
	public void toDefaultSize(){
		setPrefWidth(710);
	}
	
	public int getID(){
		return appointment.getID();
	}
	
}
