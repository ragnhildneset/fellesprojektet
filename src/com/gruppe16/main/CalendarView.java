package com.gruppe16.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

enum Month {
	JANUARY(0, 31),
	FEBRUARY(1, 28),
	MARCH(2, 31),
	APRIL(3, 30),
	MAY(4, 31),
	JUNE(5, 30),
	JULY(6, 31),
	AUGUST(7, 31),
	SEPTEMBER(8, 30),
	OCTOBER(9, 31),
	NOVEMBER(10, 30),
	DESEMBER(11, 31);
	
	private final int value;
	private final int dayCount;
	
	private Month(int value, int dayCount) {
		this.value = value;
		this.dayCount = dayCount;
	}

	public int getValue() {
		return value;
	}
	
	public int getDayCount() {
		return dayCount;
	}
}

public class CalendarView extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		GridPane root = new GridPane();
		root.setMinHeight(Double.MAX_VALUE);
		root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		root.setPrefSize(800, 600);
		
		Scene scene = new Scene(root, 800, 600);
		arg0.setScene(scene);
		arg0.show();

		String[] days = {
				"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
		};
		
		for(int i = 0; i < 7; ++i) {
			Label label = new Label(days[i]);
			label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
			label.setAlignment(Pos.CENTER);
			label.setStyle("-fx-border-width: 1; -fx-border-color:  transparent #000000 #000000 transparent;");
			root.add(label, i, 0);
		}
		root.getRowConstraints().add(new RowConstraints(24));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, Calendar.AUGUST);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		int startDay = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) - 1) % 12);
		int daysInPrevMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		int i = -startDay;
		for(int y = 0; y < 6; ++y) {
			root.getRowConstraints().add(new RowConstraints(100));
			for(int x = 0; x < 7; ++x) {
				root.getColumnConstraints().add(new ColumnConstraints(114));
				
				Label label = new Label(Integer.toString((i % daysInMonth) + 1));
				if(i < 0) {
					label.setText(Integer.toString(daysInPrevMonth + i + 1));
				}
				i++;
				label.setAlignment(Pos.TOP_RIGHT);
				label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
				label.setStyle("-fx-border-width: 1; -fx-border-color: transparent #000000 #000000 transparent;");
				root.add(label, x, y+1);
			}
		}
	}
}
