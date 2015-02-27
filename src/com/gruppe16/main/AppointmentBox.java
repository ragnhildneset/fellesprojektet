package com.gruppe16.main;

import java.time.LocalTime;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
	
	private LocalTime start, end;
	private double panelWidth;
	private double panelHeight;
	private panelColors color = panelColors.RED;
	
	public AppointmentBox(LocalTime start, LocalTime end, String name, panelColors color){
		this.start = start;
		this.end = end;
		this.color = color;
		
		int appointmentTime = (end.toSecondOfDay() - start.toSecondOfDay())/60;
		int appointmentStart = start.toSecondOfDay()/60;
		setStyle(this.color.styleDefault);
		setPrefSize(710, appointmentTime);
		relocate(0, appointmentStart);
		getChildren().add(new Label(name));
		setId(name);
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				setStyle(color.styleHover);
				panelWidth = getPrefWidth();
				panelHeight = getPrefHeight();
				toFront();
				if(panelWidth < 400) setPrefWidth(400);
				if(panelHeight < 300) setPrefHeight(300);

			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.DEFAULT);
				setStyle(color.styleDefault);
				setPrefSize(panelWidth, panelHeight);
			}
		});
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				getChildren().remove(this);
			}
		});
		}
	
	public void setColor(panelColors color){
		this.color = color;
	}
	
	public LocalTime getStart(){
		return start;
	}
	
	public LocalTime getEnd(){
		return end;
	}
	
}
