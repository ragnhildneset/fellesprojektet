package com.gruppe16.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CalendarView {
	private Calendar calendar;
	Label[][] dayLabels = new Label[7][6];
	
	CalendarView() {
		calendar = Calendar.getInstance();
	}
	
	void nextMonth() {
		calendar.add(Calendar.MONTH, 1);
		update();
	}
	
	void prevMonth() {
		calendar.add(Calendar.MONTH, -1);
		update();
	}
	
	int getMonth() {
		return calendar.get(Calendar.MONTH);
	}
	
	int getYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	void setup(Pane mainView) {
		GridPane root = new GridPane();
		root.setMinHeight(Double.MAX_VALUE);
		root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		root.setPrefSize(800, 600);

		String[] days = {
				"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
		};
		
		// Day row (Mon, tue, etc)
		for(int i = 0; i < 7; ++i) {
			Label label = new Label(days[i]);
			label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
			label.setAlignment(Pos.CENTER);
			label.setStyle("-fx-border-width: 1; -fx-border-color:  transparent #000000 #000000 transparent; -fx-background-color: #CCCCFF;");
			root.add(label, i, 0);
		}
		root.getRowConstraints().add(new RowConstraints(24));
		
		for(int y = 0; y < 6; ++y) {
			root.getRowConstraints().add(new RowConstraints(100));
			for(int x = 0; x < 7; ++x) {
				root.getColumnConstraints().add(new ColumnConstraints(114));
				
				Label label = new Label();
				label.setAlignment(Pos.TOP_RIGHT);
				label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
				label.setStyle("-fx-border-width: 1; -fx-border-color: transparent #000000 #000000 transparent; -fx-background-color: #FFFFFF;");
				root.add(label, x, y+1);
				dayLabels[x][y] = label;
				dayLabels[x][y].setText(x + ", " + y);
			}
		}
		
		//update();
		
		mainView.getChildren().add(root);
	}
	
	void update() {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int currentMonth = calendar.get(Calendar.MONTH);

		int startDay = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) - 1) % 12);
		int daysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.set(Calendar.MONTH, currentMonth);

		int i = -startDay;
		for(int y = 0; y < 6; ++y) {
			for(int x = 0; x < 7; ++x) {
				Label label = dayLabels[x][y];
				label.setText("Test");
				/*
				label.setText(Integer.toString((i % daysInMonth) + 1));
				if(i >= daysInMonth)
				{
					label.setTextFill(new Color(0.5, 0.5, 0.5, 1.0));
				}else if(i < 0) {
					label.setText(Integer.toString(daysInPrevMonth + i + 1));
					label.setTextFill(new Color(0.5, 0.5, 0.5, 1.0));
				}
				i++;*/
			}
		}
	}
}
