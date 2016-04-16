package hello_world_pkg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class helloWorldDemo {

	private String testVar;
	private ArrayList<Integer> testArrayList;
	private List<Integer> testLinkedList;
	
	public helloWorldDemo() {
		testVar = "Hello World";
		testArrayList = new ArrayList<Integer> ();
		testLinkedList = new LinkedList<Integer> ();
	}
	
	public List<Integer> doStuff() {
		testArrayList.add(1);
		testLinkedList.add(1);
		int j = 2;
		String testNull = "";
		/**
		 *  @JDSA:j:small
		 */
		while (j < 3) {
			testArrayList.add(j);
			testLinkedList.add(j);
			j++;
		}
		while (testNull != null) {
			if (j > 6) {
				testNull = null;
			}
			j++;
		}
		System.out.println("Past while");
		for (int i=3; i<101; i++) {
			testArrayList.add(i);
			testLinkedList.add(i);
		}
		System.out.println("Past 101 loop");
		for (Integer k : testArrayList) {
			testLinkedList.add(k + 1000);
		}
		System.out.println("Past arraylist foreach");
		// Testing nested loop. Should get O(n^2)
		for (Integer b : testArrayList) {
			for (Integer l : testLinkedList) {
				testArrayList.add(l+b+1000);
			}
		}
		System.out.println("Past linked list foreach");
		// Shouldn't get O(n) for the size call here. Need to fix that part.
		for (int i=1; i<testArrayList.size(); i++) {
			testLinkedList.add(i^2 + 2000);
		}
		int tempVar = 0;
		for (int i = 0; i < testLinkedList.size(); i++) {
			tempVar = testLinkedList.get(i);
			if (testArrayList.size() < tempVar) {
				testArrayList.remove(tempVar);
			}
		}
		// Add a method that isn't available for LinkedLists 
		// (It just adds an O() method and doesn't error - Expected behaviour)
		testArrayList.ensureCapacity(testArrayList.size());
		// Testing chained methods (Should just be get - it is :) )
		testArrayList.get(0).intValue();
		
		System.out.println("Past Array list size loop");
		System.out.println(testVar);
		System.out.println((testArrayList.get(testArrayList.size()-1) - testArrayList.get(0)));
		System.out.println(testLinkedList.get(testLinkedList.size()-1));
		return testArrayList;
	}
	
	public static void main (String args []) {
		List<Integer> dupeList = new ArrayList<Integer>();
		helloWorldDemo hwd = new helloWorldDemo();
		dupeList = hwd.doStuff();
	}
}
