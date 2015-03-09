package com.gruppe16.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Appointment;

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
	private VBox[][] dayVBoxes = new VBox[7][6];
	
	CalendarView(CalendarMain mainPane) {
		
		calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);

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
			getRowConstraints().add(new RowConstraints(102));
			for(int x = 0; x < 7; ++x) {
				getColumnConstraints().add(new ColumnConstraints(114));
				
				VBox vbox = new VBox();
				vbox.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
				
				vbox.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
				        InnerShadow glow = new InnerShadow();
				        glow.setWidth(30);
				        glow.setHeight(30);
				        glow.setColor(Color.LIGHTBLUE);
				        vbox.setEffect(glow);
					}
				});
				
				vbox.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						vbox.setEffect(null);
					}
				});
				
				vbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						mainPane.showDayPlan((Date)vbox.getUserData());
					}
				});
				
				Label label = new Label();
				label.setFont(new Font("Arial", 18));
				label.setPadding(new Insets(3, 5, 0, 0));
				label.setAlignment(Pos.TOP_RIGHT);
				label.setPrefWidth(Double.MAX_VALUE);

				dayVBoxes[x][y] = vbox;
				vbox.getChildren().add(label);
				add(vbox, x, y+1);
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
		update();
	}
	
	void update() {
		Date beforeTime = calendar.getTime();
		Date nowDate = new Date();

		Map<Integer, Appointment> appointments = DBConnect.getAppointments();
		Map<String, List<Appointment>> appointmentDateMap = new HashMap<String, List<Appointment>>();
		for(Appointment a : appointments.values()) {
			try {
				if(a.getOwnerID() == Login.getCurrentUserID()) {
					Date date = java.sql.Date.valueOf(a.getAppDate());
					if(!appointmentDateMap.containsKey(date.toString())) {
						appointmentDateMap.put(date.toString(), new ArrayList<Appointment>());
					}
					appointmentDateMap.get(date.toString()).add(a);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Set calendar to the first monday
		calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.getTime(); // Bug workaround
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); calendar.getTime();

		for(int y = 0; y < 6; ++y) {
			for(int x = 0; x < 7; ++x) {
				VBox vbox = dayVBoxes[x][y];
				vbox.getChildren().remove(1, vbox.getChildren().size());
				vbox.setUserData(calendar.getTime());
				
				Label label = (Label)vbox.getChildren().get(0);
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
				
				String dateStr = new java.sql.Date(calendar.getTime().getTime()).toString();
				if(appointmentDateMap.containsKey(dateStr)) {
					int i = 0;
					for(Appointment a : appointmentDateMap.get(dateStr)) {
						Label appLabel = new Label(a.getTitle());
						appLabel.setFont(new Font("Arial Bold", 12));
						appLabel.setPrefWidth(Double.MAX_VALUE);
						appLabel.setPadding(new Insets(4, 4, 4, 4));
						VBox.setMargin(appLabel, new Insets(0, 1, 0, 0));
						if(i == appointmentDateMap.get(dateStr).size()-1 || i == 2)
							appLabel.setStyle("-fx-background-color: #CCCCFF; -fx-border-width: 1; -fx-border-color: #0000FF #0000FF #0000FF #0000FF;");
						else
							appLabel.setStyle("-fx-background-color: #CCCCFF; -fx-border-width: 1; -fx-border-color: #0000FF #0000FF transparent #0000FF;");
						vbox.getChildren().add(appLabel);
						if(i == 2 && appointmentDateMap.get(dateStr).size() > 3) {
							appLabel.setText((appointmentDateMap.get(dateStr).size()-i) + " more...");
							break;
						}
						i++;
					}
				}
				vbox.requestLayout();
				
				vbox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-width: 1; -fx-border-color: " + borderColor + ";");
				label.setStyle("-fx-text-fill: " + textFill + ";");
				calendar.add(Calendar.DATE, 1);
			}
		}
		
		calendar.setTime(beforeTime);
	}
}
