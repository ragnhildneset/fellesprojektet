package com.gruppe16.main;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Building;
import com.gruppe16.entities.Room;
import com.gruppe16.entities.RoomReservation;
import com.gruppe16.util.Tuple;

public class RoomPicker implements Initializable {
	
    @FXML private TableView<Room> roomlistTable;
    @FXML private TableColumn<Building, String> buildingNameCol;
    @FXML private TableColumn<Room, String> roomNameCol;
    @FXML private TableColumn<Room, String> roomCapCol;
    @FXML private TableColumn<Room, String> roomdescrCol;
    @FXML private TableColumn<Room, Boolean> rlist_pick;

	private static Stage stage;
	private static AddAppointment addAppointment;
	private static Room currentRoom;
	private ArrayList<Tuple> availableRooms;

    static ObservableList<Room> roomdata = FXCollections.observableArrayList(DBConnect.getRooms().values());


	public RoomPicker(ArrayList<Tuple> available) {
		this.availableRooms = available;
	}
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buildingNameCol.setCellValueFactory(new PropertyValueFactory<Building, String>("ID"));
		roomCapCol.setCellValueFactory(new PropertyValueFactory<Room, String>("capacity"));
		roomNameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
		roomdescrCol.setCellValueFactory(new PropertyValueFactory<Room, String>("description"));
		
		roomlistTable.setItems(roomdata);
		
		
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
	class PickRoomCell extends TableCell<Room, Boolean> {
    	final Button p = new Button("Pick");
    	PickRoomCell(){
    		p.setOnMousePressed(new EventHandler<MouseEvent>(){
    			@Override
    			public void handle(MouseEvent mouseEvent){
    				Room r = (Room) PickRoomCell.this.getTableView().getItems().get(PickRoomCell.this.getIndex());
    				DBConnect.deleteRoom(r.getID());
    				roomdata.remove(r);
    				System.out.println("HELLO");
    			}
    		});
    	}
    }


}
