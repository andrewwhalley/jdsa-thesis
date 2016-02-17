package ds_advisor_demo;
/**
 * Created by andrewwhalley on 14/08/15.
 */

import java.util.HashMap;
import java.util.Map;

class DSAdvisor {

    private Map<String, String> linkedListComplexity;
    private Map<String, String> arrayListComplexity;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int VALUE_FOR_N = 100000;
    private static final int INITIAL_SIZE = 1;

    private DSAdvisor() {
        linkedListComplexity = new HashMap<String, String>(INITIAL_SIZE);
        arrayListComplexity = new HashMap<String, String>(INITIAL_SIZE);
        linkedListComplexity.put("add", "n");
        arrayListComplexity.put("add", "1");
    }

    private static DSAdvisor createDS_Advisor() {
        return new DSAdvisor();
    }

    /**
     * look at populating and then getting the even numbers
     * as an example and then do a replace...
     * @param className
     */
    private void analyseClass (String className) {
    	int even = 0;
        if ("linkedListDemo".equals(className)) {
            StringBuilder sb = new StringBuilder(DSAdvisor.INITIAL_SIZE);

            long startTime = System.currentTimeMillis();
            linkedListDemo lld = linkedListDemo.createLinkedList_Demo(DSAdvisor.VALUE_FOR_N);
            long endTime = System.currentTimeMillis();

            sb.append("Time taken for LinkedList: ").append(endTime - startTime).append("ms.").
                    append(DSAdvisor.LINE_SEPARATOR);
            sb.append(className).append(" uses add ").append(DSAdvisor.VALUE_FOR_N).append(" times. The complexity of").
                    append(DSAdvisor.LINE_SEPARATOR);
            sb.append("add for a " + "LinkedList" + " is ").append(linkedListComplexity.get("add")).
                    append(DSAdvisor.LINE_SEPARATOR).append(DSAdvisor.LINE_SEPARATOR);

            sb.append("ArrayList add has complexity: ").append(arrayListComplexity.get("add")).append('.').
                    append(DSAdvisor.LINE_SEPARATOR);
            startTime = System.currentTimeMillis();
            arrayListDemo ald = arrayListDemo.createArrayList_Demo(DSAdvisor.VALUE_FOR_N);
            endTime = System.currentTimeMillis();
            sb.append("The same operations in an ArrayList will take: ").append(endTime - startTime).append("ms.").
                    append(DSAdvisor.LINE_SEPARATOR).append(DSAdvisor.LINE_SEPARATOR);
            sb.append("It is therefore recommended that an ArrayList be used").
                    append(" in place of the LinkedList.").append(DSAdvisor.LINE_SEPARATOR);

            startTime = System.currentTimeMillis();
            even = ald.getEven();
            endTime = System.currentTimeMillis();
            sb.append("ArrayList get even numbers: ").append(endTime - startTime).append("ms.").
                    append(DSAdvisor.LINE_SEPARATOR);
            startTime = System.currentTimeMillis();
            even = lld.getEven();
            endTime = System.currentTimeMillis();
            sb.append("LinkedList get even numbers: ").append(endTime - startTime).append("ms.").
                    append(DSAdvisor.LINE_SEPARATOR);
            String output = sb.toString();
            System.out.println(output);
        }
    }

    public static void main (String args []) {

        DSAdvisor dsa;
        dsa = createDS_Advisor();
        dsa.analyseClass("linkedListDemo");
    }

    @Override
    public String toString() {
        return "DSAdvisor{" +
                "linkedListComplexity=" + linkedListComplexity +
                ", arrayListComplexity=" + arrayListComplexity +
                '}';
    }
}
