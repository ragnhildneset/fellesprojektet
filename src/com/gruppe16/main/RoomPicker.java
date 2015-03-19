package com.gruppe16.main;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Room;

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
	static List<Room> availableRooms;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		capacityField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				updateRoomdata();

			}

		});
		
		capacityField.setText(Integer.toString(addAppointment.attendees.size() + 1));
		chooseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Room r = roomlistTable.getSelectionModel().getSelectedItem();
				if(r != null) {
					addAppointment.roomTextField.setText(r.getName() + ", in building: " + r.getBuildingname());
					addAppointment.room = r;
					stage.close();
				}
			}
		});

		roomlistTable.setOnMouseClicked(event -> {
			if(event.getClickCount() == 2) {
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

		updateRoomdata();

	}

	public void updateRoomdata(){
		ArrayList<Room> rooms = new ArrayList<Room>();
		try{
			for(Room r : availableRooms){
				if (capacityField.getText().equals("") || r.getCapacity() >= Integer.parseInt(capacityField.getText())){
					rooms.add(r);
				}
			}
			buildingNameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("buildingname"));
			roomCapCol.setCellValueFactory(new PropertyValueFactory<Room, String>("capacity"));
			roomNameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
			roomdescrCol.setCellValueFactory(new PropertyValueFactory<Room, String>("description"));	
			roomlistTable.setItems(FXCollections.observableArrayList(rooms));
		} catch (NumberFormatException e){
			capacityField.setEffect(new InnerShadow(4.0, Color.RED));
		}	


	}


	public static void start(Stage stage, Window owner, AddAppointment addApp, LocalDate date, LocalTime fromTime, LocalTime toTime) throws IOException {
		RoomPicker.stage = stage;
		RoomPicker.addAppointment = addApp;
		if(addApp.editMode){
			RoomPicker.availableRooms = DBConnect.findRoom(date, fromTime, toTime, AddAppointment.appointment);
		}
		else{
			RoomPicker.availableRooms = DBConnect.findRoom(date, fromTime, toTime);
		}
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
