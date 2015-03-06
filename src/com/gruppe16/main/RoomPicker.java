package com.gruppe16.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Room;

public class RoomPicker extends Application implements Initializable {
	
    @FXML private TableView<Room> roomlistTable;
    @FXML private TableColumn<Room, String> roomIDCol;
    @FXML private TableColumn<Room, String> capacityCol;
    @FXML private TableColumn<Room, String> roomnameCol;
    @FXML private TableColumn<Room, String> roomdescrCol;
    @FXML private TableColumn<Room, String> roombuildingidCol;
    @FXML private TableColumn<Room, Boolean> rlist_pick;
    

    static ObservableList<Room> roomdata = FXCollections.observableArrayList(DBConnect.getRooms().values());


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	
}
