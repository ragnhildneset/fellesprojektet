package com.gruppe16.main;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddAppointment extends Application implements Initializable {
	
	Data data;
	
	class Data{
		
		String formaal, rom;
		LocalTime from, to;
		LocalDate endDate, startDate;
		int repetitions;
		
		@Override
		public String toString() {
			return "Data [formaal=" + formaal + ", rom=" + rom + ", from="
					+ from + ", to=" + to + ", endDate=" + endDate
					+ ", startDate=" + startDate + ", repetitions="
					+ repetitions + "]";
		}
		
		public Data(String formaal, String rom, LocalDate date, String from, String to)
				throws Exception {
			this.formaal = formaal;
			if(!rom.matches("^.*\\w\\d*$"))
				throw new Exception("Feltet for rom er ikke formatert riktig.");
			this.rom = rom;
			this.startDate = date;
			this.endDate = endDate;
			this.from = LocalTime.parse(from, DateTimeFormatter.ofPattern("HH:mm")); 
			this.to = LocalTime.parse(to, DateTimeFormatter.ofPattern("HH:mm"));
			this.repetitions = repetitions;
		}
		
		
	}
	
	@FXML
	private Button sendid;
	@FXML
	private TextField tilid;
	@FXML
	private TextField room;
	@FXML
	private TextField fraid;
	@FXML
	private TextField formaal;
	@FXML
	private DatePicker dateid;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		sendid.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try{
					data = new Data(formaal.getText(), room.getText(), dateid.getValue(), fraid.getText(),tilid.getText());
					System.out.println(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}

	@Override
	public void start(Stage arg0) throws Exception {
		AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/com/gruppe16/main/AddAppointment.fxml"));
		Scene scene = new Scene(root, 600, 509);
		arg0.setScene(scene);
		arg0.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
