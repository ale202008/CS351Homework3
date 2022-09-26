package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
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
	
	//Constructor of NewApptBook
	public NewApptBook() {
		this.data = new Appointment[0];
		version = 0;
		manyItems = 0;
	}
	
	//Constructor of NewApptBook with a given capacity
	public NewApptBook(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException();
		}
		this.data = new Appointment[capacity];
		version = 0;
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
	
	
	@Override //required
	public int size() {
		return manyItems;
	}

	/**
	 * Basically copied from my Homework 2 method for ensureCapacity.
	 * Will probably optimize using Homework 2 solutions later one.
	 * @param minimumCapacity
	 */
	
	private void ensureCapacity(int minimumCapacity) {
		if (minimumCapacity >= Integer.MAX_VALUE) {
			throw new OutOfMemoryError();
		}

		//Copied from ApptBook Homework 2
		if(data.length == 0) {
			Appointment[] temp = new Appointment[minimumCapacity];
			data = temp;
		}
		else if (minimumCapacity/data.length >= 1 && minimumCapacity/data.length <= 2) {
			Appointment[] temp = new Appointment[2*data.length];
			for (int i = 0; i < data.length; ++i) {
				temp[i] = data[i];
			}
			data = temp;
		}
		else if (minimumCapacity/data.length > 2) {
			Appointment[] temp = new Appointment[minimumCapacity];
			for (int i = 0; i < data.length; ++i) {
				temp[i] = data[i];
			}
			data = temp;
		}
		
		if (data.length < minimumCapacity) {
			Appointment[] temp = new Appointment[minimumCapacity];
			for (int i = 0; i < data.length; ++i) {
				temp[i] = data[i];
			}
		}
	}
	
	/**
	 * add methods is basically a really simple implementation of the
	 * insert method given from Homework 2 solutions. Currently, does not
	 * add elements in between the sequence, but will probably
	 * add that later.
	 */
	
	@Override //implementation
	public boolean add(Appointment o) {
		assert wellFormed();
		
		if (o == null) {
			throw new NullPointerException();
		}
		
		ensureCapacity(manyItems+1);
		
		if (manyItems == 0) {
			data[0] = o;
		}
		else if (manyItems > 0) {		
			int i;
			for (i = manyItems; i > 0 && data[i-1].compareTo(o) > 0; --i) {
				data[i] = data[i-1];
			}
			data[i] = o;
		}

		manyItems++;
		version++;
		
		assert wellFormed();
		
		return true;
		
	}
	
	public boolean addAll(NewApptBook collection) {
		/**
		 * Used homework 2 solution of insertAll and implemented it
		 * for the addAll method.
		 */
		if (collection.manyItems == 0) {
			return false;
		}
		
		ensureCapacity(manyItems + collection.manyItems);
		
		if (collection == this) {
			collection = collection.clone();
		}
		
		for (int i = 0; i < collection.manyItems; ++i) {
			this.add(collection.data[i]);
		}
		
		
		return true;
	}
	
	@Override //implementation
	public NewApptBook clone() {
		/**
		 * Took the clone method from Homework 2 that was given to use
		 * and it works pretty fine.
		 */
		
		assert wellFormed();

		NewApptBook answer;
	
		try
		{
			answer = (NewApptBook) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}
	
		// all that is needed is to clone the data array.
		// (Exercise: Why is this needed?)
		answer.data = data.clone( );
		
		assert wellFormed();
		return answer;
	}
	
	@Override //required
	public Iterator<Appointment> iterator() {
		// TODO Auto-generated method stub
		MyIterator it = new MyIterator();
		return it;
	}
	
	public Iterator<Appointment> iterator(Appointment o) {
		/**
		 * Reconstructed the constructor for iterator for the collection
		 * and by the comments made in the PDF sends an index instead of the
		 * object. Also added additional methods such as addAll.
		 * 
		 * The current method uses three different cases to do what it needs
		 * to do.
		 */
		if (o == null) {
			throw new NullPointerException();
		}
		// TODO Auto-generated method stub
		
		int tempIndex = 0;
		for (int i = 0; i < manyItems; ++i) {
			if (o.compareTo(data[i]) > 0) {
				tempIndex++;
			}
			else {
				break;
			}

		}
		
		
		
		MyIterator it = new MyIterator(tempIndex);
		return it;
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
			//Prints out stuff
			if (doReport) {
				System.out.println("Invariant error: " + error);
			}
			return false;
		}

		@Override //required
		public boolean hasNext() {
			//Returns true if next is less than many items, and false otherwise
			assert wellFormed();
			
			if (version != myVersion) {
				throw new ConcurrentModificationException();
			}

			return next < manyItems;
		}

		@Override //required
		public Appointment next() {
			//Checks to see if there exists an element beyond
			assert wellFormed();
			
			
			if (!hasNext()) {
				//and if it does not, equal next to manyItems and throws exception
				throw new NoSuchElementException();
			}
			else if (version != myVersion) {
				throw new ConcurrentModificationException();
			}
			else {
				//and if it does, current = next and next++.
				current = next;
				next++;
			}
			
			assert wellFormed();
			
			return data[this.current];
		}

		@Override //required
		public Iterator<Appointment> iterator() {
			// TODO Auto-generated method stub
			this.current = -1;
			return this;
		}
		
		public MyIterator() {
			//initial constructor, where there does not exist a current element yet until one is added
			next = 0;
			myVersion = version;
			this.current = next;
		}
		
		public MyIterator(int Index) {
			//Implements constructor where if given an Appointment object will look through the array
			//to find an object equal to the given, in this case o, and set current to that index.
			//Might have to change to an enhanced for-loop at a later date.
			next = Index;

			this.current = next;
			myVersion = version;

		}
		
		/**
		 * The remove method current runs that if next is == 0, meaning that next()
		 * not been called yet since next is never incremented, or current is less
		 * than 0 if the remove method has already ran once and it sets current = -1
		 * to represent there is no current element.
		 */

		public void remove() {
			/**
			 * Since we are removing the current element that means there will not exist
			 * an element within the iterator if the collection has not changed. So the condition
			 * is that after the remove method is called once, next = 0 and current = 0 like how
			 * the fields were initialized in the constructors.
			 */
			
			assert wellFormed();
			
			if (version != myVersion) {
				throw new ConcurrentModificationException();
			}
			
			if (next == current) {
				throw new IllegalStateException();
			}
			else {
				data[current] = null;
				Appointment[] temp = new Appointment[manyItems-1];
				int tempIndex = 0;
				for (int i = 0; i < manyItems; ++i) {
					if (data[i] != null) {
						temp[tempIndex] = data[i];
						tempIndex++;
					}
				}
				next--;
				data = temp;
				myVersion++;
				version = myVersion;
				manyItems--;
			}
			
			assert wellFormed();
				
		}
		
		
		
	}




}
