package com.gruppe16.main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.gruppe16.entities.Notif;

public class NotificationView extends VBox {
	private Runnable onAccept;
	private Runnable onDecline;
	
	public NotificationView(Notif notification) {
		setPrefWidth(300.0);
		
		Label label = null;
		HBox hbox = null;

		label = new Label(); label.setText("Title:"); label.setPrefWidth(100.0);  label.setMinWidth(100.0);
		hbox = new HBox(label, new Label(notification.title));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(10.0, 10.0, 10.0, 10.0));

		label = new Label(); label.setText("Description:"); label.setPrefWidth(100.0); label.setMinWidth(100.0);
		Label descLabel = new Label(notification.description);
		descLabel.setWrapText(true);
		hbox = new HBox(label, descLabel);
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(0.0, 10.0, 10.0, 10.0));

		label = new Label(); label.setText("Date:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.date));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(10.0, 10.0, 10.0, 10.0));
		
		label = new Label(); label.setText("From:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.fromTime));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(0.0, 10.0, 10.0, 10.0));

		label = new Label(); label.setText("To:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.toTime));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(0.0, 10.0, 10.0, 10.0));

		Button acceptBtn = new Button("Accept");
		HBox.setMargin(acceptBtn, new Insets(0.0, 10.0, 0.0, 60.0));
		acceptBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				notification.accept();
				onAccept.run();
			}
		});
		
		Button declineBtn = new Button("Decline");
		declineBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				notification.decline();
				onDecline.run();
			}
		});

		label = new Label(); label.setText(notification.owner); label.setPrefWidth(150.0);
		hbox = new HBox(label, acceptBtn, declineBtn);
		hbox.setAlignment(Pos.CENTER_LEFT);
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(10.0, 10.0, 10.0, 10.0));
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				requestFocus();
			}
		});
	}

	void setOnAccept(Runnable onAccept) {
		this.onAccept = onAccept;
	}
	
	void setOnDecline(Runnable onDecline) {
		this.onDecline = onDecline;
	}
}
