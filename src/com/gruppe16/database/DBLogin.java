package com.gruppe16.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DBLogin {
	
	public static void main(String args[]){
		login();
	}

	private static String user = null;
	private static String pass = null;
	private static String url = null;
	
	private final static String path = "login.txt";

	private static ArrayList<String> ls = new ArrayList<String>();
	
	public static void login() {
		try {
			BufferedReader r = new BufferedReader(new FileReader(path));
			String line;
			while((line = r.readLine()) != null){
				ls.add(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("You do not have login.txt!");
		} catch (IOException e) {
			System.err.println("login.txt was found, but the file may be currupt.");
		}
		try{
			user = ls.get(0);
			pass = ls.get(1);
			url = ls.get(2);
			System.out.println("Username, password and url to db is now set.");
			System.out.println("User:\t" + user);
			System.out.println("Pass:\t" + pass);
			System.out.println("URL:\t" + url);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("login.txt was found, but it's not correctly formatted.");
		}
	}

	public static String getUser() {
		return user;
	}

	public static String getPass() {
		return pass;
	}
	
	public static String getURL() {
		return url;
	}

}
