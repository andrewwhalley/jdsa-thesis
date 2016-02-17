package ds_advisor_demo;
/**
 * Created by andrewwhalley on 14/08/15.
 */

import java.util.LinkedList;
import java.util.List;

class linkedListDemo {

    private List<Integer> demoList;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private linkedListDemo(int n) {
        super();
        this.demoList = new LinkedList<Integer>();
        this.singlePopulate();
        this.loopPopulate(n);
    }

    public static linkedListDemo createLinkedList_Demo(int n) {
        return new linkedListDemo(n);
    }

    private void singlePopulate() {
        this.demoList.add(0);
    }

    /**
     * Look into JIT optimisations
     * @param n - The number of entries to be added
     */
    private void loopPopulate( int n ) {
        for (int i = 1; i <= n; i++) {
            //demoList.add(i);
            this.demoList.add(i/2, i);
        }
    }

    public int getEven() {
        int num = 0;
        for (int i = 0; i < this.demoList.size(); i+=2) {
            num = this.demoList.get(i);
        }
        return num;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : this.demoList) {
            sb.append(i);
            sb.append(linkedListDemo.LINE_SEPARATOR);
        }
        return sb.toString();
    }
}
