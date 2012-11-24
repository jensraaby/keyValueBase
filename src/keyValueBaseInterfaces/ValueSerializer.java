package keyValueBaseInterfaces;

import java.io.IOException;

public interface ValueSerializer<V extends Value> {

	public V fromByteArray(byte[] array) throws IOException;
	public byte[] toByteArray(V v) throws IOException;
}
