package assignmentImplementation;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
	 * 
	 */
	private Store storage;
	private Map<KeyImpl,MemoryMappingData> locationTable;
	private TreeSet<Space> freeSpaces;
	private ValueSerializerImpl serializer = new ValueSerializerImpl();
	
	public long getFreeSpace() {
		long total = 0;
		for (Space s : freeSpaces) {
			total = total + s.size;
		}
		return total;
	}
	private class Space implements Comparable<Space> {
		public Integer size;
		public Long offset;
		
		/**
		 * Construct a space with specified size and position
		 * @param s Size 
		 * @param o Offset
		 */
		public Space(int s, long o) {
			this.size = s;
			this.offset = o;
		}
		
		/**
		 * Creates a non-positioned space
		 * @param size
		 */
		public Space(int size) {
			this.size = size;
			this.offset = -1L;
		}
		@Override
		public int compareTo(Space s) {
			
			return size.compareTo(s.size);
		}

	}
	
	// class to store offset and length
	private class MemoryMappingData {
		private long offset;
		
		public long getOffset() {
			updateAccessTime();
			return offset;
		}
		public void setOffset(long offset) {
			updateAccessTime();
			this.offset = offset;
		}
		public int getLength() {
			updateAccessTime();
			return length;
		}
		public void setLength(int length) {
			this.length = length;
			updateAccessTime();
		}
		private int length;
		
		private void updateAccessTime() {
			lastAccess = new Date();
		}
		public Date getLassAccess() {
			return lastAccess;
		}
		private Date lastAccess;
	}


	
	public IndexImpl(String storePath, int i) {
		try {
			storage = new StoreImpl(storePath, i);
			
			// Current free space is total address space
			Space initialFree = new Space(i, 0L);
			freeSpaces = new TreeSet<IndexImpl.Space>();
			freeSpaces.add(initialFree);
			
			
			
			//TODO consider other data structures for index
			locationTable = new HashMap<KeyImpl, MemoryMappingData>();
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
			int countFreespaces = freeSpaces.size();
			// serialise valuelist to byte array
			
			byte[] data = serializer.toByteArray(v);
			
			// find free space that is smallest possible
			Space toAllocate = new Space( data.length);
			Space selectedFreeSpace = freeSpaces.higher(toAllocate);
			// create new replacement freespace (non positioned)
			Space newFreeSpace = new Space(selectedFreeSpace.size -  data.length);
			
			// put the data
			MemoryMappingData mmd = new MemoryMappingData();
			mmd.setOffset(selectedFreeSpace.offset);
			mmd.setLength(data.length);
			locationTable.put(k, mmd);
			storage.write(mmd.getOffset(), data);
			
			
			// update the freespaces structure
			newFreeSpace.offset = selectedFreeSpace.offset+data.length;
			freeSpaces.add(newFreeSpace);
			freeSpaces.remove(selectedFreeSpace);
			
			
			//TODO: quick check
			assert( countFreespaces == freeSpaces.size());
			//TODO: handle case where not big enough freespace
		}
		
	}

	@Override
	public void remove(KeyImpl k) throws KeyNotFoundException {
		if (locationTable.containsKey(k))
		{
			// create a free space and delete from table
			long size = locationTable.get(k).getLength();
			MemoryMappingData mmd = locationTable.get(k);
			Space newFreeSpace = new Space(mmd.getLength(),mmd.getOffset());
			freeSpaces.add(newFreeSpace);
			locationTable.remove(k);
			
		}
		else
			throw new KeyNotFoundException(k);
		
	}

	@Override
	public ValueListImpl get(KeyImpl k) throws KeyNotFoundException,
			IOException {
		
		try {
			MemoryMappingData mmd = locationTable.get(k);
			byte[] data = storage.read(mmd.getOffset(), mmd.getLength());
			
			ValueListImpl vl = serializer.fromByteArray(data);
			
			return vl;
		} //TODO throw exceptions
		catch (Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
		
		

	}

	@Override
	public void update(KeyImpl k, ValueListImpl v) throws KeyNotFoundException,
			IOException {
		if (locationTable.containsKey(k)) {
			MemoryMappingData mmd = locationTable.get(k);
			
			// find the size of the new value and the old value
			int oldSize = mmd.getLength();
			byte[] newValue = serializer.toByteArray(v);
			int newSize = newValue.length;
			
			if (oldSize == newSize) {
				// put data
				storage.write(mmd.getOffset(), serializer.toByteArray(v));
			}
			else if (oldSize > newSize) {
				// update the memory table
				MemoryMappingData newmmd = new MemoryMappingData();
				newmmd.setLength(newSize);
				newmmd.setOffset(mmd.getOffset());
				locationTable.put(k, newmmd);
				
				// put data
				storage.write(newmmd.getOffset(), serializer.toByteArray(v));
				
				// new freespace
				Space newFreeSpace = new Space(oldSize - newSize, newmmd.getOffset() + newSize);
				freeSpaces.add(newFreeSpace);
			}
			else {
				// newsize is greater - find a new freespace
				Space needed = new Space(newSize);
				Space newSpace = freeSpaces.higher(needed);
				MemoryMappingData newmmd = new MemoryMappingData();
				newmmd.setLength(newSize);
				newmmd.setOffset(newSpace.offset);
				// put the data
				locationTable.put(k, newmmd);
				storage.write(newSpace.offset, newValue);
				
				// free up the old space
				Space newFreeSpace = new Space(oldSize, mmd.getOffset());
				freeSpaces.add(newFreeSpace);
			}
			
			
			// else if equal, then replace
			// else if less, create new freespace after
		}
		else {
			throw new KeyNotFoundException(k);
		}
		
		
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