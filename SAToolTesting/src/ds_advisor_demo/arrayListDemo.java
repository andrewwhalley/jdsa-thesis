package ds_advisor_demo;
/**
 * Created by andrewwhalley on 14/08/15.
 */

import java.util.ArrayList;
import java.util.List;

class arrayListDemo {

    private List<Integer> demoList;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int INITIAL_SIZE = 1;

    private arrayListDemo(int n) {
        super();
        this.demoList = new ArrayList<Integer>(INITIAL_SIZE);
        this.singlePopulate();
        this.loopPopulate(n);
    }

    public static arrayListDemo createArrayList_Demo(int n) {
        return new arrayListDemo(n);
    }

    private void singlePopulate() {
        this.demoList.add(0);
    }

    private void loopPopulate( int n ) {
        for (int i = 1; i <= n; i++) {
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
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }
}
