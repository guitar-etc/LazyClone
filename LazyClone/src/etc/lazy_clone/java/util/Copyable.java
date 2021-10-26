package etc.lazy_clone.java.util;

/**
 * What if you need more configurable copy logic....
 * @author pycs9
 *
 * @param <T>
 */
public interface Copyable<T extends Copyable<T>> {
//	/**
//	 * Full deep copy
//	 * @return
//	 */
//	public T deepCopy();
//	
	/**
	 * Copies only the immediate object.
	 * @return
	 */
	public T shallowCopy();
}
