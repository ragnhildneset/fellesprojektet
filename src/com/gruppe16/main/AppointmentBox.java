package com.gruppe16.main;

import java.time.LocalTime;
import java.util.ArrayList;

import com.gruppe16.main.DayPlanView;
import com.gruppe16.database.DBConnect;
import com.gruppe16.entities.Appointment;
import com.gruppe16.entities.Employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AppointmentBox extends AnchorPane{
	
	private static int PANEL_WIDTH_PARENT = 710;
	private static int PANEL_HEIGHT_PARENT = 1250;
	private static int PANEL_WIDTH_OPEN = 450;
	private static int PANEL_HEIGHT_OPEN = 255;
	
	public enum panelColors {
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
	    
	    private String styleHover;
	    private String styleDefault;
	    private String styleListView;
	    panelColors(String cMain, String cSecondary, String cBorder){
	    	styleDefault = "-fx-background-color: " + cMain + "; -fx-border-width: 1; -fx-border-color: " + cBorder + ";";
	    	styleHover = "-fx-background-color: " + cSecondary + "; -fx-border-width: 1; -fx-border-color: " + cBorder + ";";
	    	styleListView = "list-view-color: " + cMain + ";";
	    }
	    
	    public String getDefault(){
	    	return styleDefault;
	    }
	    
	    public String getHover(){
	    	return styleHover;
	    }
	    public String getListView() {
	    	return styleListView;
	    }
	    
	}
	
	private Appointment appointment;
	private double panelWidth;
	private double panelHeight;
	private double panelX;
	private double panelY;
	private boolean active = false;
	private boolean show = false;
	private panelColors color;
	private DayPlanView dpv;
	
	//Using list of employees for testing
    static ObservableList<Employee> employeedata = FXCollections.observableArrayList(DBConnect.getEmployees().values());
	
	public AppointmentBox(Appointment appointment, panelColors color, DayPlanView dpv){
		this.appointment = appointment;
		LocalTime start = this.appointment.getFromTime();
		LocalTime end = this.appointment.getToTime();
		this.color = color;
		this.dpv = dpv;
		getStylesheets().add("/com/gruppe16/main/listView.css");
		int appointmentTime = (end.toSecondOfDay() - start.toSecondOfDay())/60;
		int appointmentStart = start.toSecondOfDay()/60;
		setPrefSize(PANEL_WIDTH_PARENT, Math.max(appointmentTime, 30));
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
		setStyle("list-view-color: #CCCCFF; -fx-background-color: #CCCCFF; -fx-border-width: 1; -fx-border-color: #0000FF;");
		//Title
		Label titleLabel = new Label(appointment.getTitle());
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleLabel.setPrefWidth(getPrefWidth()-10);
		titleLabel.setClip(new Rectangle(getPrefWidth()-5, 45));
		
		//TimeStamps
		Label timeLabel = new Label("From: " + appointment.getFromTime()+"\nTo: " + appointment.getToTime());
		timeLabel.setTextAlignment(TextAlignment.RIGHT);
		timeLabel.setVisible(false);
		
		//Description Title
		Label descriptionTitleLabel = new Label("Description:");
		descriptionTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		descriptionTitleLabel.setVisible(false);
		
		//Description
		Label descriptionLabel = new Label(appointment.getDescription());
		descriptionLabel.setAlignment(Pos.TOP_LEFT);
		descriptionLabel.setPrefWidth(getPrefWidth()-10);
		descriptionLabel.setPrefHeight(0);
		descriptionLabel.setWrapText(true);
		descriptionLabel.setVisible(false);
		
		//Delete Button
		Button delBtn = new Button("Delete");
		delBtn.setStyle(color.styleDefault);
		delBtn.setVisible(false);
		
		//Delete Button controller
		delBtn.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleHover);
			}
		});
		
		delBtn.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleDefault);
			}
		});
		
		delBtn.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleDefault);
			}
		});
		
		delBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				delBtn.setStyle(color.styleHover);
			}
		});
		
		delBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				
				Stage dialogStage = new Stage();
				VBox deleteBox = new VBox();
				VBox deleteBox2 = new VBox();
				FlowPane buttonPane = new FlowPane();
				
				Label deleteLabel = new Label("Delete "+ appointment.getTitle()+"?");
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
				dialogStage.setTitle("Delete "+ appointment.getTitle()+"?");
				dialogStage.initOwner(dpv.getScene().getWindow());
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.show();
				
				yesBtn.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent event) {
						DBConnect.deleteAppointment(appointment.getID());
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
		});
		
		//Edit Button
		Button editBtn = new Button("Edit");
		editBtn.setStyle(color.styleDefault);
		editBtn.setVisible(false);
		
		//Edit Button Controller
		editBtn.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleHover);
			}
		});
		
		editBtn.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleDefault);
			}
		});
		
		editBtn.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleDefault);
			}
		});
		
		editBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				editBtn.setStyle(color.styleHover);
			}
		});
		
		editBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				System.out.println("Edit!");
			}
		});
		
		//Participants Title
		Label participantsTitleLabel = new Label("Participants:");
		participantsTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		participantsTitleLabel.setVisible(false);
		
		//Participants ListView
		ListView<Employee> participants = new ListView<Employee>();
		participants.setPrefSize(0, 0);
		participants.setStyle(color.styleListView);
		participants.setVisible(false);
		participants.setMouseTransparent( true );
		participants.setFocusTraversable( false );
		
		
		//Show Participants Button
				Button showBtn = new Button("Show participants");
				showBtn.setStyle(color.styleDefault);
				showBtn.setVisible(false);
				showBtn.setPrefWidth(150);;
				
				
				//Show Participants Button Controller
				showBtn.setOnMouseEntered(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent event) {
						setCursor(Cursor.HAND);
						showBtn.setStyle(color.styleHover);
					}
				});
				
				showBtn.setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent event) {
						setCursor(Cursor.HAND);
						showBtn.setStyle(color.styleDefault);
					}
				});
				
				showBtn.setOnMousePressed(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent event) {
						setCursor(Cursor.HAND);
						showBtn.setStyle(color.styleDefault);
					}
				});
				
				showBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent event) {
						setCursor(Cursor.HAND);
						showBtn.setStyle(color.styleHover);
					}
				});
				
				showBtn.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent event) {
						if(!show){
							show = true;
							descriptionLabel.setVisible(false);
							descriptionTitleLabel.setVisible(false);
							participantsTitleLabel.setVisible(true);
							participants.setVisible(true);
							
							showBtn.setText("Hide participants");
							participants.setItems(employeedata);;
						}
						else if(show){
							show = false;
							participants.setVisible(false);
							participantsTitleLabel.setVisible(false);
							descriptionLabel.setVisible(true);
							descriptionTitleLabel.setVisible(true);
							showBtn.setText("Show participants");
						}
					}
				});
		
		//Add Everything
		getChildren().addAll(titleLabel, timeLabel, descriptionTitleLabel, descriptionLabel, delBtn, editBtn, participantsTitleLabel, participants, showBtn);
		AnchorPane.setRightAnchor(timeLabel, 5.0);
		AnchorPane.setTopAnchor(timeLabel, 5.0);
		AnchorPane.setTopAnchor(titleLabel, 5.0);
		AnchorPane.setLeftAnchor(titleLabel, 5.0);
		AnchorPane.setTopAnchor(descriptionTitleLabel, 40.0);
		AnchorPane.setLeftAnchor(descriptionTitleLabel, 5.0);
		AnchorPane.setTopAnchor(descriptionLabel, 55.0);
		AnchorPane.setLeftAnchor(descriptionLabel, 5.0);
		AnchorPane.setTopAnchor(participantsTitleLabel, 40.0);
		AnchorPane.setLeftAnchor(participantsTitleLabel, 5.0);
		AnchorPane.setTopAnchor(participants, 60.0);
		AnchorPane.setLeftAnchor(participants, 5.0);
		AnchorPane.setBottomAnchor(delBtn, 5.0);
		AnchorPane.setRightAnchor(delBtn, 5.0);
		AnchorPane.setBottomAnchor(editBtn, 5.0);
		AnchorPane.setRightAnchor(editBtn, 70.0);
		AnchorPane.setBottomAnchor(showBtn, 5.0);
		AnchorPane.setLeftAnchor(showBtn, 5.0);
		
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
					titleLabel.setClip(null);
					titleLabel.setPrefWidth(getPrefWidth()-10);
					timeLabel.setVisible(true);
					descriptionLabel.setPrefWidth(getPrefWidth()-10);
					descriptionLabel.setPrefHeight(getPrefHeight()-80);
					if(!show){
						descriptionTitleLabel.setVisible(true);
						descriptionLabel.setVisible(true);
					}
					else {
						participantsTitleLabel.setVisible(true);
						participants.setVisible(true);
					}
					participants.setPrefSize(getPrefWidth()-12, getPrefHeight()-105);
					delBtn.setVisible(true);
					editBtn.setVisible(true);
					showBtn.setVisible(true);
				}
				else{
					active = false;
					toBack();
					setPrefSize(panelWidth, panelHeight);
					setLayoutX(panelX);
					setLayoutY(panelY);
					titleLabel.setPrefWidth(getPrefWidth()-10);
					timeLabel.setVisible(false);
					descriptionLabel.setPrefWidth(0);
					descriptionLabel.setPrefHeight(0);
					participantsTitleLabel.setVisible(false);
					descriptionTitleLabel.setVisible(false);
					descriptionLabel.setVisible(false);
					participants.setPrefSize(0, 0);
					participants.setVisible(false);
					delBtn.setVisible(false);
					editBtn.setVisible(false);
					showBtn.setVisible(false);
				}
			}
		});
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.HAND);
				setStyle(color.styleHover);
				if(active) toFront();
				if(!active) toBack();
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				setCursor(Cursor.DEFAULT);
				setStyle(color.styleDefault);
				if(!active) toBack();
			}
		});
		
		
	}
	
	public void setColor(panelColors color){
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
	
	
}
