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

/**
 * The Class RoomPicker. A GUI for picking available rooms for an appointment.
 * 
 * @author Gruppe 16
 */
public class RoomPicker implements Initializable {

	/** A table listing up rooms */
	@FXML private TableView<Room> roomlistTable;
	
	/** The building name column. */
	@FXML private TableColumn<Room, String> buildingNameCol;
	
	/** The room name column. */
	@FXML private TableColumn<Room, String> roomNameCol;
	
	/** The room cap column. */
	@FXML private TableColumn<Room, String> roomCapCol;
	
	/** The room description column. */
	@FXML private TableColumn<Room, String> roomdescrCol;
	
	/** The cancel button. Closes the Room Picker without selecting a room.*/
	@FXML private Button cancelButton;
	
	/** The choose button. Closes the Room Picker, choosing the highlighted room. */
	@FXML private Button chooseButton;
	
	/** The capacity field. Takes input for the room capacity.*/
	@FXML private TextField capacityField;

	/** The stage. */
	private static Stage stage;
	
	/** The add appointment variable. Holds the parent window AddAppointment. */
	private static AddAppointment addAppointment;
	
	/** The available rooms. */
	private static List<Room> availableRooms;

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
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
					addAppointment.roomTextField.setText(r.getName() + ", in building: " + r.getBuildingName());
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
				stage.close();
			}
		});

		updateRoomdata();

	}

	/**
	 * A method used to update the list of available rooms.
	 */
	private void updateRoomdata(){
		ArrayList<Room> rooms = new ArrayList<Room>();
		try{
			for(Room r : availableRooms){
				if (capacityField.getText().equals("") || r.getCapacity() >= Integer.parseInt(capacityField.getText())){
					rooms.add(r);
				}
			}
			buildingNameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("buildingName"));
			roomCapCol.setCellValueFactory(new PropertyValueFactory<Room, String>("capacity"));
			roomNameCol.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
			roomdescrCol.setCellValueFactory(new PropertyValueFactory<Room, String>("description"));	
			roomlistTable.setItems(FXCollections.observableArrayList(rooms));
		} catch (NumberFormatException e){
			capacityField.setEffect(new InnerShadow(4.0, Color.RED));
		}	


	}


	/**
	 * Starts the Room Picker.
	 *
	 * @param stage the stage
	 * @param owner the owner
	 * @param addAppointment the AddAppointment object
	 * @param date the date to look for available rooms
	 * @param fromTime the from time to look for available rooms
	 * @param toTime the to time to look for available rooms
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void start(Stage stage, Window owner, AddAppointment addAppointment, LocalDate date, LocalTime fromTime, LocalTime toTime) throws IOException {
		RoomPicker.stage = stage;
		RoomPicker.addAppointment = addAppointment;
		if(AddAppointment.editMode){
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
