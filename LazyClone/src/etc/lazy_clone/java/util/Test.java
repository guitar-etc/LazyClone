package etc.lazy_clone.java.util;

import java.util.*;

public class Test {
	public static void main(String[] args) {
		var al = new LCArrayList<>();
		
		al.add(null);
		
		log(al);
		
		var alClone = al.clone();
		var temp = al.clone();
		log(alClone);
		log(al);
		
		al.add(null);
		
		log(alClone);
		log(al);
		
		temp.close();
		alClone.add("bye");
		
		log(alClone);
		log(al);
	}
	public static void log(Object...objects) {
		System.out.println(Arrays.deepToString(objects));
	}
}
