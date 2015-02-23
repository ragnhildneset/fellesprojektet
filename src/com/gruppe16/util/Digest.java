package com.gruppe16.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public abstract class Digest {
	
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		while(true){
			String pe = in.nextLine();
			print(getHash(pe));
		}
	}
	
	public static void print(byte[] a){
		String p = "";
		for(byte e : a){
			p += String.valueOf(Character.toChars(e + 128)[0]) + "";
		}
		System.out.println(p);
	}

	public static byte[] getHash(String password) {
		try {
			MessageDigest digest;
			digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			byte[] input;
			input = digest.digest(password.getBytes("UTF-8"));
			return input;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	
	public static boolean equals(byte[] a, byte[] b){
		if(a.length != b.length){
			return false;
		}
		for(int i = 0; i < a.length; i++){
			if(a[i] != b[i]){
				return false;
			}
		}
		return true;
	}
	
}
