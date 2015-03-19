package com.gruppe16.main;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Room;

public class AddAppointment implements Initializable {
	@FXML
	private Button sendBtn;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button findEmployeeBtn;
	
	@FXML
	private Button searchForRoomBtn;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private TextArea descriptionTextArea;
	
	@FXML
	private TextField attendeesTextField;

	@FXML
	private TextField fromTextField;
	
	@FXML
	private TextField toTextField;
	
	@FXML
	protected TextField roomTextField;

	@FXML
	private TextField titleTextField;
	
	@FXML
	private Label errorMessage;
	
	@FXML
	private Label titleLabel;

	private static Stage stage;
	private static LocalDate startDate = null;
	private static int startTime = 0;
	public static Appointment appointment = null;
	private static ArrayList<Employee> participants = null;
	private static Room oldRoom;
	public static boolean editMode = false;
	public Room room;
	private static ArrayList<Employee> availableEmployees = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		availableEmployees = new ArrayList<Employee>();
		for(Employee e : DBConnect.getEmployeeList()) {
			if(!e.equals(Login.getCurrentUser())){
				availableEmployees.add(e);
			}
		}
		
		descriptionTextArea.setWrapText(true);
		roomTextField.setDisable(true);
		attendeesTextField.setDisable(true);
		
		if(editMode) datePicker.setValue(appointment.getAppDate());
		else if(startDate != null) datePicker.setValue(startDate);
		else datePicker.setValue(LocalDate.now());
		
		if(editMode) {
			titleTextField.setText(appointment.getTitle());
			descriptionTextArea.setText(appointment.getDescription());
			titleLabel.setText("Edit Appointment");
			roomTextField.setText(oldRoom.getName());
			room = oldRoom;
			setAttendees(participants);
		}
		else{
			titleTextField.setText(null);
			descriptionTextArea.setText(null);
		}
		
		roomTextField.setEditable(true);
		sendBtn.setOnAction(new EventHandler<ActionEvent>(){
			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				boolean valid = true;
				
				int fromHour = 0, toHour = 0, fromMin = 0, toMin = 0;
				if(fromTextField.getText() != null && toTextField.getText() != null && !fromTextField.getText().isEmpty() && !toTextField.getText().isEmpty()) {
					fromHour = Integer.parseInt(fromTextField.getText(0, 2)); toHour = Integer.parseInt(toTextField.getText(0, 2));
					fromMin = Integer.parseInt(fromTextField.getText(3, 5)); toMin = Integer.parseInt(toTextField.getText(3, 5));
				}
				
				if(!checkRoom(datePicker.getValue(), LocalTime.of(fromHour, fromMin, 0), LocalTime.of(toHour, toMin))) {
					roomTextField.setEffect(new InnerShadow(4.0, Color.RED));
					errorMessage.setText((roomTextField.getText() == null || roomTextField.getText().isEmpty()) ? "Please select a room." : "The room is already reserved. Please find another room.");
					valid = false;
				}
				
				fromTextField.setEffect(null);
				toTextField.setEffect(null);
				
				if(fromHour > toHour || (fromHour == toHour && fromMin >= toMin)) {
					toTextField.setEffect(new InnerShadow(4.0, Color.RED));
					fromTextField.setEffect(new InnerShadow(4.0, Color.RED));
					errorMessage.setText("Appointment can not end before it begins.");
					valid = false;
				}
				
				LocalDate date = datePicker.getValue();
				if(date == null || date.isBefore(LocalDate.now())) {
					datePicker.setEffect(new InnerShadow(4.0, Color.RED));
					errorMessage.setText("Please select a date.");
					valid = false;
				}

				if(titleTextField.getText() == null || titleTextField.getText().isEmpty()) {
					titleTextField.setEffect(new InnerShadow(4.0, Color.RED));
					errorMessage.setText("Please enter a title.");
					valid = false;
				}
				
				if(valid) {
					if(editMode){
						DBConnect.editAppointment(appointment.getID(), titleTextField.getText(), descriptionTextArea.getText() == null ? "" : descriptionTextArea.getText(), Date.valueOf(datePicker.getValue()), new Time(toHour, toMin, 0), new Time(fromHour, fromMin, 0));
						if(isChangedTime()){
							for(Employee e : attendees){
								DBConnect.deleteAppointmentAndEmployee(appointment.getID(), e.getID());
								e.invite(appointment.getID());
							}
						}else {
							for(Employee a : attendees){
								if(!participants.contains(a)){
									a.invite(appointment.getID());
								}
							}
							for(Employee p : participants){
								if(!attendees.contains(p)){
									DBConnect.deleteAppointmentAndEmployee(appointment.getID(), p.getID());
								}
							}
						}
						DBConnect.deleteRoomReservation(appointment.getID(), oldRoom.getID());
						DBConnect.addRoomReservation(appointment.getID(), room.getID(), room.getBuildingID());
						stage.close();
					}else{
						int appid = DBConnect.addAppointment(titleTextField.getText(), descriptionTextArea.getText(), Date.valueOf(datePicker.getValue()), new Time(fromHour, fromMin, 0), new Time(toHour, toMin, 0), Login.getCurrentUser());
						DBConnect.addRoomReservation(appid, room.getID(), room.getBuildingID());
						DBConnect.setOwnerOfAppointment(Login.getCurrentUser(), appid);
						for(Employee e : attendees){
							e.invite(appid);
						}
						stage.close();
					}

				}
			}
		});
		
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});

		titleTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) titleTextField.setEffect(null);
			}
		});

		datePicker.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) datePicker.setEffect(null);
			}
		});
		
		datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0, LocalDate arg1, LocalDate arg2) {
				invalidateRoom();
			}
		});

		descriptionTextArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) descriptionTextArea.setEffect(null);
			}
		});

		fromTextField.setEditable(false);
		fromTextField.setUserData(new Integer(0));
		if(editMode){
			fromTextField.setText(appointment.getFromTime().toString());
			toTextField.setText(appointment.getToTime().toString());
		}
		else if(startTime != 0){
			if(startTime < 10) {
				fromTextField.setText("0"+ startTime + ":00");
				toTextField.setText("0"+ startTime + ":59");
			}
			else {
				fromTextField.setText(startTime + ":00");
				toTextField.setText(startTime + ":59");
			}
		}
		fromTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					if(fromTextField.getText().isEmpty()) fromTextField.setText("00:00");
					fromTextField.setUserData(new Integer(0));
					fromTextField.positionCaret(0);
					fromTextField.selectForward();
					fromTextField.selectForward();
				}
			}
		});
		
		fromTextField.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getX() < 20) {
					fromTextField.setUserData(new Integer(0));
					fromTextField.positionCaret(0);
					fromTextField.selectForward();
					fromTextField.selectForward();
				}
				else {
					fromTextField.setUserData(new Integer(3));
					fromTextField.positionCaret(3);
					fromTextField.selectForward();
					fromTextField.selectForward();
				}
			}
		});
		
		fromTextField.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getX() < 20) {
					fromTextField.setUserData(new Integer(0));
					fromTextField.positionCaret(0);
					fromTextField.selectForward();
					fromTextField.selectForward();
				}
				else {
					fromTextField.setUserData(new Integer(3));
					fromTextField.positionCaret(3);
					fromTextField.selectForward();
					fromTextField.selectForward();
				}
			}
		});

		fromTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				invalidateRoom();
				
				char c = event.getCharacter().charAt(0);
				String beforeText = fromTextField.getText();
				
				Integer textPos = (Integer)fromTextField.getUserData();
				if(textPos < 5) {
					if(c >= '0' && c <= '9') {
						fromTextField.setText(fromTextField.getText(0, textPos) + c + fromTextField.getText(textPos+1, fromTextField.getText().length()));
					}
				}
				
				if(Integer.parseInt(fromTextField.getText(0, 2)) >= 24 || Integer.parseInt(fromTextField.getText(3, 5)) >= 60) {
					fromTextField.setText(beforeText);
				}
				else {
					textPos += 1;
					if(textPos == 2) {
						textPos += 1;
					}
					else if(textPos >= 5) {
						textPos = 0;
						toTextField.requestFocus();
					}
					fromTextField.setUserData(textPos);
				}
				
				if(textPos < 2) {
					fromTextField.positionCaret(0);
					fromTextField.selectForward();
					fromTextField.selectForward();
				}
				else {
					fromTextField.positionCaret(3);
					fromTextField.selectForward();
					fromTextField.selectForward();
				}
			}
		});
		
		fromTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) fromTextField.setEffect(null);
			}
		});
		
		toTextField.setEditable(false);
		toTextField.setUserData(new Integer(0));

		toTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					if(toTextField.getText().isEmpty()) toTextField.setText("00:00");
					toTextField.setUserData(new Integer(0));
					toTextField.positionCaret(0);
					toTextField.selectForward();
					toTextField.selectForward();
				}
			}
		});
		
		toTextField.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getX() < 20) {
					toTextField.setUserData(new Integer(0));
					toTextField.positionCaret(0);
					toTextField.selectForward();
					toTextField.selectForward();
				}
				else {
					toTextField.setUserData(new Integer(3));
					toTextField.positionCaret(3);
					toTextField.selectForward();
					toTextField.selectForward();
				}
			}
		});
		
		toTextField.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getX() < 20) {
					toTextField.setUserData(new Integer(0));
					toTextField.positionCaret(0);
					toTextField.selectForward();
					toTextField.selectForward();
				}
				else {
					toTextField.setUserData(new Integer(3));
					toTextField.positionCaret(3);
					toTextField.selectForward();
					toTextField.selectForward();
				}
			}
		});

		toTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				invalidateRoom();
				
				char c = event.getCharacter().charAt(0);
				String beforeText = toTextField.getText();
				
				Integer textPos = (Integer)toTextField.getUserData();
				if(textPos < 5) {
					if(c >= '0' && c <= '9') {
						toTextField.setText(toTextField.getText(0, textPos) + c + toTextField.getText(textPos+1, toTextField.getText().length()));
					}
				}
				
				if(Integer.parseInt(toTextField.getText(0, 2)) >= 24 || Integer.parseInt(toTextField.getText(3, 5)) >= 60) {
					toTextField.setText(beforeText);
				}
				else {
					textPos += 1;
					if(textPos == 2) {
						textPos += 1;
					}
					toTextField.setUserData(textPos);
				}
				
				if(textPos < 2) {
					toTextField.positionCaret(0);
					toTextField.selectForward();
					toTextField.selectForward();
				}
				else {
					toTextField.positionCaret(3);
					toTextField.selectForward();
					toTextField.selectForward();
				}
			}
		});
		
		toTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) toTextField.setEffect(null);
			}
		});

		roomTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if(newValue) roomTextField.setEffect(null);
				
			}
		});
		
		roomTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				roomTextField.setEffect(null);
			}
		});
		
		searchForRoomBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				boolean valid = true;
				
				LocalDate date = datePicker.getValue();
				if(date == null || date.isBefore(LocalDate.now())) {
					datePicker.setEffect(new InnerShadow(4.0, Color.RED));
					valid = false;
				}
				
				int fromHour = 0, toHour = 0, fromMin = 0, toMin = 0;
				if(!fromTextField.getText().isEmpty() && !toTextField.getText().isEmpty()) {
					fromHour = Integer.parseInt(fromTextField.getText(0, 2)); toHour = Integer.parseInt(toTextField.getText(0, 2));
					fromMin = Integer.parseInt(fromTextField.getText(3, 5)); toMin = Integer.parseInt(toTextField.getText(3, 5));
				}
				
				if(fromHour > toHour || (fromHour == toHour && fromMin >= toMin)) {
					toTextField.setEffect(new InnerShadow(4.0, Color.RED));
					fromTextField.setEffect(new InnerShadow(4.0, Color.RED));
					valid = false;
				}
				
				if(valid){
					try{
						RoomPicker.start(new Stage(), stage.getScene().getWindow(), AddAppointment.this, datePicker.getValue(), LocalTime.of(fromHour, fromMin, 0), LocalTime.of(toHour, toMin));
					}catch (Exception rr){
						rr.printStackTrace();
					}
				}
			}
		});
		
		findEmployeeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					EmployeePicker.start(new Stage(), stage.getScene().getWindow(), AddAppointment.this, attendees);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private boolean isChangedTime(){
		return !fromTextField.getText().equals(appointment.getFromTime().toString()) || !toTextField.getText().equals(appointment.getToTime().toString()) || !datePicker.getValue().isEqual(appointment.getAppDate());
	}

	private void invalidateRoom() {
		room = null;
		roomTextField.setEffect(new InnerShadow(4.0, Color.RED));
	}
	
	public ArrayList<Employee> attendees = new ArrayList<Employee>(); 

	public void setAttendees(Collection<Employee> employees) {
		attendees.clear();
		attendeesTextField.clear();

		int i = 0;
		for(Employee e : employees) {
			attendees.add(e);
		}
		
		attendees.sort(new Comparator<Employee>() {

			@Override
			public int compare(Employee arg0, Employee arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
			
		});
		
		for(Employee a: attendees) {
			if(i++ > 0) {
				attendeesTextField.appendText(", ");
			}
			attendeesTextField.appendText(a.getFirstName() + " " + a.getLastName());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void start(Stage stage, Window owner, java.util.Date date) throws Exception {
		AddAppointment.editMode = false;
		AddAppointment.stage = stage;
		AddAppointment.startDate = new java.sql.Date(date.getTime()).toLocalDate();
		AddAppointment.startTime = date.getHours();
		Scene scene = new Scene((Parent)FXMLLoader.load(AddAppointment.class.getResource("/com/gruppe16/main/AddAppointment.fxml")));
		stage.setResizable(false);
		stage.initStyle(StageStyle.UTILITY);
		if(owner != null) {
			stage.initOwner(owner);
			stage.initModality(Modality.WINDOW_MODAL);
		}
		stage.setScene(scene);
		stage.show();
	}
	
	public boolean checkRoom(LocalDate appdate, LocalTime fromtime, LocalTime totime){
		if(room == null) return false;
		
		List<Room> rooms;
		if (editMode){
			rooms = DBConnect.findRoom(appdate, fromtime, totime, appointment);
		}
		else{
			rooms = DBConnect.findRoom(appdate, fromtime, totime);
		}
		
		boolean check = false;
		for (Room r: rooms){
			if (r.getBuildingID() == room.getBuildingID() && r.getID() == room.getID()){
				check = true;
			}
		}
		return check;
	}
	
	
	public static void start(Stage stage, Window owner, Appointment appointment, ArrayList<Employee> attendees, Room room) throws Exception {
		AddAppointment.editMode = true;
		AddAppointment.stage = stage;
		AddAppointment.appointment = appointment;
		AddAppointment.oldRoom = room;
		attendees.remove(0);
		AddAppointment.participants = attendees;
		Scene scene = new Scene((Parent)FXMLLoader.load(AddAppointment.class.getResource("/com/gruppe16/main/AddAppointment.fxml")));
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
