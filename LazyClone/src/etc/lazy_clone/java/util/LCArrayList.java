package etc.lazy_clone.java.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class LCArrayList<E> extends ArrayList<E> implements Copyable<LCArrayList<E>>, AutoCloseable {
	private static final long serialVersionUID = 1L;
	private ArrayList<E> impl;
	
	/**
	 * Probably want to use Closer
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
//		copyCounter[0]--; // If not decremented, cloning could occur even if the cloned object has been gc'ed.
		close();
	}
	
	@Override
	public void close() {
		copyCounter[0]--;
	}
	
//	private List<LCArrayList<E>> myCopies = new ArrayList<>();
	
	// need to clone "al" because changes made in al isn't notified to LCArrayList.
	@SuppressWarnings("unchecked")
	public static <E> ArrayList<E> copy(ArrayList<E> al) {
		if (al instanceof LCArrayList<E> lcal) {
			return lcal.shallowCopy();
		}
		return new LCArrayList<>((ArrayList<E>)al.clone(), new int[1]);
	}
	
	public LCArrayList() {
		this(null);
	}
	
	public LCArrayList(ArrayList<E> impl) {
		this(impl, null);
	}

	private LCArrayList(ArrayList<E> impl, int[] copyCounter) {
		this.impl = impl != null ? impl : new ArrayList<>();
		this.copyCounter = copyCounter != null ? copyCounter : new int[1];
	}
	
	@Override
	public String toString() {
		return "LCArrayList [impl=" + impl + "@" + System.identityHashCode(impl) + ", copyCounter=" + Arrays.toString(copyCounter) + ", implCopied="
				+ implCopied + "]";
	}
	
//	@Override
//	public LCArrayList<E> deepCopy() {
////		return new LCArrayList<>(this);
//	}

	private int[] copyCounter;
	@Override
	public LCArrayList<E> shallowCopy() {
		copyCounter[0]++;
		return new LCArrayList<>(this.impl, this.copyCounter);
	}
	
	public LCArrayList<E> clone() {
		return shallowCopy();
//		return impl.clone();
	}
	
	private void onRead() {
		
	}
	
	private void onWrite() {
		// separate the child objects from parent object.
//		for (var copy : myCopies) {
//			copy.copyImpl();
//		}

		// If there is no copy of this object, use impl directly.
		if (copyCounter[0] > 0 && !implCopied) {
			copyImpl();
			
			// Since the impl is copied, this object no longer needs to be tracked by copyCounter.
			copyCounter[0]--;
			copyCounter = new int[1];
		}
	}
	private boolean implCopied = false;
	
	@SuppressWarnings("unchecked")
	private void copyImpl() {
		this.impl = (ArrayList<E>)this.impl.clone();
		implCopied = true;
	}
	
	public void trimToSize() {
//		onWrite(); // although this is a write operation, this is not a structural change.
		impl.trimToSize();
	}

	public void ensureCapacity(int minCapacity) {
//		onWrite();
		impl.ensureCapacity(minCapacity);
	}

	public int size() {
		return impl.size();
	}

	public boolean isEmpty() {
		return impl.isEmpty();
	}

	public boolean containsAll(Collection<?> c) {
		return impl.containsAll(c);
	}

	public boolean contains(Object o) {
		return impl.contains(o);
	}

	public int indexOf(Object o) {
		return impl.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return impl.lastIndexOf(o);
	}

	public Object[] toArray() {
		return impl.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return impl.toArray(a);
	}

	public E get(int index) {
		return impl.get(index);
	}

	public E set(int index, E element) {
		onWrite();
		return impl.set(index, element);
	}

	public boolean add(E e) {
		onWrite();
		return impl.add(e);
	}

	public void add(int index, E element) {
		onWrite();
		impl.add(index, element);
	}

	public E remove(int index) {
		onWrite();
		return impl.remove(index);
	}

	public boolean equals(Object o) {
		return impl.equals(o);
	}

	public <T> T[] toArray(IntFunction<T[]> generator) {
		return impl.toArray(generator);
	}

	public int hashCode() {
		return impl.hashCode();
	}

	public boolean remove(Object o) {
		onWrite();
		return impl.remove(o);
	}

	public void clear() {
		onWrite();
		
		// I thought about just setting impl to new ArrayList and not calling "onWrite" to avoid cloning unnecessarily.
		// But impl could be an unknown subclass of ArrayList, so this is the safest.
		impl.clear();
	}

	public boolean addAll(Collection<? extends E> c) {
		onWrite();
		return impl.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		onWrite();
		return impl.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		onWrite();
		return impl.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		onWrite();
		return impl.retainAll(c);
	}

	public ListIterator<E> listIterator(int index) {
		onWrite(); // It's probably best to somehow override the ListIterator's write methods, but I'm not sure how to do it.
		return impl.listIterator(index);
	}

	public ListIterator<E> listIterator() {
		onWrite(); // It's probably best to somehow override the ListIterator's write methods, but I'm not sure how to do it.
		return impl.listIterator();
	}

	public Iterator<E> iterator() {
		return impl.iterator();
	}

	public Stream<E> stream() {
		return impl.stream();
	}

	public Stream<E> parallelStream() {
		return impl.parallelStream();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		onWrite(); // It's probably best to somehow override the SubList's write methods, but I'm not sure how to do it.
		return impl.subList(fromIndex, toIndex);
	}

	public void forEach(Consumer<? super E> action) {
		impl.forEach(action);
	}

	public Spliterator<E> spliterator() {
		return impl.spliterator();
	}

	public boolean removeIf(Predicate<? super E> filter) {
		onWrite();
		return impl.removeIf(filter);
	}

	public void replaceAll(UnaryOperator<E> operator) {
		onWrite();
		impl.replaceAll(operator);
	}

	public void sort(Comparator<? super E> c) {
		onWrite();
		impl.sort(c);
	}
}
