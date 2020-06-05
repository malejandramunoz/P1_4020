package structures;

import java.util.Iterator;

public class DynamicSet<E> implements Set<E> {

	private StaticSet<E> theSet;
	private int maxCapacity;

	public DynamicSet(int initialCapacity) {
		this.theSet = new StaticSet<E>(initialCapacity);
		this.maxCapacity = initialCapacity;

	}


	@Override
	public Iterator<E> iterator() {
		return theSet.iterator();
	}

	@Override
	public boolean add(E obj) {
		if(this.maxCapacity == this.theSet.size()) {
			StaticSet<E> newSet = new StaticSet<E>(2*maxCapacity);
			this.maxCapacity *= 2;
			copySet(theSet, newSet);
			theSet = newSet;
		}
		return this.theSet.add(obj);
	}

	private void copySet(Set<E> src, Set<E> dst) {
		for (E e: src) {
			dst.add(e);
		}
	}


	@Override
	public boolean isMember(E obj) {
		return this.theSet.isMember(obj);
	}

	@Override
	public boolean remove(E obj) {
		return this.theSet.remove(obj);
	}

	@Override
	public boolean isEmpty() {
		return this.theSet.isEmpty();
	}

	@Override
	public int size() {
		return this.theSet.size();
	}

	@Override
	public void clear() {
		this.theSet.clear();

	}

	@Override
	public Set<E> union(Set<E> S2) {
		Set<E> temp = this.theSet.union(S2);
		DynamicSet<E> result = new DynamicSet<E>(temp.size());
		copySet(temp, result);
		return result; 
	}

	@Override
	public Set<E> difference(Set<E> S2) {
		Set<E> temp = this.theSet.difference(S2);
		DynamicSet<E> result = new DynamicSet<E>(temp.size());
		copySet(temp, result);
		return result; 
	}

	@Override
	public Set<E> intersection(Set<E> S2) {
		Set<E> temp = this.theSet.intersection(S2);
		DynamicSet<E> result = new DynamicSet<E>(temp.size());
		copySet(temp, result);
		return result; 
	}

	@Override
	public boolean isSubSet(Set<E> S2) {
		return this.theSet.isSubSet(S2);
	}


	@Override
	public boolean equals(Set<E> S2) {
		return this.theSet.equals(S2);
	}
	
	@SuppressWarnings({ "unchecked" })
	public static boolean checkDisjoint(Object[] sets) {
		for (int i = 1; i < sets.length; i++) {
			for (int j = i + 1; j < sets.length; j++) {
				Set temp1 = (Set) sets[i];
				Set temp2 = (Set) sets[j];
				if(temp1.intersection(temp2).size() == 0) {
					return true;
				}
				
			}
		}
		return false;
	}


	@Override
	public Set<Set<E>> singletonSets() {
		return theSet.singletonSets();
	}

}
