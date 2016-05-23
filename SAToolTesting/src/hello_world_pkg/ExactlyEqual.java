package hello_world_pkg;

import java.util.ArrayList;

public class ExactlyEqual {

	public static void main(String[] args) {
		ArrayList<Integer> myList = new ArrayList<Integer>();
		for (int i=0; i < 86; i+=3) {
			myList.add(i % 29);
		}
		if (myList.contains(7)) {
			System.out.println("Contains 7");
		} else {
			System.out.println("Doesn't contain 7");
		}
		System.out.println(myList.toString());
		System.out.println(myList.size());
	}

}
