package tjacobs.animation;

/**
 * The reason d'etre for PrimaryKey is to make overriding equals, hashCode, and compareTo simpler and easier to
 * implement so that they are consistent with one another in data object classes.
 * @author tom_jacobs
 *
 * @param <T>
 */
public interface PrimaryKey<T> {
	public Object[] getPrimaryKey();
	//public boolean equals(T obj);
	public int hashCode();
	
	public interface ComparablePrimaryKey<T> extends Comparable<T>, PrimaryKey<T> {
		@SuppressWarnings("unchecked")
		public Comparable[] getPrimaryKey();
		
	}
	
	public static class Impl {
		@SuppressWarnings("unchecked")
		public static final boolean equalsImpl(PrimaryKey key1, PrimaryKey key2) {
			Object[] data1 = key1.getPrimaryKey();
			Object[] data2 = key2.getPrimaryKey();
			//don't check length equal - let that get thrown as an exception
			try {
				for (int i = 0; i < data1.length; i++) {
					if (data1[i] == null && data2[i] == null) continue;
					if (data1[i] == null || data2[i] == null) return false;
					if (!data1[i].equals(data2[i])) return false;
				}
			}
			catch (ArrayIndexOutOfBoundsException ex) {
				//throw new Error("Class: " + key1.getClass() + " has invalid implementation of getPrimaryKey");
				ex.printStackTrace();
				return false;
			}
			return true;
		}
		
		@SuppressWarnings("unchecked")
		public static final int compareTo(ComparablePrimaryKey key1, ComparablePrimaryKey key2) {
			Comparable[] data1 = key1.getPrimaryKey();
			Comparable[] data2 = key2.getPrimaryKey();
			for (int i = 0; i < data1.length; i++) {
				System.out.println("comparing: " + data1[i] + " to " + data2[i]);
				int compare = data1[i].compareTo(data2[i]);
				if (compare != 0) {
					System.out.println("returning: " + compare);
					return compare;
				}
			}
			return 0;
		}
		
		@SuppressWarnings("unchecked")
		public static final int hashCode(PrimaryKey key) {
			Object[] data1 = key.getPrimaryKey();
			long hashCode = 0;
			for (int i= 0; i < data1.length; i++) {
				hashCode += data1[i].hashCode();
			}
			return (int) hashCode;
		}
		
		/*
		 * model equals, hashCode, compareTo methods
		 
		public boolean equals(Object o) {
			if (o.getClass() != getClass()) {
				return false;
			}
			return PrimaryKey.Impl.equalsImpl( this, o);
		}
		
		public int hashCode() {
			return PrimaryKey.Impl.hashCode(this);
		}

		 public int compareTo(PrimaryKey obj) {
		 	return PrimaryKey.Impl.compareTo(this, obj);
		 }

		*/
				
	}
	
	
}
