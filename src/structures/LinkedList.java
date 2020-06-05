package structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements List<E> {
	
	private Node header;
	private int currentSize;
	
	public LinkedList() {
		header = new Node();
		currentSize = 0;
	}
	
	private class Node {
		private E value;
		private Node next;
		
		public E getValue() {
			return value;
		}
		
		public void setValue(E value) {
			this.value = value;
		}
		
		public Node getNext() {
			return next;
		}
		
		public void setNext(Node next) {
			this.next = next;
		}
		
		public Node(E value, Node next) {
			this.value = value;
			this.next = next;
		}
		
		public Node(E value) {
			this(value,null);
		}
		
		public Node() {
			this(null, null);
		}
		
		public void clear() {
			value = null;
			next = null;
		}
	}

	private class ListIterator implements Iterator<E> {
		private Node nextNode;
		
		public ListIterator() {
			nextNode = header.getNext();
		}
		
		@Override
		public boolean hasNext() {
			return nextNode != null;
		}
		
		@Override
		public E next() {
			if(hasNext()) {
				E val = nextNode.getValue();
				nextNode = nextNode.getNext();
				return val;
			} else {
				throw new NoSuchElementException();
			}
		}
	}
	
	
	@Override
	public Iterator<E> iterator() {
		return new ListIterator();
	}

	@Override
	public void add(E obj) {
		Node newNode = new Node(obj);
		Node curNode;
		
		// Transverse the list until we reach the last node
		for (curNode = this.header; curNode.getNext() != null; curNode = curNode.getNext());
		
		curNode.setNext(newNode);
		this.currentSize++;
		
	}

	@Override
	public void add(int index, E obj) {
		Node curNode, newNode;
		// Verify it's a valid position
		if(index < 0 || index > this.size()) {
			throw new IndexOutOfBoundsException();
		}
		if(index == this.size()) {
			this.add(obj);
		} else {
			// Need to find node at Position index -1
			curNode = get_Node(index-1);
			newNode = new Node(obj, curNode.getNext());
			curNode.setNext(newNode);
			this.currentSize++;
		}
		
	}

	@Override
	public boolean removeObj(E obj) {
		Node curNode = this.header;
		Node nextNode = curNode.getNext();
		// Need to find predecessor node
		while(nextNode != null && !nextNode.getValue().equals(obj)) {
			curNode = nextNode;
			nextNode = nextNode.getNext();
		}
		
		// Check whether we exited the loop because we found it
		if(nextNode != null) {  // Found it
			curNode.setNext(nextNode.getNext()); // Link to the next's next. :)
			clear_node(nextNode); // free up resources
			this.currentSize--;
			return true;
		} else {  // Didn't find it
			return false;
		}
	}
	
	private void clear_node(Node theNode) {
		theNode.setNext(null);
		theNode.setValue(null);
	}

	private Node get_Node(int index) {
		int curPos;
		Node curNode;
		// Get node at position index
		if(index < 0 || index >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		curPos = -1;
		curNode = this.header;
		for(curPos = -1; curPos < index; curPos++) {
			curNode = curNode.getNext();
		}
		
		return curNode;
	}

	@Override
	public boolean remove(int index) {
		Node curNode, rmNode;
		//First confirm index is a valid position
		if(index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		curNode = get_Node(index - 1);
		rmNode = curNode.getNext();
		//If we have A -> B -> C and want to remove B, make A point to C
		curNode.setNext(rmNode.getNext());
		rmNode.clear();
		currentSize--;
		return true;
	}

	@Override
	public int removeAll(E obj) {
		int counter = 0;
		Node curNode = header;
		Node nextNode = curNode.getNext();
		
		/*
		 * We used the following in ArrayList and it would also work here,
		 * but it would have a running time of O(n^2)
		 */
		while(nextNode != null) {
			if(nextNode.getValue().equals(obj)) {
				curNode.setNext(nextNode.getNext());
				nextNode.clear();
				counter++;
				/* Node that was pointed to by nextNode no longer exists
				 * so we reset it such that it still the node after curNode
				 */
				nextNode = curNode.getNext();
			} else {
				curNode = nextNode;
				nextNode = nextNode.getNext();
			}
		}
		return counter;
	}

	@Override
	public E get(int index) {
		// get_node allows for the index to be -1, but we don't want to get to allow that
		if(index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		return get_Node(index).getValue();
	}

	@Override
	public E set(int index, E obj) {
		// get_node allows for the index to be -1, but we don't want to set to allow that
		if(index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		Node theNode = get_Node(index);
		E theValue = theNode.getValue();
		theNode.setValue(obj);
		return theValue;
	}

	@Override
	public E first() {
		return get(0);
	}

	@Override
	public E last() {
		return get(size() - 1);
	}

	@Override
	public int firstIndex(E obj) {
		Node curNode = header.getNext();
		int curPos = 0;
		// Transverse the list until we find the element or we reach the end
		while(curNode != null && !curNode.getValue().equals(obj)) {
			curPos++;
			curNode = curNode.getNext();
		}
		if(curNode != null) {
			return curPos;
		} else {
			return -1;
		}
	}

	@Override
	public int lastIndex(E obj) {
		int curPos = 0;
		int lastPos = -1;
		for(Node curNode = header.getNext(); curNode != null; curNode = curNode.getNext()) {
			if(curNode.getValue().equals(obj)){
				lastPos = curPos;
			}
			curPos ++;
		}
		
		return lastPos;
	}

	@Override
	public int size() {
		return this.currentSize;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public boolean contains(E obj) {
		return firstIndex(obj) != -1;
	}

	@Override
	public void clear() {
		// Avoid throwing an exception if the list is already empty
		while(size() > 0) {
			remove(0);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(E e, E f) {
		int count = 0;
		Iterator check = this.iterator();
		for (int i = 0; check.hasNext(); i++) {
			E temp = (E) check.next();
			if(temp.equals(e)) {
				this.set(i, f);
				count++;
			}
		}
		return count;
	}

	@Override
	public List<E> reverse() {
		LinkedList<E> result = new LinkedList<E>();
		int count = this.currentSize - 1;
		for (int i = count; i >= 0; i--) {
			result.add(this.get(i));
		}
		return result;
	}

}
