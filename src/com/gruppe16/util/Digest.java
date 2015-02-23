package com.gruppe16.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Digest {

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
