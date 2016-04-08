package hello_world_pkg;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BinarySearchList {
	
	static List<Integer> searchList;
	
	private static void processSearch(int n) {
		searchList = new LinkedList<Integer>();
		Random rand = new Random();
		int i = 0;
		int searchVal = rand.nextInt(n)*3;
		if (searchVal > n) searchVal = 3*n; 
		int actualIndex = -1;
		while (i <= n) {
			searchList.add(3*i);
			i++;
		}
		long startTime = System.currentTimeMillis();
		actualIndex = binarySearch(searchList, searchVal);
		long finishTime = System.currentTimeMillis();
		if (actualIndex == -1) {
			System.out.println("Couldn't find: " + searchVal);
			return;
		}
		System.out.println("Found searchValue: " + searchVal + " at index: " + actualIndex);
		System.out.println("Actual value at index: " + actualIndex + " - " + searchList.get(actualIndex));
		System.out.println("Elapsed time: " + (finishTime - startTime) + " for list type: " + searchList.getClass());
	}
	
	private static int binarySearch(List<Integer> l, int searchValue) {
		int foundIndex = -1; // -1 will never be in the list being searched
		int midPt;
		int lowerBound = 0;
		int upperBound = l.size();
		int currValue = -1;
		if (l.size() == 1) {
			return l.get(0) == searchValue? 0 : -1;
		}
		while (lowerBound <= upperBound) {
			midPt = (lowerBound+upperBound)/2;
			currValue = l.get(midPt);
			if (currValue == searchValue) {
				foundIndex = midPt;
				break;
			}
			if (currValue > searchValue) {
				upperBound = midPt - 1;
			}
			if (currValue < searchValue) {
				lowerBound = midPt + 1;
			}
		}
		return foundIndex;
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			return;
		}
		int n = 0;
		try {
			n = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.err.println("Need to provide an integer value for n");
		}
		processSearch(n);
	}
}
