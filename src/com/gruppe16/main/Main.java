package com.gruppe16.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Scene scene = new Scene(new Pane());
		arg0.setScene(scene);
		arg0.show();
	}

}
