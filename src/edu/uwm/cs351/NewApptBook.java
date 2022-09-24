package edu.uwm.cs351;

import java.util.AbstractCollection;

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

	private class MyIterator // TODO: what should this implement?	
	{
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
	}
}
