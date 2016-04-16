package hello_world_pkg;

import java.util.Collections;
import java.util.LinkedList;

public class ArraySort {
	
	private LinkedList<Integer> sortList;
	
	public ArraySort() {
		sortList = new LinkedList<Integer>();
		for (int i = 0; i < 100; i++) {
			sortList.add(i);
		}
		Collections.shuffle(sortList);
	}
	
	private void sort() {
		int j = 0;
		int temp = 0;
		for (int i = 1; i < sortList.size(); i++) {
			j = i;
			// Loop while we are greater than previous elements
			while (j > 0 && sortList.get(j-1) > sortList.get(j)) {
				// Swap the elements
				temp = sortList.get(j);
				sortList.set(j, sortList.get(j-1));
				sortList.set(j-1, temp);
				j--;
			}
		}
		System.out.println("Sorted list:");
		System.out.println(sortList.toString());
	}
	
	
	
	public static void main (String [] args) {
		ArraySort as = new ArraySort();
		as.sort();
	}

}
