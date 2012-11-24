package keyValueBaseInterfaces;

public interface Store
{
	public byte[] read (Long position, int length);
	public void write (Long position, byte[] value);
}
