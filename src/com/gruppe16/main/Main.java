package com.gruppe16.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main {

	public static void _init(){
		try {
			Scene scene;
			scene = new Scene((AnchorPane) FXMLLoader.load(Main.class.getResource("/com/gruppe16/main/mainPane.fxml")));
			Stage arg0 = new Stage();
			arg0.setScene(scene);
			arg0.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
