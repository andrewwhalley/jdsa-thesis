package thesis.pmd.fragment.initial;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class JavaCollectionsComplexities {
	
	// TODO: Lookup WeakHashMap and SynchronousQueue (Concurrent) data types
	
	private static final Set<String> ConcurrentDS = new HashSet<String>(Arrays.asList("CopyOnWriteArrayList",
			"ConcurrentHashMap", "ConcurrentSkipListMap", "CopyOnWriteArraySet", "ConcurrentSkipListSet", 
			"ConcurrentLinkedQueue", "ArrayBlockingQueue", "PriorityBlockingQueue", "DelayQueue",
			"LinkedBlockingQueue", "LinkedBlockingDequeue", "SynchronousQueue"));

	public static final HashMap<String, Complexity> ArrayListComplexities = new HashMap<String, Complexity>() {
		/**
		 * From the Java Docs (http://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html):
		 * The size, isEmpty, get, set, iterator, and listIterator operations run in constant time. 
		 * The add operation runs in amortized constant time, that is, adding n elements requires O(n) time. 
		 * All of the other operations run in linear time (roughly speaking). 
		 * The constant factor is low compared to that for the LinkedList implementation.
		 * Also:
		 * http://stackoverflow.com/questions/322715/when-to-use-linkedlist-over-arraylist
		 */
		private static final long serialVersionUID = -5175333366556841249L;

	{
		// Note that adding at a given index is O(n), which is the same for LinkedList
		// So it has not been included
		put("add", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("addAll", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("clear", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("clone", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("contains", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("ensureCapacity", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("get", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("indexOf", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("isEmpty", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("iterator", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("lastIndexOf", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("listIterator", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("removeAll", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("removeRange", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("retainAll", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("set", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("subList", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("toArray", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("toString", new Complexity(null, new Polynomial(1), null)); // O(1)
		put("trimToSize", new Complexity(null, new Polynomial(1), null)); // O(n)
		
		// Methods for the ArrayList iterator
		put("Iterator.hasNext", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("Iterator.next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("Iterator.remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove with Iterator
		// Methods for the ArrayList listIterator
		put("ListIterator.add", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("ListIterator.hasNext", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.hasPrevious", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.nextIndex", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.previous", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.previousIndex", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove with Iterator
		put("ListIterator.set", new Complexity(null, new Polynomial(0), null)); // O(1)
	}};
	
	public static final HashMap<String, Complexity> LinkedListComplexities = new HashMap<String, Complexity>() {
		/**
		 * Java Docs (http://docs.oracle.com/javase/7/docs/api/java/util/Deque.html)
		 * All of the operations perform as could be expected for a doubly-linked list. 
		 * Operations that index into the list will traverse the list from the
		 * beginning or the end, whichever is closer to the specified index.
		 * 
		 * Note: Deque methods have been ignored as they cannot be compared with ArrayLists
		 */
		private static final long serialVersionUID = -7487937071188392987L;

	{
		// Note that adding at a given index is O(n), which is the same for ArrayList
		// So it has not been included
		put("add", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("addAll", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("clear", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("clone", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("contains", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("get", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("indexOf", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("isEmpty", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("iterator", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("lastIndexOf", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("listIterator", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(0), null)); // O(1) - Remove Object
		put("removeAll", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("set", new Complexity(null, new Polynomial(1), null)); // O(n) (Has to find element before it can replace)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("toArray", new Complexity(null, new Polynomial(1), null)); // O(1)
		put("toString", new Complexity(null, new Polynomial(1), null)); // O(1)
		
		// Methods for the LinkedList Iterator
		put("Iterator.hasNext", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("Iterator.next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("Iterator.remove", new Complexity(null, new Polynomial(0), null)); // O(1) - Remove with Iterator
		// Methods for the LinkedList ListIterator
		put("ListIterator.add", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.hasNext", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.hasPrevious", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.nextIndex", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.previous", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.previousIndex", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("ListIterator.remove", new Complexity(null, new Polynomial(0), null)); // O(1) - Remove with Iterator
		put("ListIterator.set", new Complexity(null, new Polynomial(0), null)); // O(1)
	}};
	
	public static final HashMap<String, Complexity> CopyOnWriteArrayListComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("add", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("contains", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("Iterator.remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove with Iterator
		
	}};
	
	public static final HashMap<String, Complexity> HashMapComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("containsKey", new Complexity(null, new Polynomial(1), null)); // O(n)
		// TODO: Add a way to represent a complexity like h/n.
		put("next", new Complexity(null, new Polynomial(1), null)); // O(h/n) where h is table capacity 
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> LinkedHashMapComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("containsKey", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> IdentityHashMapComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(h/n)
		put("containsKey", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> EnumMapComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("containsKey", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> TreeMapComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("next", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("containsKey", new Complexity(null, null, new Logarithm())); // O(log(n))
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> ConcurrentHashMapComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(h/n)
		put("containsKey", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> ConcurrentSkipListMapComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("next", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("containsKey", new Complexity(null, null, new Logarithm())); // O(log(n))
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> HashSetComplexities = new HashMap<String, Complexity>() {{
		
		put("add", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("contains", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(h/n)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> LinkedHashSetComplexities = new HashMap<String, Complexity>() {{
		
		put("add", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("contains", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> CopyOnWriteArraySetComplexities = new HashMap<String, Complexity>() {{
		
		put("add", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("contains", new Complexity(null, new Polynomial(1), null)); // O(n)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> EnumSetComplexities = new HashMap<String, Complexity>() {{
		
		put("add", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("contains", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("next", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> TreeSetComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("next", new Complexity(null, null, new Logarithm())); // O(1)
		put("contains", new Complexity(null, null, new Logarithm())); // O(log(n))
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> ConcurrentSkipListSetComplexities = new HashMap<String, Complexity>() {{
		
		put("get", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("next", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("contains", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Fix the following complexities
		put("put", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("remove", new Complexity(null, new Polynomial(1), null)); // O(n) - Remove Object
		put("values", new Complexity(null, new Polynomial(1), null)); // O(n) - With Iterator
		
	}};
	
	public static final HashMap<String, Complexity> PriorityQueueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> ConcurrentLinkedQueueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(1), null)); // O(n)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> ArrayBlockingQueueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> LinkedBlockingQueueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> PriorityBlockingQueueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> DelayQueueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, null, new Logarithm())); // O(log(n))
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> LinkedListQueueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> ArrayDequeComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	public static final HashMap<String, Complexity> LinkedBlockingDequeueComplexities = new HashMap<String, Complexity>() {{
		
		put("offer", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("peak", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("poll", new Complexity(null, new Polynomial(0), null)); // O(1)
		put("size", new Complexity(null, new Polynomial(0), null)); // O(1)
		// TODO: Check other methods
	}};
	
	private JavaCollectionsComplexities () {
		
	}
	
	/**
	 * Return the associated HashMap for the data structure passed through
	 * @param type - the string name of the data structure being used in the code
	 * @return the hashmap of complexities for the given type name
	 */
	public static HashMap<String, Complexity> DSDetermination (String type) {
		// Based off the runtime type determined by the program, give it the
		// associated complexity map
		switch (type) {
		case "ArrayList":
			return ArrayListComplexities;
		case "LinkedList":
			return LinkedListComplexities;
		case "CopyOnWriteArrayList":
			return CopyOnWriteArrayListComplexities;
		}
		return null;
	}
	
	/**
	 * Return a list of data structures that can be compared to the given data structure
	 * @param type - the given data structure
	 * @return An array of data structures derived from the same interface (comparable)
	 */
	public static String [] DSToCompareTo (String type) {
		switch (type) {
		// Only compare the List Interface types against themselves
		// N.B. CopyOnWriteArrayList is concurrent and can't be compared to the others
		case "ArrayList":
			return new String[]{"LinkedList"};
		case "LinkedList":
			return new String[]{"ArrayList"};
		case "CopyOnWriteArrayList":
			return new String[]{};
		}
		
		return null;
	}
	
	public static Boolean IsDSConcurrent (String type) {
		return ConcurrentDS.contains(type);
	}
	
}
