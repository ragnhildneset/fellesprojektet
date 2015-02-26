package com.gruppe16.main;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class DayPlanView{
	
	void setup(Pane mainView){
		ScrollPane root = new ScrollPane();
		root.setPrefSize(800, 625);
		root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(800, 2000);
		flowPane.setOrientation(Orientation.VERTICAL);
		flowPane.setStyle("-fx-background-color: #FFFFFF;");

		for(int i = 0; i < 24; i++){
			Separator sep = new Separator();
			HBox hBox = new HBox();
			hBox.setId("hour_" + i);
			hBox.setPrefSize(800, 80);
			HBox hBoxLeft = new HBox();
			hBoxLeft.setPrefWidth(80);
			hBoxLeft.setAlignment(Pos.TOP_RIGHT);
			Label time = new Label(i + ":00");
			time.setFont(new Font(20));
			hBoxLeft.getChildren().add(time);
			hBox.getChildren().add(hBoxLeft);
			flowPane.getChildren().addAll(hBox, sep);
			
			hBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
			        hBox.setStyle("-fx-background-color: #CCCCFF;");
				}
			});
			
			hBox.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					hBox.setStyle("-fx-background-color: transparent;");
				}
			});
		}
		
		root.setContent(flowPane);
		
		root.requestLayout();
		mainView.getChildren().add(root);
		mainView.requestLayout();
	}
	
}
