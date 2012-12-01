package assignmentImplementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sun.source.tree.SynchronizedTree;

import keyValueBaseExceptions.BeginGreaterThanEndException;
import keyValueBaseExceptions.KeyAlreadyPresentException;
import keyValueBaseExceptions.KeyNotFoundException;
import keyValueBaseInterfaces.Index;
import keyValueBaseInterfaces.Pair;
import keyValueBaseInterfaces.Store;

/**
 * IndexImpl implements a basic memory map over a StoreImpl object. It uses a
 * Reentrant locking scheme to enforce atomicity No reads are allowed during any
 * write operations No writes are allowed during reads
 * 
 * @author jens
 * 
 */
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

	// Index data structures:
	private Map<KeyImpl, Space> allocatedSpaces;
	private Map<Long,Long> freeSpaces;

	

	// Utilities:
	private ValueSerializerImpl serializer = new ValueSerializerImpl();

	// Concurrency locks - set 'fair' so that requests served in order
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

	/**
	 * Constructor - creates storage and initialises data structures
	 * 
	 * @param storePath
	 * @param size
	 */
	public IndexImpl(String storePath, long size) {
		w.lock();
		try {
			storage = new StoreImpl(storePath, size);
			allocatedSpaces = new TreeMap<KeyImpl, Space>();
			freeSpaces = new TreeMap<Long, Long>();
			freeSpaces.put(0L, size);
		} catch (Exception ex) {
			// TODO think about what needs handling!
			ex.printStackTrace();
		} finally {
			w.unlock();
		}

	}

	/**
	 * Insert a key and value in the data store and update memory table
	 */
	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException {

		w.lock();
		try {
			if (allocatedSpaces.containsKey(k))
				throw new KeyAlreadyPresentException(k);
			else {

				// Record prior memory table information
				long countFreespaces = getFreeSpace();
				int countAllocations = allocatedSpaces.size();

				// serialise valuelist to byte array
				byte[] data = serializer.toByteArray(v);

				// allocate space - this throws an IO exception if there is not
				// enough free space
				long offset = allocateSpace(k, data.length);

				// perform the write
				storage.write(offset, data);

				//POSTCONDITION: Check that the tables were correctly updated
				assert (countFreespaces == getFreeSpace());
				assert (countAllocations == allocatedSpaces.size() + 1);
			}
		} finally {
			w.unlock();
		}

	}

	@Override
	public void remove(KeyImpl k) throws KeyNotFoundException {
		w.lock();
		try {
			// find the allocation
			if (allocatedSpaces.containsKey(k)) {
				int numAllocations = allocatedSpaces.size();
				
				
				Space toDelete = allocatedSpaces.remove(k);
				
				System.out.println("adding free space at pos: " + toDelete.getOffset() + ". Num free: " + freeSpaces.size());
				
				freeSpaces.put(toDelete.getOffset(),toDelete.getSize());
				defragmentFreeSpace(toDelete.getOffset());
				
				//POSTCONDITION: 1 less allocation than before
				assert(numAllocations == allocatedSpaces.size()+1);
				
			} else
				throw new KeyNotFoundException(k);
		} finally {
			w.unlock();
		}
	}

	@Override
	/**
	 * Returns the value list for the specified key
	 * @param k key to lookup
	 */
	public ValueListImpl get(KeyImpl k) throws KeyNotFoundException,
			IOException {
		r.lock();
		try {
			if (allocatedSpaces.containsKey(k)) {
				Space locationData = allocatedSpaces.get(k);

				// this could throw IOException!
				// nb. Spaces which are allocated have max size MAXINT
				// Spaces which are free could have 64bit size
				byte[] data = storage.read(locationData.getOffset(),
						(int) locationData.getSize());

				ValueListImpl vl = serializer.fromByteArray(data);

				return vl;
			}

			else {
				throw new KeyNotFoundException(k);
			}
		} finally {
			r.unlock();
		}
	}

	@Override
	public void update(KeyImpl k, ValueListImpl v) throws KeyNotFoundException,
			IOException {
		w.lock();
		int numAllocations = allocatedSpaces.size();
		try {
			if (allocatedSpaces.containsKey(k)) {

				// serialise the new value:
				byte[] newValue = serializer.toByteArray(v);

				// get the current location data:
				Space locationData = allocatedSpaces.get(k);

				// find the size of the new and the old value
				int oldSize = (int) locationData.getSize();
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

						allocatedSpaces.put(k, newSpace);

						// Store data
						storage.write(newSpace.getOffset(),
								serializer.toByteArray(v));

						// Create a new free space immediately after the new
						// allocation
						freeSpaces.put(newSpace.getOffset()+newSize,(long) oldSize-newSize);
						defragmentFreeSpace(newSpace.getOffset()+newSize);
					} catch (Exception ex) {
						// TODO what could go wrong?
						ex.printStackTrace();
					}

				} else {
					// If the new data is larger, a new free space is needed

					// Find a big enough space:
					long newOffset = findSpace(newSize); //freeSpaces.higher(new Space(newSize));

					if (newOffset >= 0) {

						long availableSize = freeSpaces.remove(newOffset);

						Space newAllocationData = new Space(newSize,
								newOffset);

						// put the data address and write to store
						allocatedSpaces.put(k, newAllocationData);
						storage.write(newOffset, newValue);

						// Replace free space that we are not using
						freeSpaces.put(newOffset+newSize, availableSize - newSize);
						defragmentFreeSpace(newOffset+newSize);
						
						// Set the previous storage space as a free space
						freeSpaces.put(locationData.getOffset(), (long) oldSize);
						defragmentFreeSpace(locationData.getOffset());
					} else {
						throw new IOException(
								"Could not find large enough space in memory");
					}

				}

			} else {
				throw new KeyNotFoundException(k);
			}
			//POSTCONDITION: same number of allocations as before
			assert(numAllocations == allocatedSpaces.size());
			
		} finally {
			w.unlock();
		}

	}
	
	
	/**
	 * Find a space in memory to insert specified size
	 * @param size
	 * @return memory offset
	 */
	private long findSpace(long size) {
		for (long offset : freeSpaces.keySet())
		{
			if (freeSpaces.get(offset) >= size)
				return offset;
		}
		// invalid space if none found
		return -1L;
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end)
			throws BeginGreaterThanEndException, IOException {
		// TODO Auto-generated method stub
		r.lock();
		List<ValueListImpl> values = new ArrayList<ValueListImpl>();
		try {

			for (KeyImpl k : allocatedSpaces.keySet()) {
				if (k.compareTo(begin) >= 0 && k.compareTo(end) <= 0) {
					values.add(get(k));
				}
			}
		} catch (KeyNotFoundException e) {
			// should never occur - but could be thrown by get method
		} finally {
			// locking implemented by get method?
			r.unlock();
		}
		return values;

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

	/******************************************************************
	 * Internal methods
	 */

	// Lists all freespaces
	public String freeSpacesString() {
		r.lock();
		String free = "";
		for (long f : freeSpaces.keySet()) {
			free = free + f + "[" + freeSpaces.get(f) + "]\n";
		}
		r.unlock();
		return free;
	}

	private void defragmentFreeSpace(long offset) {
				
				long currentSize = freeSpaces.get(offset);
				
				// find any subsequent neighbour:
				if (freeSpaces.containsKey(offset+currentSize))
				{
					// if there is a neighbour, merge the spaces into one
					long extraSpace = freeSpaces.get(offset+currentSize);
					freeSpaces.put(offset, currentSize+extraSpace);
					freeSpaces.remove(offset+currentSize);
					
					
					
				}
				// find a previous neighbour is trickier! need to look behind some unspecified amount
				//TODO - implement backwards search as well!
//				else if (freeSpaces.containsKey(offset-currentSize)) {
//					// if there is a previous neighbour, merge the spaces into one
//					long extraSpace = freeSpaces.get(offset-currentSize);
//					freeSpaces.put(offset-currentSize, currentSize+extraSpace);
//					freeSpaces.remove(offset);
//				}
				
		
	}
	
	/**
	 * 
	 * Adding a free space can modify other parts of memory - hence synchronized
	 * block to protect the data structure
	 * 
	 * @param size
	 * @param offset
	 */
//	private void addFreeSpace(long size, long offset) {
//		synchronized (freeSpaces) {
//			if (size > 0) {
//				Space newFree = new Space(size, offset);
//
//				// check for neighbours in freeSpaces - if any then merge
//				// sort the merged spaces by position:
//				long newSize = 0;
//				long newOffset = offset;
//				int neighbours = 0;
//				for (Space free : freeSpaces) {
//					if (isNeighbour(free, newFree))
//						System.out.println("New free space has a neighbour!");
//					neighbours++;
//					if (free.getOffset() < newFree.getOffset()) {
//						newOffset = free.getOffset();
//					}
//					freeSpaces.remove(free);
//					newSize += free.getSize();
//
//					if (neighbours == 2)
//						break;
//				}
//				if (newSize > 0) {
//					// create the new freespace at the initial offset
//					newFree.setOffset(newOffset);
//					newFree.setSize(newSize);
//					System.out.println("merging neighbours to add " + newSize
//							+ " bytes free space at " + newOffset);
//				}
//
//				freeSpaces.add(newFree);
//			}
//		}
//
//	}
	

	/**
	 * Find a space to fit the given key - store the location and return the
	 * offset
	 * 
	 * This method deals with finding free space and rearranging allocations
	 * 
	 * @param k
	 *            the key to map
	 * @param size
	 *            the size of the memory to allocate
	 * @return the memory offset to store the data
	 * @throws Exception
	 */
	private long allocateSpace(KeyImpl k, int size) throws IOException {

		w.lock();
		try {
			// Create space that fits this data:
			Space toAllocate = new Space(size);

			// find the free space that is smallest possible space to fit
			// this new data:
			long selectedOffset = findSpace(size);

			if (selectedOffset >= 0) {
				// set the location for the data
				toAllocate.setOffset(selectedOffset);

				// remove the old free space
				long freeSize = freeSpaces.remove(selectedOffset);

				// add free space after allocated memory
				freeSpaces.put(selectedOffset+size,freeSize-size);
				defragmentFreeSpace(selectedOffset+size);

				// update allocation table
				allocatedSpaces.put(k, toAllocate);

				return toAllocate.getOffset();
			} else {
				throw new IOException(
						"Not enough free space to hold data for key " + k);
			}
		} finally {
			w.unlock();
		}

	}

	/**
	 * getFreeSpace calculates the total current unassigned memory
	 * 
	 * @return the current total free space
	 */
	public long getFreeSpace() {
		r.lock();
		try {
			long total = 0;
			for (long s : freeSpaces.values()) {
				total += s;
			}

			return total;
		} finally {
			r.unlock();
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

		protected Long size; // could occupy entire memory
		protected Long offset;
		protected boolean positioned = false;

		/**
		 * Construct a space with specified size and position
		 * 
		 * @param size
		 *            Size
		 * @param offset
		 *            Offset
		 */
		public Space(int size, long offset) {
			this.size = (long) size;
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
			this.size = (long) size;
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

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public long getOffset() {
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

}