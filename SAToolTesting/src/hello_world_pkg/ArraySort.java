package hello_world_pkg;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArraySort {
	
	public static void main (String [] args) {
		LinkedList<Integer> sortList = new LinkedList<Integer>();
		for (int i = 0; i < 100; i++) {
			sortList.add(i);
		}
		Collections.shuffle(sortList);
		sort(sortList);
		System.out.println("Sorted list:");
		System.out.println(sortList.toString());
	}
	
	private static void sort(List<Integer> A) {
		int j = 0;
		int temp = 0;
		for (int i = 1; i < A.size(); i++) {
			j = i;
			// Loop while we are greater than previous elements
			while (j > 0 && A.get(j-1) > A.get(j)) {
				// Swap the elements
				temp = A.get(j);
				A.set(j, A.get(j-1));
				A.set(j-1, temp);
				j--;
			}
		}
	}
}
