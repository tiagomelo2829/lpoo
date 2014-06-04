package tjacobs.animation;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * Some basic array utilities beyond what's in the java.lang.Arrays class
 * (mostly just making output to a PrintStream a 1 liner)
 */

public class Arrays {

	private Arrays() {
		super();
	}
	
	/**
	 * Print an int array to Standard Out
	 * @param array array of ints to print
	 */
	public static void printIntArray(int array[]) {
		printIntArray(array, System.out);
	}
	
	/**
	 * Print an int array to the given stream
	 * @param array array of ints to print
	 * @param out PrintStream to print the int array to
	 */
	public static void printIntArray(int array[], PrintStream out) {
		out.print("DEBUG PRINT ARRAY: ");
		for (int i = 0; i < array.length; i++) {
			out.print(array[i] + " ");
		}
		out.println();
	}
	
	/**
	 * Print an array of objects using Object.toStream()
	 * to Standard out
	 * @param array
	 */
	public static void printArray(Object array[]) {
		printArray(array, System.out);
	}

	/**
	 * Print an array of objects using Object.toStream()
	 * to the given PrintStream
	 * @param array
	 * @param out
	 */
	public static void printArray(Object array[], PrintStream out) {
		for (int i = 0; i < array.length; i++) {
			out.print((array[i] == null ? "null" : array[i].toString()) + " ");
		}
		out.println();
	}
	
	/**
	 * Compare if 2 arrays are equal
	 * <P>
	 * comparisions:<ul><li>if list is null</li><li>if lengths are not equals</li><li>if one or both list objects at a common index are null</li><li>if list items at a common index are equal</li></ul> 
	 * @param ar1
	 * @param ar2
	 * @return
	 */
	public static boolean arraysEqual(Object[] ar1, Object[] ar2) {
		if (ar1 == null && ar2 == null) return true;
		if (ar1 == null || ar2 == null) return false;
		if (ar1.length != ar2.length) return false;
		for (int i = 0; i < ar1.length; i++) {
			if (ar1[i] == null) {
				if (ar2[i] == null) continue;
				return false;
			} else if (ar2[i] == null) return false;
			if (!ar1[i].equals(ar2[i])) return false;
		}
		return true;
	}
		
	public static void arraysEqualTest() {
		String items[] = new String[] {"a", "b"};
		Object items2[] = new Object[] {"a", "b"};
		arraysEqual(items, items2);
	}
		
	public static void reverse(Object obj[]) {
		int len = obj.length;

		for (int i = 0; i < len / 2; i++) {
			Object tmp = obj[i];
			obj[i] = obj[len - 1 - i];
			obj[len - 1 - i] = tmp;
		}
	}

	public static class ArrayIterator<T> implements Iterator<T> {
		private T[] mVals;
		private int index = 0;
		/**
		 * @throws NullPointerException if the input argument is null
		 * @param ar
		 */
		public ArrayIterator (T[] ar) {
			if (ar == null) throw new NullPointerException();
			mVals = ar;
		}
		
		@Override
		public boolean hasNext() {
			
			return mVals.length > index;
		}

		@Override
		public T next() {
			return mVals[index++];	
		}

		@Override
		public void remove() {
				
		}
	}
	
	public static class ArrayIterable<T> implements Iterable<T> {
		private T[] mVals;
		public ArrayIterable(T[] vals) {
			mVals = vals;
		}
		public Iterator<T> iterator() {
			return new ArrayIterator<T>(mVals);
		}
	}

}