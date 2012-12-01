package assignmentImplementation;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sun.org.apache.bcel.internal.generic.AllocationInstruction;

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
	
	// Index data structures:
	private Map<KeyImpl, Space> allocatedSpaces;
	private TreeSet<Space> freeSpaces;

	// Utilities:
	private ValueSerializerImpl serializer = new ValueSerializerImpl();

	// Concurrency locks - fair so that requests served in order
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
			allocatedSpaces = new TreeMap<KeyImpl,Space>();
			freeSpaces = new TreeSet<Space>();
			freeSpaces.add(new Space(size, 0L));

			// needs to be protected and sorted by key ordering

		} catch (Exception ex) {
			// TODO think about what needs handling!
			ex.printStackTrace();
		} finally {
			w.unlock();
		}

	}

	public String freeSpaces() {
		r.lock();
		String free = "";
		for (Space f : freeSpaces)
		{
			free = free + " \n" + f.getOffset() + "[" +  f.getSize() +"]";
		}
		r.unlock();
		return free;
	}
	/**
	 * 
	 * Adding a free space can modify other parts of memory - hence synchronized
	 * block
	 * 
	 * @param size
	 * @param offset
	 */
	private void addFreeSpace(long size, long offset) {
		synchronized (freeSpaces) {
			if (size > 0) {
				Space newFree = new Space(size, offset);

				// check for neighbours in freeSpaces - if any then merge
				// sort the merged spaces by position:
				long newSize = 0;
				long newOffset = offset;
				int neighbours = 0;
				for (Space free : freeSpaces) {
					if (isNeighbour(free, newFree))
						System.out.println("New free space has a neighbour!");
						neighbours++;
						if (free.getOffset() < newFree.getOffset()) {
							newOffset = free.getOffset();
						}
						freeSpaces.remove(free);
						newSize += free.getSize();

					if (neighbours == 2)
						break;
				}
				if (newSize > 0) {
					// create the new freespace at the initial offset
					newFree.setOffset(newOffset);
					newFree.setSize(newSize);
					System.out.println("merging neighbours to add " + newSize + " bytes free space at " + newOffset);
				}
				
				freeSpaces.add(newFree);
			}
		}

	}

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
			Space selectedFreeSpace = freeSpaces.higher(toAllocate);

			if (selectedFreeSpace != null) {
				// set the location for the data
				toAllocate.setOffset(selectedFreeSpace.getOffset());

				// remove the old free space 
				freeSpaces.remove(selectedFreeSpace);
				
				// add free space after allocated memory
				addFreeSpace(selectedFreeSpace.getSize() - size,
						selectedFreeSpace.getOffset() + size);
				
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
			for (Space s : freeSpaces) {
				total += s.getSize();
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
		 * @param s
		 *            Size
		 * @param o
		 *            Offset
		 */
		public Space(long size, long offset) {
			this.size = size;
			this.offset = offset;
			this.positioned = true;
		}

		// helper constructor for integer sized spaces
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

				System.out.println("Trying to insert key " + k);
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

				// Check that the tables were correctly updated
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
				Space toDelete = allocatedSpaces.get(k);
				allocatedSpaces.remove(k);
				addFreeSpace(toDelete.getSize(), toDelete.getOffset());
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

				System.out.println("Getting from address: "
						+ locationData.getOffset());

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

						// put the data address and write to store
						allocatedSpaces.put(k, newAllocationData);
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
			} else {
				throw new KeyNotFoundException(k);
			}
		} finally {
			w.unlock();
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