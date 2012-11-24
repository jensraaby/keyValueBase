package keyValueBaseInterfaces;

public class Pair<K, V> {
	private K k;
	private V v;
	
	@SuppressWarnings("unused")
	private Pair()
	{
	}
	
	public Pair (K k, V v){
		this.k = k;
		this.v = v;
	}
	
	public K getKey(){
		return k;
	}
	
	public V getValue(){
		return v;
	}
}
