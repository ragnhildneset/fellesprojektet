package com.gruppe16.main;

import java.util.Calendar;
import java.util.Date;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CalendarView extends GridPane {
	static String TEXT_DAY_COLOR = "#FFFFFF";
	static String TEXT_DEFAULT_COLOR = "#000000";
	static String TEXT_DEFAULT_INACTIVE_COLOR = "#888888";
	static String TEXT_SUNDAY_COLOR = "#FF0000";
	static String TEXT_SUNDAY_INACTIVE_COLOR = "#FF8888";
	static String CELL_DAY_COLOR = "#4472C4";
	static String CELL_DEFAULT_COLOR = "#FFFFFF";
	static String CELL_WEEKEND_COLOR = "#D9E2F3";
	static String CELL_CURRENT_COLOR = "#B2C9F5";
	static String BORDER_COLOR = "#8EAADB";
	
	static String[] DAY_NAMES = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

	private Calendar calendar;
	private Label[][] dayLabels = new Label[7][6];
	
	CalendarView(CalendarMain mainPane) {
		
		calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);

		setMinHeight(Double.MAX_VALUE);
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		setPrefSize(800, 600);
		
		// Day row (Mon, tue, etc)
		for(int i = 0; i < 7; ++i) {
			Label label = new Label(DAY_NAMES[i]);
			label.setFont(new Font("Arial", 18));
			label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
			label.setAlignment(Pos.CENTER);
			if(i == 0) label.setStyle("-fx-border-width: 1; -fx-border-color: " + BORDER_COLOR + " " + BORDER_COLOR + " transparent " + BORDER_COLOR + "; -fx-background-color: " + CELL_DAY_COLOR + "; -fx-text-fill: " + TEXT_DAY_COLOR + ";");
			else label.setStyle("-fx-border-width: 1; -fx-border-color: " + BORDER_COLOR + " " + BORDER_COLOR + " transparent transparent; -fx-background-color: " + CELL_DAY_COLOR + "; -fx-text-fill: " + TEXT_DAY_COLOR + ";");
			add(label, i, 0);
		}
		getRowConstraints().add(new RowConstraints(24));
		
		for(int y = 0; y < 6; ++y) {
			getRowConstraints().add(new RowConstraints(100));
			for(int x = 0; x < 7; ++x) {
				getColumnConstraints().add(new ColumnConstraints(114));
				
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
				
				label.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						mainPane.showDayPlan(calendar.getTime());
					}
				});
				
				add(label, x, y+1);
				
				dayLabels[x][y] = label;
			}
		}
		update();

		requestLayout();
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
	
	void setDate(Date date) {
		calendar.setTime(date);
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
					backgroundColor = CELL_CURRENT_COLOR;
					textFill = TEXT_DEFAULT_COLOR;
				}
				else {
					if(calendar.get(Calendar.MONTH) == beforeTime.getMonth()) {
						if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
							backgroundColor = CELL_WEEKEND_COLOR;
							textFill = TEXT_SUNDAY_COLOR;
						}
						else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
							backgroundColor = CELL_WEEKEND_COLOR;
							textFill = TEXT_DEFAULT_COLOR;
						}
						else{
							backgroundColor = CELL_DEFAULT_COLOR;
							textFill = TEXT_DEFAULT_COLOR;
						}
					}
					else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						backgroundColor = CELL_WEEKEND_COLOR;
						textFill = TEXT_SUNDAY_INACTIVE_COLOR;
					}
					else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
						backgroundColor = CELL_WEEKEND_COLOR;
						textFill = TEXT_DEFAULT_INACTIVE_COLOR;
					}
					else {
						backgroundColor = CELL_DEFAULT_COLOR;
						textFill = TEXT_DEFAULT_INACTIVE_COLOR;
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
