package com.gruppe16.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Class ListOperations.
 * 
 * @author Gruppe 16
 */
public class ListOperations {

    /**
     * Returns the union of two lists.
     *
     * @param <T> the generic type
     * @param list1 one of the lists
     * @param list2 one of the lists
     * @return the union list
     */
    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    /**
     * Checks if a sublist is contained in a parent list.
     *
     * @param <T> the generic type
     * @param parent the parent list
     * @param sub the sub list
     * @return true, if successful
     */
    public static <T> boolean contains(List<T> parent, List<T> sub){
    	for(T t : sub){
    		if(!parent.contains(t))
    			return false;
    	}
    	return true;
    }
    
}
