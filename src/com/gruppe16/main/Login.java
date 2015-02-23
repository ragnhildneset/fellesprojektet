package com.gruppe16.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.security.MessageDigest;

import com.gruppe16.util.Digest;

public class Login extends Application implements Initializable {
	
	@FXML Button loginBtn;
	@FXML TextField user;
	@FXML TextField pass;
	
	static String username="a";
	static byte[] password = Digest.getHash("b");
	
	private Stage stage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try{
					if(user.getText().contentEquals(username) && Digest.equals(Digest.getHash(pass.getText()), password)){
						Main._init();
					} else {
						System.out.println("Wrong password or username.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		Scene scene = new Scene((AnchorPane) FXMLLoader.load(Main.class.getResource("/com/gruppe16/main/login.fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
