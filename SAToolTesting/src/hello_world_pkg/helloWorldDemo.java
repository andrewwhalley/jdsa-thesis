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
		while (j < 3) {
			testArrayList.add(j);
			testLinkedList.add(j);
			j++;
		}
		for (int i=3; i<101; i++) {
			testArrayList.add(i);
			testLinkedList.add(i);
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
