package com.gruppe16.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
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
	private GridPane root;
	
	static String BORDER_COLOR = "#444444";
	
	CalendarView() {
		calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
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
			if(i == 0) label.setStyle("-fx-border-width: 1; -fx-border-color: " + BORDER_COLOR + " " + BORDER_COLOR + " transparent " + BORDER_COLOR + "; -fx-background-color: #CCCCFF;");
			else label.setStyle("-fx-border-width: 1; -fx-border-color: " + BORDER_COLOR + " " + BORDER_COLOR + " transparent transparent; -fx-background-color: #CCCCFF;");
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
				
				label.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
				        InnerShadow glow = new InnerShadow();
				        glow.setWidth(30);
				        glow.setHeight(30);
				        glow.setColor(Color.LIGHTBLUE);
						label.setEffect(glow);
					}
				});
				
				label.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						label.setEffect(null);
					}
				});
				
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
				String backgroundColor = "";
				String textFill = "";
				
				if(calendar.getTime().getDate() == nowDate.getDate() && calendar.getTime().getMonth() == nowDate.getMonth() && calendar.getTime().getYear() == nowDate.getYear()) {
					backgroundColor = "#CCCCFF";
					textFill = "#000000";
				}
				else {
					if(calendar.get(Calendar.MONTH) == beforeTime.getMonth()) {
						if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
							backgroundColor = "#DDDDDD";
							textFill = "#FF0000";
						}
						else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
							backgroundColor = "#DDDDDD";
							textFill = "#000000";
						}
						else{
							backgroundColor = "#FFFFFF";
							textFill = "#000000";
						}
					}
					else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						backgroundColor = "#DDDDDD";
						textFill = "#FF8888";
					}
					else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
						backgroundColor = "#DDDDDD";
						textFill = "#888888";
					}
					else {
						backgroundColor = "#FFFFFF";
						textFill = "#888888";
					}
				}
				
				String borderColor;
				if(x == 0) {
					if(y == 0)	borderColor = BORDER_COLOR + " " + BORDER_COLOR + " " + BORDER_COLOR + " " + BORDER_COLOR;
					else		borderColor = "transparent " + BORDER_COLOR + " " + BORDER_COLOR + " " + BORDER_COLOR;
				}
				else if(y == 0) {
					borderColor = BORDER_COLOR + " " + BORDER_COLOR + " " + BORDER_COLOR + " transparent";
				}
				else {
					borderColor = "transparent " + BORDER_COLOR + " " + BORDER_COLOR + " transparent";
				}
				
				label.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textFill + "; -fx-border-width: 1; -fx-border-color: " + borderColor + ";");
				calendar.add(Calendar.DATE, 1);
			}
		}
		
		calendar.setTime(beforeTime);
	}
}
