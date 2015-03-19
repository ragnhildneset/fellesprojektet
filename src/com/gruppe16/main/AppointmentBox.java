package com.gruppe16.main;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.AppointmentAndEmployee;
import com.gruppe16.entities.Employee;
import com.gruppe16.entities.Room;

public class AppointmentBox extends AnchorPane{
	
	private static int PANEL_WIDTH_PARENT = 710;
	private static int PANEL_HEIGHT_PARENT = 1250;
	private static int PANEL_WIDTH_OPEN = 450;
	private static int PANEL_HEIGHT_OPEN = 255;
	
	public enum PanelColors {
	    RED("#FFCCCC", "#FFAAAA", "#FF0000"),
	    GREEN("#CCFFCC", "#AAFFAA", "#00FF00"),
	    BLUE("#CCCCFF", "#AAAAFF", "#0000FF"),
	    YELLOW("#FFFFCC", "#FFFFAA", "#DDDD00"),
	    BROWN("D8C2A0", "#C29653", "#7E4C00"),
	    PURPLE("#FFCCFF", "#FFAAFF", "#FF00FF"),
	    ORANGE("#FFB27F", "#FF9900", "#DE8500"),
	    TURQUOISE("#CCFFFF", "#AAFFFF", "#00FFFF"),
	    GREY("#CCCCCC", "#AAAAAA", "#000000")
	    ;
	    
	    private String style;
	    
	    private String mainColor;
	    
	    PanelColors(String cMain, String cSecondary, String cBorder){
	    	this.style = "cMain: " + cMain + "; cSecondary: " + cSecondary + "; cBorder: " + cBorder + ";";
	    	this.mainColor = cMain;
	    }
	    
	    public String getStyle() {
	    	return style;
	    }
	    
	    public String getMainColor() {
	    	return mainColor;
	    }
	}
	
	private Appointment appointment;
	private double panelWidth;
	private double panelHeight;
	private double panelX;
	private double panelY;
	private boolean active = false;
	private boolean show = false;
	private PanelColors color;
	private DayPlanView dpv;
	private Employee e;
	private Room room;
	
	static ObservableList<Employee> employeedata = FXCollections.observableArrayList(DBConnect.getEmployeeList());
	
	public AppointmentBox(Appointment appointment, AppointmentAndEmployee appAndEmp, DayPlanView dpv){
		setId("appBox");
		this.e = Employee.getEmployee(appAndEmp.getEmployeeid());
		this.appointment = appointment;
		this.room = DBConnect.getRoom(appointment);
		LocalTime start = this.appointment.getFromTime();
		LocalTime end = this.appointment.getToTime();
		this.color = toEnumColor(appAndEmp.getColor());
		this.dpv = dpv;
		getStylesheets().add("/com/gruppe16/main/listView.css");
		int appointmentTime = (end.toSecondOfDay() - start.toSecondOfDay())/60;
		int appointmentStart = start.toSecondOfDay()/60;
		setPrefSize(PANEL_WIDTH_PARENT, Math.max(appointmentTime, 40));
		relocate(0, appointmentStart);
		updateLabels();
		}
	public AppointmentBox(Appointment appointment, DayPlanView dpv){
		setId("appBox");
		this.e = null;
		this.room = DBConnect.getRoom(appointment);
		this.appointment = appointment;
		LocalTime start = this.appointment.getFromTime();
		LocalTime end = this.appointment.getToTime();
		this.color = PanelColors.BLUE;
		this.dpv = dpv;
		getStylesheets().add("/com/gruppe16/main/listView.css");
		int appointmentTime = (end.toSecondOfDay() - start.toSecondOfDay())/60;
		int appointmentStart = start.toSecondOfDay()/60;
		setPrefSize(PANEL_WIDTH_PARENT, Math.max(appointmentTime, 40));
		relocate(0, appointmentStart);
		updateLabels();
		}
	
	public void updateLabels(){
		ArrayList<Label> labels = new ArrayList<Label>();
		for(Node n : getChildren()){
			if(n instanceof Label){
				labels.add((Label) n);
			}
		}
		getChildren().removeAll(labels);
		setStyle(color.getStyle());

		//Title
		Label titleLabel = new Label(appointment.getTitle());
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleLabel.setPrefWidth(getPrefWidth()-10);
		titleLabel.setClip(new Rectangle(getPrefWidth()-5, 45));
		
		//TimeStamps
		Label timeLabel = new Label("From: " + appointment.getFromTime()+"\nTo: " + appointment.getToTime());
		timeLabel.setTextAlignment(TextAlignment.RIGHT);
		
		//Description
		Pane descriptionPane = new Pane();
		
		//Description Title
		Label descriptionTitleLabel = new Label("Description:");
		descriptionTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		//Description
		Label descriptionLabel = new Label(appointment.getDescription());
		descriptionLabel.setAlignment(Pos.TOP_LEFT);
		descriptionLabel.setWrapText(true);
		descriptionLabel.setLayoutY(15);
		
		//Room
		Label roomTitleLabel = new Label("Room:");
		roomTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		Label roomLabel = new Label(room.getName());
		roomLabel.setFont(Font.font("Arial", 16));
		roomLabel.setLayoutX(55);
		
		descriptionPane.getChildren().addAll(descriptionTitleLabel, descriptionLabel, roomTitleLabel, roomLabel);
		
		//Delete Button
		Button delBtn = new Button("Delete");
		
		//Delete Button controller
		delBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				deleteLeaveDialog(true);
			}
		});
		
		//Leave Button
		Button leaveBtn = new Button("Leave");
		
		//Leave Button controller
		leaveBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				deleteLeaveDialog(false);
			}
		});
		
		//Edit Button
		Button editBtn = new Button("Edit");
		
		//Edit Button Controller
		editBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evnet) {
				try {
					Stage newStage = new Stage();
					newStage.setOnHidden(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent event) {
							dpv.showAppointments();
							dpv.getMainPane().redraw();
							}
					});
					AddAppointment.start(newStage, dpv.getMainPane().getScene().getWindow(), appointment, getParticipants(), room);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//Participants
		Pane participantPane = new Pane();
		
		//Participants Title
		Label participantsTitleLabel = new Label("Participants:");
		participantsTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		//Participants ListView
		ListView<AnchorPane> participants = new ListView<AnchorPane>();
		participants.setFocusTraversable( false );
		participantPane.getChildren().addAll(participantsTitleLabel,participants);
		participants.setLayoutY(20);
		
		//Show Participants Button
				Button showBtn = new Button("Show participants");
				showBtn.setPrefWidth(150);;
				
				showBtn.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent event) {
						if(!show){
							show = true;
							descriptionPane.setVisible(false);
							participantPane.setVisible(true);
							ObservableList<Employee> attendees = FXCollections.observableArrayList(getParticipants());
							ObservableList<AnchorPane> attendeeAnchorPane = FXCollections.observableArrayList();
							
							
							for(Employee a : attendees){			
								AnchorPane attendeePane = new AnchorPane();
								Label nameLabel = new Label(a.getName());
								Label statusLabel = new Label();
								AnchorPane.setLeftAnchor(nameLabel, 5.0);
								AnchorPane.setRightAnchor(statusLabel, 5.0);
								if(a.getID() == appointment.getOwnerID()) {
									statusLabel.setText("Owner");
									statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
								}
								else if(DBConnect.getStatus(appointment.getID(), a.getID()) == 1) {
									statusLabel.setText("Attending");
									statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
								}
								else if(DBConnect.getStatus(appointment.getID(), a.getID()) == 2) {
									statusLabel.setText("Declined");
									statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
								}
								else if(DBConnect.getStatus(appointment.getID(), a.getID()) == 0) {
									statusLabel.setText("Pending");
									statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
								}
								attendeePane.getChildren().addAll(nameLabel, statusLabel);
								attendeeAnchorPane.add(attendeePane);
							}
							participants.setItems(attendeeAnchorPane);
							participantsTitleLabel.setText("Participants (" + attendees.size() + "):");
							
							showBtn.setText("Hide participants");
						}
						else if(show){
							show = false;
							participantPane.setVisible(false);
							descriptionPane.setVisible(true);
							showBtn.setText("Show participants");
						}
					}
				});
		
		//Colors dropDownMenu
				 ComboBox<PanelColors> colorPicker = new ComboBox<PanelColors>();
				 colorPicker.setStyle(color.style);
				 colorPicker.getItems().addAll(
				     PanelColors.RED,
				     PanelColors.GREEN,
				     PanelColors.BLUE,
				     PanelColors.YELLOW,
				     PanelColors.BROWN,
				     PanelColors.PURPLE,
				     PanelColors.ORANGE,
				     PanelColors.TURQUOISE,
				     PanelColors.GREY);

				 colorPicker.setCellFactory(new Callback<ListView<PanelColors>, ListCell<PanelColors>>() {
				     @Override public ListCell<PanelColors> call(ListView<PanelColors> p) {
				         return new ListCell<PanelColors>() {		
				             @Override protected void updateItem(PanelColors item, boolean empty) {
				                 super.updateItem(item, empty);
				                 
				                 if (item == null || empty) {
				                     setStyle("-fx-background-color: " + PanelColors.BLUE.mainColor);
				                 } else {
				                	 setStyle("-fx-background-color: " + item.mainColor);
				                 }
				            }
				       };
				   }
				});
				colorPicker.setPrefWidth(150);
				colorPicker.setValue(color);
				colorPicker.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent event) {
						DBConnect.setColorOfAppointment(Login.getCurrentUser(), appointment.getID(), toStringColor(colorPicker.getValue()));
						dpv.showAppointments();
					}
				});
				 
		//Add Everything
		getChildren().addAll(titleLabel);
		AnchorPane.setRightAnchor(timeLabel, 5.0);
		AnchorPane.setTopAnchor(timeLabel, 5.0);
		AnchorPane.setTopAnchor(titleLabel, 5.0);
		AnchorPane.setLeftAnchor(titleLabel, 5.0);
		AnchorPane.setTopAnchor(descriptionPane, 40.0);
		AnchorPane.setLeftAnchor(descriptionPane, 5.0);
		AnchorPane.setTopAnchor(participantPane, 40.0);
		AnchorPane.setLeftAnchor(participantPane, 5.0);
		AnchorPane.setBottomAnchor(delBtn, 5.0);
		AnchorPane.setRightAnchor(delBtn, 5.0);
		AnchorPane.setBottomAnchor(leaveBtn, 5.0);
		AnchorPane.setRightAnchor(leaveBtn, 5.0);
		AnchorPane.setBottomAnchor(editBtn, 5.0);
		AnchorPane.setRightAnchor(editBtn, 70.0);
		AnchorPane.setBottomAnchor(showBtn, 5.0);
		AnchorPane.setLeftAnchor(showBtn, 5.0);
		AnchorPane.setBottomAnchor(colorPicker, 5.0);

		//Controller
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				if(!active){
					active = true;
					panelWidth = getPrefWidth();
					panelHeight = getPrefHeight();
					panelX = getLayoutX();
					panelY = getLayoutY();
					toFront();
					if(panelWidth < PANEL_WIDTH_OPEN) setPrefWidth(PANEL_WIDTH_OPEN);
					if(panelHeight < PANEL_HEIGHT_OPEN) setPrefHeight(PANEL_HEIGHT_OPEN);
					if(panelX > PANEL_WIDTH_PARENT - PANEL_WIDTH_OPEN) setLayoutX(PANEL_WIDTH_PARENT - PANEL_WIDTH_OPEN);
					if(panelY > PANEL_HEIGHT_PARENT) setLayoutY(PANEL_HEIGHT_PARENT);
					getChildren().addAll(timeLabel, descriptionPane, participantPane);
					titleLabel.setClip(null);
					titleLabel.setPrefWidth(getPrefWidth()-10);
					timeLabel.setVisible(true);
					participantPane.setVisible(false);
					descriptionPane.setPrefWidth(getPrefWidth()-10);
					descriptionPane.setPrefHeight(getPrefHeight()-100);
					descriptionLabel.setPrefWidth(getPrefWidth()-10);
					descriptionLabel.setPrefHeight(getPrefHeight()-100);
					participantPane.setPrefWidth(getPrefWidth()-10);
					participantPane.setPrefHeight(getPrefHeight()-100);
					participants.setPrefWidth(getPrefWidth()-12);
					participants.setPrefHeight(getPrefHeight()-105);
					roomTitleLabel.setLayoutY(descriptionPane.getPrefHeight()-10);
					roomLabel.setLayoutY(descriptionPane.getPrefHeight()-10);
					if(!show){
						descriptionPane.setVisible(true);
					}
					else {
						participantPane.setVisible(true);
					}
					if (e == null || e.getID() != Login.getCurrentUser().getID()) getChildren().add(showBtn);
					else if(Login.getCurrentUser().getID() == appointment.getOwnerID()){
						AnchorPane.setRightAnchor(colorPicker, 118.0);
						getChildren().addAll(delBtn, editBtn, showBtn, colorPicker);
					}
					else {
						AnchorPane.setRightAnchor(colorPicker, 65.0);
						getChildren().addAll(showBtn, leaveBtn, colorPicker);
					}
				}
				else{
					active = false;
					toBack();
					setPrefSize(panelWidth, panelHeight);
					setLayoutX(panelX);
					setLayoutY(panelY);
					titleLabel.setPrefWidth(getPrefWidth()-10);
					timeLabel.setVisible(false);
					getChildren().removeAll(timeLabel, descriptionPane, participantPane, delBtn, editBtn, showBtn, leaveBtn, colorPicker);
				}
			}
		});
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(active) toFront();
				if(!active) toBack();
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(!active) toBack();
			}
		});
		
		
	}
	
	private void deleteLeaveDialog(boolean b) {
		//To Leave, set False. To Delete, set True;
		boolean delete = b;
		Stage dialogStage = new Stage();
		VBox deleteBox = new VBox();
		VBox deleteBox2 = new VBox();
		FlowPane buttonPane = new FlowPane();
		Label deleteLabel;
		if(delete) deleteLabel = new Label("Delete "+ appointment.getTitle()+"?");
		else deleteLabel = new Label("Leave "+ appointment.getTitle()+"?");
		deleteLabel.setFont(new Font(18));
		deleteLabel.setAlignment(Pos.CENTER);
		deleteLabel.setPrefSize(250,60);
		deleteLabel.setWrapText(true);
		deleteLabel.setTextAlignment(TextAlignment.CENTER);
		Button yesBtn = new Button("Yes");
		yesBtn.setPrefWidth(60);
		Button noBtn = new Button("No");
		noBtn.setPrefWidth(60);
		buttonPane.getChildren().addAll(yesBtn, noBtn);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setHgap(10);
		deleteBox2.getChildren().add(buttonPane);
		deleteBox.getChildren().addAll(deleteLabel, deleteBox2);
		deleteBox.setAlignment(Pos.CENTER);
		
		Scene deleteScene = new Scene(deleteBox, 300, 100);
		dialogStage.setScene(deleteScene);
		dialogStage.setResizable(false);
		dialogStage.initStyle(StageStyle.UTILITY);
		if(delete) dialogStage.setTitle("Delete "+ appointment.getTitle()+"?");
		else dialogStage.setTitle("Leave "+ appointment.getTitle()+"?");
		dialogStage.initOwner(dpv.getScene().getWindow());
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.show();
		
		yesBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				if(delete) DBConnect.deleteAppointment(appointment.getID());
				else DBConnect.deleteAppointmentAndEmployee(appointment.getID(), Login.getCurrentUser().getID());
				
				dpv.showAppointments(Login.getCurrentUser());
				dialogStage.close();
			}
		});
		
		noBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				dialogStage.close();
			}
		});
	}
	
	public void setColor(PanelColors color){
		this.color = color;
	}
	
	public LocalTime getStart(){
		return appointment.getFromTime();
	}
	
	public LocalTime getEnd(){
		return appointment.getToTime();
	}
	
	public void toDefaultSize(){
		setPrefWidth(PANEL_WIDTH_PARENT);
	}
	
	public int getID(){
		return appointment.getID();
	}
	public int getDuration() {
		return getEnd().getMinute() - getStart().getMinute();
	}	
	
	public ArrayList<Employee> getParticipants() {
	    ArrayList<AppointmentAndEmployee> AppAndEmp = DBConnect.getAppointmentAndEmployee();
	    ArrayList<Employee> participants = new ArrayList<Employee>();
	    if(Login.getCurrentUser().getID() == appointment.getOwnerID()){
		    for(AppointmentAndEmployee aae : AppAndEmp) {
		    	if(aae.getAppid() == appointment.getID()) {
		    		participants.add(employeedata.get(aae.getEmployeeid()));
		    	}
		    }
	    }
	    else {
	    	 for(AppointmentAndEmployee aae : AppAndEmp) {
			    	if(aae.getAppid() == appointment.getID() && aae.getStatus() == 1) {
			    		participants.add(employeedata.get(aae.getEmployeeid()));
			    	}
			    }
	    }
	    
	    participants.sort(new Comparator<Employee>(){

			@Override
			public int compare(Employee o1, Employee o2) {
				if(o1.getID() == appointment.getOwnerID()) return -1;
				else if(o2.getID() == appointment.getOwnerID()) return 1;
				else return DBConnect.getStatus(appointment.getID(), o1.getID()) - DBConnect.getStatus(appointment.getID(), o2.getID());
			}
	    	
	    });
	    
		return participants;
	}
	
    static public PanelColors toEnumColor(String color){
    	color.toUpperCase();
    	if(color.equals("RED")) return PanelColors.RED;
    	else if(color.equals("GREEN")) return PanelColors.GREEN;
    	else if(color.equals("BLUE")) return PanelColors.BLUE;
    	else if(color.equals("YELLOW")) return PanelColors.YELLOW;
    	else if(color.equals("BROWN")) return PanelColors.BROWN;
    	else if(color.equals("PURPLE")) return PanelColors.PURPLE;
    	else if(color.equals("ORANGE")) return PanelColors.ORANGE;
    	else if(color.equals("TURQUOISE")) return PanelColors.TURQUOISE;
    	else if(color.equals("GREY")) return PanelColors.GREY;
    	else return PanelColors.BLUE;
    }
    
    static public String toStringColor(PanelColors color){
    	if(color == PanelColors.RED) return "RED";
    	else if(color == PanelColors.GREEN) return "GREEN";
    	else if(color == PanelColors.BLUE) return "BLUE";
    	else if(color == PanelColors.YELLOW) return "YELLOW";
    	else if(color == PanelColors.BROWN) return "BROWN";
    	else if(color == PanelColors.PURPLE) return "PURPLE";
    	else if(color == PanelColors.ORANGE) return "ORANGE";
    	else if(color == PanelColors.TURQUOISE) return "TURQUOISE";
    	else if(color == PanelColors.GREY) return "GREY";
    	else return "BLUE";
    }
}
