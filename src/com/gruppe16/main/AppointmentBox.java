package com.gruppe16.main;

import java.time.LocalTime;
import java.util.ArrayList;

import com.gruppe16.entities.Appointment;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class AppointmentBox extends AnchorPane {
	
	private static int PANEL_WIDTH_PARENT = 710;
	private static int PANEL_HEIGHT_PARENT = 1250;
	private static int PANEL_WIDTH_OPEN = 400;
	private static int PANEL_HEIGHT_OPEN = 200;
	
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
	private panelColors color;
	
	public AppointmentBox(Appointment appointment, panelColors color){
		this.appointment = appointment;
		int ID = this.appointment.getID();
		LocalTime start = this.appointment.getFromTime();
		LocalTime end = this.appointment.getToTime();
		String name = this.appointment.getTitle();
		this.color = color;
		
		int appointmentTime = (end.toSecondOfDay() - start.toSecondOfDay())/60;
		int appointmentStart = start.toSecondOfDay()/60;
		setPrefSize(PANEL_WIDTH_PARENT, Math.max(appointmentTime, 30));
		relocate(0, appointmentStart);
		updateLabels();
		}
	
	public void updateLabels(){
		ArrayList<Label> labels = new ArrayList<Label>();
		for(Node n : getChildren()){
			if(n instanceof Label){
				labels.add((Label) n);
			}
		}
		getChildren().removeAll(labels);
		
		setStyle(color.styleDefault);
		//Title
		Label titleLabel = new Label(appointment.getTitle());
		titleLabel.setTooltip(new Tooltip(appointment.getTitle()));
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleLabel.setPrefWidth(getPrefWidth()-10);
		titleLabel.setClip(new Rectangle(getPrefWidth()-5, 45));
		
		//TimeStamps
		Label timeLabel = new Label("From: " + appointment.getFromTime()+"\nTo: " + appointment.getToTime());
		timeLabel.setTextAlignment(TextAlignment.RIGHT);
		timeLabel.setVisible(false);
		
		//Description Title
		Label descriptionTitleLabel = new Label("Description:");
		descriptionTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		descriptionTitleLabel.setVisible(false);
		
		//Description
		Label descriptionLabel = new Label(appointment.getDescription());
		descriptionLabel.setAlignment(Pos.TOP_LEFT);
		descriptionLabel.setPrefWidth(getPrefWidth()-10);
		descriptionLabel.setPrefHeight(0);
		descriptionLabel.setWrapText(true);
		descriptionLabel.setVisible(false);
		
		//Delete Button
		Button delBtn = new Button("Delete");
		delBtn.setStyle(color.styleDefault);
		delBtn.setVisible(false);
		
		//Delete Button controller
		delBtn.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleHover);
			}
		});
		
		delBtn.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleDefault);
			}
		});
		
		delBtn.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleDefault);
			}
		});
		
		delBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleHover);
			}
		});
		
		delBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				System.out.println("Delete!");
			}
		});
		
		//Edit Button
		Button editBtn = new Button("Edit");
		editBtn.setStyle(color.styleDefault);
		editBtn.setVisible(false);
		
		//Edit Button Controller
		editBtn.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleHover);
			}
		});
		
		editBtn.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleDefault);
			}
		});
		
		editBtn.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleDefault);
			}
		});
		
		editBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleHover);
			}
		});
		
		editBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				System.out.println("Edit!");
			}
		});
		
		//Add Everything
		getChildren().addAll(titleLabel, timeLabel, descriptionTitleLabel, descriptionLabel, delBtn, editBtn);
		AnchorPane.setRightAnchor(timeLabel, 5.0);
		AnchorPane.setTopAnchor(timeLabel, 5.0);
		AnchorPane.setTopAnchor(titleLabel, 5.0);
		AnchorPane.setLeftAnchor(titleLabel, 5.0);
		AnchorPane.setTopAnchor(descriptionTitleLabel, 40.0);
		AnchorPane.setLeftAnchor(descriptionTitleLabel, 5.0);
		AnchorPane.setTopAnchor(descriptionLabel, 55.0);
		AnchorPane.setLeftAnchor(descriptionLabel, 5.0);
		AnchorPane.setBottomAnchor(delBtn, 5.0);
		AnchorPane.setRightAnchor(delBtn, 5.0);
		AnchorPane.setBottomAnchor(editBtn, 5.0);
		AnchorPane.setRightAnchor(editBtn, 75.0);
		
		//Controller
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
					if(panelWidth < PANEL_WIDTH_OPEN) setPrefWidth(PANEL_WIDTH_OPEN);
					if(panelHeight < PANEL_HEIGHT_OPEN) setPrefHeight(PANEL_HEIGHT_OPEN);
					if(panelX > PANEL_WIDTH_PARENT - PANEL_WIDTH_OPEN) setLayoutX(PANEL_WIDTH_PARENT - PANEL_WIDTH_OPEN);
					if(panelY > PANEL_HEIGHT_PARENT) setLayoutY(PANEL_HEIGHT_PARENT);
					titleLabel.setClip(null);
					titleLabel.setPrefWidth(getPrefWidth()-10);
					timeLabel.setVisible(true);
					descriptionTitleLabel.setVisible(true);
					descriptionLabel.setVisible(true);
					descriptionLabel.setPrefWidth(getPrefWidth()-10);
					descriptionLabel.setPrefHeight(getPrefHeight()-80);
					delBtn.setVisible(true);
					editBtn.setVisible(true);
				}
				else{
					active = false;
					setPrefSize(panelWidth, panelHeight);
					setLayoutX(panelX);
					setLayoutY(panelY);
					titleLabel.setPrefWidth(getPrefWidth()-10);
					timeLabel.setVisible(false);
					descriptionLabel.setPrefWidth(0);
					descriptionLabel.setPrefHeight(0);
					descriptionTitleLabel.setVisible(false);
					descriptionLabel.setVisible(false);
					delBtn.setVisible(false);
					editBtn.setVisible(false);
				}
			}
		});
		
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
		setPrefWidth(PANEL_WIDTH_PARENT);
	}
	
	public int getID(){
		return appointment.getID();
	}
	
	
	
}
