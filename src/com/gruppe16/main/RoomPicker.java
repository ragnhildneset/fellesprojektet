package com.gruppe16.main;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Building;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Room;
import com.gruppe16.entities.RoomReservation;
import com.gruppe16.util.Tuple;

public class RoomPicker implements Initializable {
	
    @FXML private TableView<Room> roomlistTable;
    @FXML private TableColumn<Room, String> buildingNameCol;
    @FXML private TableColumn<Room, String> roomNameCol;
    @FXML private TableColumn<Room, String> roomCapCol;
    @FXML private TableColumn<Room, String> roomdescrCol;
    @FXML private TableColumn<Room, Boolean> rlist_pick;
    @FXML private Button cancelButton;
    @FXML private Button chooseButton;
    @FXML private TextField capacityField;

	private static Stage stage;
	private static AddAppointment addAppointment;
    static ObservableList<Room> roomdata;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buildingNameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("buildingname"));
		roomCapCol.setCellValueFactory(new PropertyValueFactory<Room, String>("capacity"));
		roomNameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
		roomdescrCol.setCellValueFactory(new PropertyValueFactory<Room, String>("description"));	
		roomlistTable.setItems(roomdata);
		capacityField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				updateRoomdata();
				
			}
			
		});
		
		chooseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Room r = roomlistTable.getSelectionModel().getSelectedItem();
				if(r != null) {
					addAppointment.roomTextField.setText(r.getName());
					addAppointment.room = r;
					stage.close();
				}
			}
		});
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				addAppointment.rpClosed();
				stage.close();
			}
		});
			
	}
	
	public void updateRoomdata(){
		
	}
	
	public static void start(Stage stage, Window owner, AddAppointment addApp, LocalDate date, LocalTime fromTime, LocalTime toTime ) throws IOException {
		RoomPicker.stage = stage;
		RoomPicker.addAppointment = addApp;
		RoomPicker.roomdata = FXCollections.observableArrayList(RoomReservation.findRoom(date, fromTime, toTime));
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
