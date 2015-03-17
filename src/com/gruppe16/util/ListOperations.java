package com.gruppe16.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListOperations {

    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
    public static <T> List<T> hashToList(HashMap<Integer, T> in){
    	List<T> list = new ArrayList<T>();
    	
    	for(Map.Entry<Integer, T> t : in.entrySet()){
    		list.add(t.getValue());
    	}
    	return list;
    }
    
    public static <T> boolean contains(List<T> parent, List<T> sub){
    	for(T t : sub){
    		if(!parent.contains(t))
    			return false;
    	}
    	return true;
    }
    
}
