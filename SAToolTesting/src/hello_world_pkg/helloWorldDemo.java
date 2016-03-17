package hello_world_pkg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
		for (int i=3; i<101; i++) {
			testArrayList.add(i);
			testLinkedList.add(i);
		}
		for (Integer k : testArrayList) {
			testLinkedList.add(k + 1000);
		}
		for (Integer l : testLinkedList) {
			testArrayList.add(l+1000);
		}
		for (int i=1; i<testLinkedList.size(); i++) {
			testLinkedList.add(i^2 + 2000);
		}
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
