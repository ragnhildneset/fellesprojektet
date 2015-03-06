package com.gruppe16.util;

public class Tuple<Value extends Comparable>{
	
	Value a;
	Value b;
	
	Tuple(Value a, Value b){
		this.a = a;
		this.b = b;
	}
	
	public boolean equals(Tuple b){
		if(b.a.equals(this.a)&&b.b.equals(this.b))
			return true;
		return false;
	}
	
}