package com.gruppe16.main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.gruppe16.database.DBConnect;

class Notification {
	String getTitle() { return "Test"; }
	String getDescription() { return "A description"; }
	String getDate() { return "Date"; }
	String getFrom() { return "01:00"; }
	String getTo() { return "03:00"; }
	String getOwner() { return "Pending"; }
}

public class NotificationView extends VBox {
	private Runnable onAccept;
	private Runnable onDecline;
	
	public NotificationView(Notification notification) {
		setPrefWidth(300.0);
		
		Label label = null;
		HBox hbox = null;

		label = new Label(); label.setText("Title:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.getTitle()));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(10.0, 10.0, 10.0, 10.0));

		label = new Label(); label.setText("Description:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.getDescription()));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(0.0, 10.0, 10.0, 10.0));

		label = new Label(); label.setText("Date:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.getDate()));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(10.0, 10.0, 10.0, 10.0));
		
		label = new Label(); label.setText("From:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.getFrom()));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(0.0, 10.0, 10.0, 10.0));

		label = new Label(); label.setText("To:"); label.setPrefWidth(100.0);
		hbox = new HBox(label, new Label(notification.getTo()));
		getChildren().add(hbox);
		VBox.setMargin(hbox, new Insets(0.0, 10.0, 10.0, 10.0));

		Button acceptBtn = new Button("Accept");
		HBox.setMargin(acceptBtn, new Insets(0.0, 10.0, 0.0, 60.0));
		acceptBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/*String q = "update Request\n"
						+"set status = 1\n"
						+"where employeeid = "+Login.getCurrentUserID()+";";
				try{
					PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
					p.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// Remove this*/
				onAccept.run();
			}
		});
		
		Button declineBtn = new Button("Decline");
		declineBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/*String q = "update Request\n"
						+"set status = 2\n"
						+"where employeeid = "+Login.getCurrentUserID()+";";
				try{
					PreparedStatement p = DBConnect.getConnection().prepareStatement(q);
					p.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Platform.exit();*/
				onDecline.run();
			}
		});

		label = new Label(); label.setText(notification.getOwner()); label.setPrefWidth(100.0);
		hbox = new HBox(label, acceptBtn, declineBtn);
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
