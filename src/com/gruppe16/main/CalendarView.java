package com.gruppe16.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CalendarView {
	private Calendar calendar;
	private Label[][] dayLabels = new Label[7][6];
	GridPane root;
	
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
		root = new GridPane();
		root.setMinHeight(Double.MAX_VALUE);
		root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		root.setPrefSize(800, 600);

		String[] days = {
				"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
		};
		
		// Day row (Mon, tue, etc)
		for(int i = 0; i < 7; ++i) {
			Label label = new Label(days[i]);
			label.setFont(new Font("Arial", 18));
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
				label.setFont(new Font("Arial", 18));
				label.setPadding(new Insets(3, 5, 0, 0));
				label.setAlignment(Pos.TOP_RIGHT);
				label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
				label.setStyle("-fx-border-width: 1; -fx-border-color: transparent #000000 #000000 transparent; -fx-background-color: #FFFFFF;");
				
				//root.add(pane, x, y+1);
				root.add(label, x, y+1);
				
				dayLabels[x][y] = label;
			}
		}
		update();

		root.requestLayout();
		mainView.getChildren().add(root);
		mainView.requestLayout();
	}
	
	void update() {
		Date beforeTime = calendar.getTime();
		Date nowDate = new Date();
		
		// Set calendar to the first monday
		calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.getTime(); // Bug workaround
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); calendar.getTime();

		for(int y = 0; y < 6; ++y) {
			for(int x = 0; x < 7; ++x) {
				Label label = dayLabels[x][y];
				label.setText(Integer.toString(calendar.get(Calendar.DATE)));
				if(calendar.getTime().getDate() == nowDate.getDate() && calendar.getTime().getMonth() == nowDate.getMonth() && calendar.getTime().getYear() == nowDate.getYear()) {
					label.setTextFill(new Color(1.0, 1.0, 1.0, 1.0));
					label.setStyle("-fx-border-width: 1; -fx-border-color: transparent #000000 #000000 transparent; -fx-background-color: #CCCCFF;");
				}
				else {
					if(calendar.get(Calendar.MONTH) == beforeTime.getMonth()) {
						if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
							label.setTextFill(new Color(1.0, 0.0, 0.0, 1.0));
						}
						else {
							label.setTextFill(new Color(0.0, 0.0, 0.0, 1.0));
						}
					}
					else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						label.setTextFill(new Color(1.0, 0.5, 0.5, 1.0));
					}
					else {
						label.setTextFill(new Color(0.5, 0.5, 0.5, 1.0));
					}
				}
				calendar.add(Calendar.DATE, 1);
			}
		}
		
		calendar.setTime(beforeTime);
	}
}
