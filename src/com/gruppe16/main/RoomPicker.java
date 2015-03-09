package com.gruppe16.main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Room;
import com.gruppe16.entities.RoomReservation;

public class RoomPicker extends Application implements Initializable {
	
    @FXML private TableView<RoomReservation> roomlistTable;
    @FXML private TableColumn<RoomReservation, String> buildingNameCol;
    @FXML private TableColumn<RoomReservation, String> roonNameCol;
    @FXML private TableColumn<RoomReservation, String> roomCapCol;
    @FXML private TableColumn<RoomReservation, String> roomdescrCol;
    @FXML private TableColumn<RoomReservation, Boolean> rlist_pick;

	private static Stage stage;
	private static AddAppointment addAppointment;
	private static Room currentRoom;

    static ObservableList<Room> roomdata = FXCollections.observableArrayList(DBConnect.getRooms().values());


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}


	
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



	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
