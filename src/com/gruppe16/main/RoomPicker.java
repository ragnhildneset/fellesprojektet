package com.gruppe16.main;

import java.io.IOException;
import java.util.Collection;

import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Room;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class RoomPicker {
	private static Stage stage;
	private static AddAppointment addAppointment;
	private static Room currentRoom;
	
	public static void start(Stage stage, Window owner, AddAppointment addApp) throws IOException {
		RoomPicker.stage = stage;
		RoomPicker.addAppointment = addApp;
		//RoomPicker.currentRoom = addApp.getRoom();
		Scene scene = new Scene((Parent)FXMLLoader.load(EmployeePicker.class.getResource("/com/gruppe16/main/RoomPicker.fxml")));
		stage.setResizable(false);
		stage.initStyle(StageStyle.UTILITY);
		if(owner != null) {
			stage.initOwner(owner);
			stage.initModality(Modality.WINDOW_MODAL);
		}
		stage.setScene(scene);
		stage.show();
	}
}