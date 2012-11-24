package keyValueBaseInterfaces;

import java.io.IOException;
import java.util.List;
import keyValueBaseExceptions.*;

/*
 * memory-management necessary --> free-space list to reuse deleted portions of the Store.
 */

public interface Index<K extends Key<K>, V extends Value>
{
	public void insert (K k, V v) throws KeyAlreadyPresentException, IOException;
	public void remove (K k) throws KeyNotFoundException;
	public V get (K k) throws KeyNotFoundException, IOException;
	public void update (K k, V v) throws KeyNotFoundException, IOException;
	public List<V> scan (K begin, K end) throws BeginGreaterThanEndException, IOException;
	public List<V> atomicScan (K begin, K end) throws BeginGreaterThanEndException, IOException;
	public void bulkPut(List<Pair<K,V>> keys) throws IOException;
}