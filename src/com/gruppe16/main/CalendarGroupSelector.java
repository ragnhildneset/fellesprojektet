package com.gruppe16.main;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CalendarGroupSelector {
	private Scene scene;
	//private boolean[] groupSelected = new boolean[10];
	
	CalendarGroupSelector(Scene scene) {
		this.scene = scene;
	}
	
	public void setup(Pane root) {
		for(int i = 0; i <= 9; ++i){
			Label l = new Label(i == 9 ? "+ Add calendar" : "Group "+(i+1));
			l.setAlignment(Pos.CENTER);
			l.setPrefWidth(120);
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
			l.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if(l.getEffect() == null) {
						InnerShadow glow = new InnerShadow();
						glow.setColor(Color.RED);
						l.setEffect(glow);
					}
					else {
						l.setEffect(null);
					}
				}
			});
			root.getChildren().add(l);
		}
	}
}
