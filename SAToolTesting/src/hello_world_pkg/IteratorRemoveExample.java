package hello_world_pkg;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IteratorRemoveExample {
	
	public static void main(String [] args) {
		List<String> myList = new LinkedList<String>();
		String addString = "My number: ";
		for (int i=0; i < 100; i++) {
			myList.add(addString + i);
		}
		
		Iterator<String> myListIterator = myList.iterator();
		while (myListIterator.hasNext()) {
			myListIterator.next();
			myListIterator.remove();
		}
		System.out.println("Size: " + myList.size());
	}
}