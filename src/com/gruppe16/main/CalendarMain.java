package com.gruppe16.main;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CalendarMain extends Application implements Initializable {
	
	@FXML
	private Pane mainView;
	
	@FXML
	private VBox calendarGroupPane;
	
	@FXML
	private Button nextMonthBtn;
	
	@FXML
	private Button prevMonthBtn;
	
	@FXML
	private Label monthLabel;
	
	@FXML
	private Label yearLabel;

	private CalendarView calendarView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	Scene scene;
	Stage primaryStage;
	
	// HAX
	public void redraw() {
		primaryStage.setScene(null);
		primaryStage.setScene(scene);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL url = getClass().getResource("/com/gruppe16/main/mainPane.fxml");
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(url);
		fxmlLoader.setController(this);
		try {
			scene = new Scene((Parent)fxmlLoader.load(url.openStream()), 970, 740);
			scene.getRoot().setStyle("-fx-background-color: linear-gradient(#FFFFFF, #EEEEEE)");
			this.primaryStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

			redraw();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] months = {
				"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		};

		calendarView = new CalendarView();
		calendarView.setup(mainView);
		
		nextMonthBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				calendarView.nextMonth();
				monthLabel.setText(months[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});

		prevMonthBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				calendarView.prevMonth();
				monthLabel.setText(months[calendarView.getMonth()]);
				yearLabel.setText(Integer.toString(calendarView.getYear()));
				redraw();
			}
		});
		
		monthLabel.setText(months[calendarView.getMonth()]);
		yearLabel.setText(Integer.toString(calendarView.getYear()));
		
		for(int i = 0; i <= 9; ++i){
			Label l = new Label(i == 9 ? "+ Add calendar" : "Group "+(i+1));
			l.setAlignment(Pos.CENTER);
			l.setPrefWidth(100);
			l.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					scene.setCursor(Cursor.HAND);
					l.setStyle("-fx-background-color: #AAAAAA;");
				}
			});
			l.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					scene.setCursor(Cursor.DEFAULT);
					l.setStyle("-fx-background-color: transparent;");
				}
			});
			calendarGroupPane.getChildren().add(l);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
