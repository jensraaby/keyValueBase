package assignmentImplementation;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseInterfaces.Index;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Store;

public class IndexImpl implements Index<KeyImpl, ValueListImpl> {
	
	/**
	 * Private data structures
	 * 
	 * storage is the Store containing raw data addressed by position and offset
	 * locationTable is the mapping of keys to spaces in memory freeSpaces is
	 * the current set of available free space
	 * 
	 */
	private Store storage;
//	private Map<KeyImpl, Space> locationTable;
//	private TreeSet<Space> freeSpaces;
	private MemoryTable memtable; 
	private ValueSerializerImpl serializer = new ValueSerializerImpl();

	// Memory table needs locks to be able to write to it
	// don't move values, unless they increase size
	private class MemoryTable {
		private TreeSet<Space> freeSpaces = new TreeSet<Space>();
		private Map<KeyImpl, Space> allocatedSpaces = new TreeMap<KeyImpl, Space>();

		public MemoryTable(long size) {
			addFreeSpace(size,0L);
		}
		
		/**
		 * Returns allocation data for given key, if present
		 * @param k key to lookup
		 * @return The memory space occupied by the key
		 * @throws KeyNotFoundException if the key is not present
		 */
		public Space getAllocatedSpace(KeyImpl k) throws KeyNotFoundException {
			if (allocatedSpaces.containsKey(k)) {
				return allocatedSpaces.get(k);
			}
			else
				throw new KeyNotFoundException(k);
		}

		private synchronized void addFreeSpace(int size, long offset) {
			if (size > 0) {
				Space newFree = new Space(size,offset);
				
				//TODO check for neighbours in freeSpaces - if any then merge
				TreeMap<Long,Space> toMerge = new TreeMap<Long,Space>();
				for (Space free : freeSpaces) {
					if (isNeighbour(free, newFree))
						toMerge.put(free.getOffset(), free);
					
					if (toMerge.size() == 2)
						break;
				}
				for (long address : toMerge.keySet()) {
					//TODO implement merge
					
				}
			}
			
				
		}
		
		/**
		 * Find a space to fit the given key - store the location and return the offset
		 * 
		 * This method deals with finding free space and rearranging allocations
		 * @param k the key to map
		 * @param size the size of the memory to allocate
		 * @return the memory offset to store the data
		 * @throws Exception 
		 */
		public synchronized long allocateSpace(KeyImpl k, int size) throws IOException {
			
			// Create space that fits this data:
			Space toAllocate = new Space(size);

			// find the free space that is smallest possible space to fit
			// this new data:
			Space selectedFreeSpace = freeSpaces.higher(toAllocate);

			if (selectedFreeSpace != null) 
			{
				// set the location for the data
				toAllocate.setOffset(selectedFreeSpace.getOffset());
				
				// add free space after allocated memory
				addFreeSpace(selectedFreeSpace.getSize() - size, selectedFreeSpace.getOffset()+size);
							
				// update allocation table
				allocatedSpaces.put(k, toAllocate);
				
				return toAllocate.getOffset();
			}
			else {
				throw new IOException("Not enough free space to hold data for key " + k);
			}
			
		}
		
		public synchronized void deleteAllocatedSpace(KeyImpl k) throws KeyNotFoundException{
			// find the allocation
			if (allocatedSpaces.containsKey(k)) {
				Space toDelete = allocatedSpaces.get(k);
				addFreeSpace(toDelete.getSize(), toDelete.getOffset());
				allocatedSpaces.remove(k);
			}
			else 
				throw new KeyNotFoundException(k);
			
		}
		
		/**
		 * getFreeSpace calculates the total current unassigned memory
		 * 
		 * @return the current total free space
		 */
		public long getFreeSpace() {
			long total = 0;
			for (Space s : freeSpaces) {
				total += s.getSize();
			}
			return total;
		}

		/**
		 * Returns the number of allocations in the memory table
		 * @return
		 */
		public int getAllocationCount() {
			return allocatedSpaces.size();
		}

		/**
		 * Exposes the containsKey method on the allocation table
		 * @param k
		 * @return
		 */
		public boolean containsKey(KeyImpl k) {
			return allocatedSpaces.containsKey(k);
		}
	}

	/**
	 * Space: a class to represent some area of memory It implements comparable
	 * so that it can be ordered in a data structure This ordering is based on
	 * the size (byte length) of the space.
	 * 
	 * It can be extended to add access time and locking properties.
	 * 
	 * @author jens
	 * 
	 */
	private class Space implements Comparable<Space> {

		protected Integer size;
		protected Long offset;
		protected boolean positioned = false;

		/**
		 * Construct a space with specified size and position
		 * 
		 * @param s
		 *            Size
		 * @param o
		 *            Offset
		 */
		public Space(int size, long offset) {
			this.size = size;
			this.offset = offset;
			this.positioned = true;
		}

		/**
		 * Creates a non-positioned space - should not be added to any data
		 * structures
		 * 
		 * @param size
		 */
		public Space(int size) {
			this.size = size;
			this.positioned = false;
		}

		/**
		 * Comparison based on size of space
		 */
		@Override
		public int compareTo(Space s) {
			return size.compareTo(s.size);
		}

		/**
		 * Getters and Setters for size and offset
		 */

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public Long getOffset() {
			// TODO more graceful handling of non-positioning
			if (this.positioned)
				return this.offset;
			else {
				return -1L;
			}
			
		}

		public void setOffset(Long offset) {
			this.positioned = true;
			this.offset = offset;
			
		}

	}

	public IndexImpl(String storePath, long size) {
		try {
			storage = new StoreImpl(storePath, size);
			memtable = new MemoryTable(size);
			
			// needs to be protected and sorted by key ordering

		} catch (Exception ex) {
			// TODO think about what needs handling!
			ex.printStackTrace();
		}

	}

		
	/**
	 * Insert a key and value in the data store and update memory table
	 */	
	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException {

		if (memtable.containsKey(k))
			throw new KeyAlreadyPresentException(k);
		else {
			
			System.out.println("1. Trying to insert key " + k);
			// Record prior memory table information
			long countFreespaces = memtable.getFreeSpace();
			int countAllocations = memtable.getAllocationCount();
			
			// serialise valuelist to byte array
			byte[] data = serializer.toByteArray(v);

			// allocate space - this throws an IO exception if there is not enough free space
			memtable.allocateSpace(k, (long) data.length); 

			// Check that the tables are correctly updated
			assert (countFreespaces == memtable.getFreeSpace());
			assert (countAllocations == memtable.getAllocationCount() + 1);
		}

	}

	@Override
	public void remove(KeyImpl k) throws KeyNotFoundException {
		// handled by memory table
		memtable.deleteAllocatedSpace(k);
	}

	@Override
	/**
	 * Returns the value list for the specified key
	 * @param k key to lookup
	 */
	public ValueListImpl get(KeyImpl k) throws KeyNotFoundException,
			IOException {

		if (memtable.containsKey(k)) {
			
			Space locationData = memtable.getAllocatedSpace(k);

			System.out.println("Getting at address: " + locationData.getOffset());
			
			// this could throw IOException!
			byte[] data = storage.read(locationData.getOffset(), locationData.getSize());

			ValueListImpl vl = serializer.fromByteArray(data);

			return vl;
		}

		else {
			throw new KeyNotFoundException(k);
		}

	}

	@Override
	public void update(KeyImpl k, ValueListImpl v) throws KeyNotFoundException,
			IOException {
		if (memtable.containsKey(k)) {
			
			// serialise the new value:
			byte[] newValue = serializer.toByteArray(v);

			// get the current location data:
			Space locationData = memtable.getAllocatedSpace(k);

			// find the size of the new and the old value
			int oldSize = locationData.getSize();
			int newSize = newValue.length;

			// Handle size differences in the new and old value:
			if (oldSize == newSize) {
				// replace data in store since no change needed
				storage.write(locationData.getOffset(), newValue);
				
			} else if (oldSize > newSize) {
				// If less memory is needed we can create a new free space.

				try {
					Space newSpace = new Space(newSize,
							locationData.getOffset());

					putNewLocation(k, newSpace);

					// Store data
					storage.write(newSpace.getOffset(),
							serializer.toByteArray(v));

					// Create a new free space immediately after the new
					// allocation
					Space newFreeSpace = new Space(oldSize - newSize,
							newSpace.getOffset() + newSize);
					freeSpaces.add(newFreeSpace);
				} catch (Exception ex) {
					// TODO what could go wrong?
					ex.printStackTrace();
				}

			} else {
				// If the new data is larger, a new free space is needed

				// Find a big enough space:
				Space newSpace = freeSpaces.higher(new Space(newSize));

				if (newSpace != null) {

					freeSpaces.remove(newSpace);
					Space newAllocationData = new Space(newSize,
							newSpace.offset);

					// put the data
					locationTable.put(k, newAllocationData);
					storage.write(newSpace.getOffset(), newValue);

					// Reduce size of free space that we are using
					Space replacementFreeSpace = new Space(newSpace.size
							- newSize, newSpace.getOffset() + newSize);
					freeSpaces.add(replacementFreeSpace);

					// Set the previous storage space as a free space
					Space newFreeSpace = new Space(oldSize,
							locationData.getOffset());
					freeSpaces.add(newFreeSpace);
				} else {
					throw new IOException(
							"Could not find large enough space in memory");
				}

			}

			// TODO merging neighbouring freespaces method
			// need to know if 2 freespaces are neighbours
			// TODO thread safety/locks
		} else {
			throw new KeyNotFoundException(k);
		}

	}

	

	/**
	 * Helper method determines if 2 spaces are next to each other. This can be
	 * used to determine if 2 free spaces can be merged
	 * 
	 * @param s1
	 * @param s2
	 * @return true iff the two spaces are next to each other
	 */
	private boolean isNeighbour(Space s1, Space s2) {
		boolean neighbour = false;

		if (s1.getOffset() + s1.getSize() == s2.getOffset())
			neighbour = true;
		else if (s1.getOffset() - s2.getSize() == s2.getOffset())
			neighbour = true;

		return neighbour;
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