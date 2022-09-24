package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A variant of the ApptBook ADT that follows the Collection model.
 * In particular, it has no sense of a current element.
 * All access to elements by the client must be through the iterator.
 * The {@link #add(Appointment)} method should add at the correct spot in sorted order in the collection.
 */
public class NewApptBook extends AbstractCollection<Appointment> implements Cloneable {
	// TODO: Add all the contents here.
	// Remember:
	// - All public methods not marked @Override must be fully documented with javadoc
	// - A @Override method must be marked 'required', 'implementation', or 'efficiency'
	// - You need to define and check the data structure invariant
	//   (essentially the same as in Homework #2)
	// - You should define a nested iterator class called MyIterator (with its own data structure), 
	//   and then the iterator() method simply returns a new instance.
	// You are permitted to copy in any useful code/comments from the Homework #2 solution.
	// But do not include any of the cursor-related methods, and in particular,
	// make sure you have no "currentIndex" field.
	
	private int manyItems;
	private int version;
	private Appointment[] data;
	
	public NewApptBook() {
		manyItems = 0;
	}
	
	private boolean wellFormed() {
		
		//If data is empty, return false
		if (this.data == null){
			return false;
		}
		
		//if data array length is less than the amount of elements, return false
		if (this.data.length < this.manyItems) {
			return false;
		}
		
		//checks to see if each element is null and if it is out of order by checking if the
		//element before is greater than the element after it.
		for (int i = 0; i < manyItems; ++i) {
			if (this.data[i] == null) {
				return false;
			}
			else if (i > 0 && this.data[i-1].compareTo(data[i]) > 0) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override //implementation
	public int size() {
		return data.length;
	}
	
	@Override
	public Iterator<Appointment> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class MyIterator implements Iterable<Appointment>, Iterator<Appointment> // TODO: what should this implement?	
	{
		private int current;
		private int myVersion;
		private int next;
		
		// The nested MyIterator class should use the following
		// invariant checker:
		public boolean wellFormed() {
			if (!NewApptBook.this.wellFormed()) return false;
			if (version != myVersion) return true; // not my fault if invariant broken
			if (current < 0 || current > manyItems) return report("current out of range: " + current + " not in range [0," + manyItems + "]");
			if (next < 0 || next > manyItems) return report("next out of range: " + next + " not in range[0," + manyItems + "]");
			if (next != current && next != current + 1) return report("next " + next + " isn't current or its successor (current = " + current + ")");
			return true;
		}


		private static boolean doReport = true;
		private boolean report(String error) {
			if (doReport) {
				System.out.println("Invariant error: " + error);
			}
			return false;
		}

		@Override //implementation
		public boolean hasNext() {
			return this.current + 1 < manyItems;
		}

		@Override //implementation
		public Appointment next() {
			if (!hasNext()) {
				this.next = manyItems;
				throw new NoSuchElementException();
			}
			else {
				this.current++;
			}
			return data[this.current];
		}

		@Override //implementation
		public Iterator<Appointment> iterator() {
			this.current = -1;
			return this;
		}
		
		public Iterator<Appointment> iterator(Appointment o) {
			
			if (o != null) {
				for (Appointment element: this) {
					for (int i = 0; i < manyItems; ++i) {
						if (o.compareTo(element) == 0) {
							this.current = i;
							break;
						}
					}
				}
			}
			
			return this;
		}
		
		
	}




}
