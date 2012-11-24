package keyValueBaseInterfaces;

import java.io.Serializable;
import java.util.List;

public interface ValueList<T> extends Value, Iterable<T>, Serializable{
	public void add(T v);
	public void remove(T v);
	public void merge(ValueList<T> v);
	public List<T> toList();
}
