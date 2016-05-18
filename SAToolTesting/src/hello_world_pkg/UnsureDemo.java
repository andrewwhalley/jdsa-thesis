package hello_world_pkg;

import java.util.LinkedList;
import java.util.ListIterator;

public class UnsureDemo {
	
	public static void main (String [] args) {
		LinkedList<Integer> myList = new LinkedList<Integer>();
		for (int i = 0; i < 1000; i++) {
			myList.add(i);
		}
		ListIterator<Integer> li = myList.listIterator();
		int value = 20;
		while (li.hasNext()) {
			Integer n = li.next();
			if (n % 17 == 0) {
				li.remove();
				li.next();
			} 
			if (n % 7 == 0) {
				li.remove();
				li.next();
			} 
			if (n % 83 == 0) {
				li.add(value);
				li.next();
			}
			value += 20*value;
		}
		for (int i = 0; i < myList.size(); i+= 13) {
			if (!myList.contains(i)) {
				System.out.println("myList doesn't contain: " + i);
			}
		}
	}
}
