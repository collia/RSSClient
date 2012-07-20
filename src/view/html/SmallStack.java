package view.html;

import java.util.*;

/**
 *  Class implements stack with fixed size. If size bigger than 
 * length old elements remove.
 * 
 * @author Nicolay Klimchuk
 *
 * @param <E> Type of stack
 */
public class SmallStack<E> implements Deque<E> {

	private int length; 
	private Deque<E> data = new ArrayDeque<E>();
	
	/**
	 * Constructor. 
	 * @param length - maximum size of stack
	 */
	public SmallStack(int length){
		this.length = length;
	}

	@Override
	public boolean add(E arg0) {
		if(data.size() >= length)
			//data.removeFirst();
			data.removeLast();
		data.addLast(arg0);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		Iterator<? extends E> it = data.iterator();
		while(it.hasNext())
			add(it.next());
		return true;
	}

	@Override
	public void addFirst(E arg0) {
		data.addFirst(arg0);
	}

	@Override
	public void addLast(E arg0) {
		data.addLast(arg0);
	}

	@Override
	public boolean contains(Object arg0) {
		return data.contains(arg0);
	}

	@Override
	public Iterator<E> descendingIterator() {
		return data.descendingIterator();
	}

	@Override
	public E element() {
		return data.element();
	}

	@Override
	public E getFirst() {
		return data.getFirst();
	}

	@Override
	public E getLast() {
		return data.getLast();
	}

	@Override
	public Iterator<E> iterator() {
		return data.iterator();
	}

	@Override
	public boolean offer(E arg0) {
		return data.offer(arg0);
	}

	@Override
	public boolean offerFirst(E arg0) {
		return data.offerFirst(arg0);
	}

	@Override
	public boolean offerLast(E arg0) {
		return data.offerLast(arg0);
	}

	@Override
	public E peek() {
		return data.peek();
	}

	@Override
	public E peekFirst() {
		return data.peekFirst();
	}

	@Override
	public E peekLast() {
		return data.peekLast();
	}

	@Override
	public E poll() {
		return data.poll();
	}

	@Override
	public E pollFirst() {
		return data.pollFirst();
	}

	@Override
	public E pollLast() {
		return data.pollLast();
	}

	@Override
	public E pop() {
		return data.pop();
	}

	@Override
	public void push(E arg0) {
		if(data.size() >= length)
			//data.removeFirst();
			data.removeLast();
		data.push(arg0);
	}

	@Override
	public E remove() {
		return data.remove();
	}

	@Override
	public boolean remove(Object arg0) {
		return data.remove(arg0);
	}

	@Override
	public E removeFirst() {
		return data.removeFirst();
	}

	@Override
	public boolean removeFirstOccurrence(Object arg0) {
		return data.removeFirstOccurrence(arg0);
	}

	@Override
	public E removeLast() {
		return data.removeLast();
	}

	@Override
	public boolean removeLastOccurrence(Object arg0) {
		return data.removeLastOccurrence(arg0);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return data.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return data.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return data.retainAll(arg0);
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return data.toArray(arg0);
	}


	
}
