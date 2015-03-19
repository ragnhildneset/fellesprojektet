package com.gruppe16.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * The Class DBLogin. Used to log in to the database from a file login.txt
 *
 * @author Gruppe 16
 */
public class DBLogin {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		login();
	}

	/** The user. */
	private static String user = null;
	
	/** The pass. */
	private static String pass = null;
	
	/** The url. */
	private static String url = null;

	/** The Constant path. */
	private final static String path = "login.txt";

	/** The ls. */
	private static ArrayList<String> ls = new ArrayList<String>();


	/**
	 * Login.
	 */
	public static void login() {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(path));
			String line;
			while ((line = r.readLine()) != null) {
				ls.add(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("You do not have login.txt!");
		} catch (IOException e) {
			System.err
					.println("login.txt was found, but the file may be currupt.");
		} finally {
			try {
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			user = ls.get(0);
			pass = ls.get(1);
			url = ls.get(2);
			System.out.println("Username, password and url to db is now set.");
		} catch (IndexOutOfBoundsException e) {
			System.err.println("login.txt was found, but it's not correctly formatted.");
		}
	}


	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public static String getUser() {
		return user;
	}


	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public static String getPass() {
		return pass;
	}


	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public static String getURL() {
		return url;
	}

}
