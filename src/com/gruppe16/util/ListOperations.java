package com.gruppe16.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The Class ListOperations. Uses magic.
 */
public class ListOperations {

    /**
     * Union.
     *
     * @param <T> the generic type
     * @param list1 the list1
     * @param list2 the list2
     * @return the list
     */
    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    /**
     * Contains.
     *
     * @param <T> the generic type
     * @param parent the parent
     * @param sub the sub
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
