package assignmentImplementation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseInterfaces.Index;
import keyValueBaseInterfaces.Key;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Store;

public class IndexImpl implements Index<KeyImpl,ValueListImpl>
{
	/**
	 * Private data structures
	 * 
	 * storage is the Store containing raw data addressed by position and offset
	 * locationTable is the mapping of keys to addresses
	 */
	private Store storage;
	private Map<Key,MemoryMappingData> locationTable;
	private long nextOffset;
	
	// class to store offset and length
	private class MemoryMappingData {
		private long offset;
		
		public long getOffset() {
			return offset;
		}
		public void setOffset(long offset) {
			this.offset = offset;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		private int length;
	}

	public IndexImpl(String storePath, long totalSize) {
		try {
			storage = new StoreImpl(storePath, 2048);
			
			//TODO consider other data structures for index
			locationTable = new HashMap<Key, MemoryMappingData>();
		}
		catch (Exception ex) {
			//TODO think about what needs handling!
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException {
		
		if (locationTable.containsKey(k))
			throw new KeyAlreadyPresentException(k);
		else
		{
			// convert valuelist to byte array
			byte[] data = new byte[]{};
			int i = 0;
			for (ValueImpl value : v) {
				data[i] = value.toByte();
				i++;
			}
			MemoryMappingData mmd = new MemoryMappingData();
			mmd.setOffset(nextOffset);
			mmd.setLength(v.toList().size());
			locationTable.put(k, mmd);
			// next perform the put into the store
			storage.write(mmd.offset, data);
		}
		
	}

	@Override
	public void remove(KeyImpl k) throws KeyNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ValueListImpl get(KeyImpl k) throws KeyNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		MemoryMappingData mmd = locationTable.get(k);
		Object data = storage.read(mmd.getOffset(), mmd.getLength());
		return null;
	}

	@Override
	public void update(KeyImpl k, ValueListImpl v) throws KeyNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end)
			throws BeginGreaterThanEndException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end)
			throws BeginGreaterThanEndException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> keys)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}