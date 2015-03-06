package com.gruppe16.util;

public class Tuple<Value extends Comparable>{
	
	public Value a;
	public Value b;
	
	public Tuple(Value a, Value b){
		this.a = a;
		this.b = b;
	}
	
	public boolean equals(Tuple b){
		if(b.a.equals(this.a)&&b.b.equals(this.b))
			return true;
		return false;
	}
	
	
	
}