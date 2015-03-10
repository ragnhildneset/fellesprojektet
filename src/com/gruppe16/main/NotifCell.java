package com.gruppe16.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class NotifCell implements Initializable {
	
	@FXML Label c_title;
	@FXML Label c_desc;
	@FXML Label c_from;
	@FXML Label c_to;
	@FXML Label c_date;
	@FXML Label c_status;
	
	public static final int NOT_RESPONDED = 0, DECLINED = 1, ACCEPTED = 2;
	
	public void set(String title, String desc, String from, String to, String date, int status){
		c_title.setText(title);
		c_desc.setText(desc);
		c_from.setText(from);
		c_to.setText(to);
		c_date.setText(date);
		switch(status){
		case NOT_RESPONDED:
			c_status.setText("Not responded.");
			break;
		case DECLINED:
			c_status.setText("Declined.");
			break;
		case ACCEPTED:
			c_status.setText("Accepted.");
			break;
		}
	}
	
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
