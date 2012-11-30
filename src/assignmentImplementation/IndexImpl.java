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
	private Map<KeyImpl, Space> locationTable;
	private TreeSet<Space> freeSpaces;
	private ValueSerializerImpl serializer = new ValueSerializerImpl();

	/**
	 * getFreeSpace calculates the total current unassigned memory
	 * 
	 * @return the current total free space
	 */
	public long getFreeSpace() {
		long total = 0;
		for (Space s : freeSpaces) {
			System.out.println("free: " + s.size);
			total = total + s.size;
		}
		return total;
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

		public Integer getSize() {
			return size;
		}

		public void setSize(Integer size) {
			this.size = size;
		}

		public Long getOffset() {
			// TODO more graceful handling of non-positioning
			if (this.positioned)
				return this.offset;
			else {
				System.out.println("Non positioned!!");
				return -1L;
			}
			
		}

		public void setOffset(Long offset) {
			this.positioned = true;
			System.out.println("Now positioned :)");
			this.offset = offset;
			
		}

	}

	private class AllocatedSpace extends Space {

		private Date lastAccess;

		public AllocatedSpace(int size, long offset) {
			super(size, offset);
			updateAccessTime();
			// TODO Auto-generated constructor stub
		}

		private void updateAccessTime() {
			lastAccess = new Date();
		}

		@Override
		public Integer getSize() {
			updateAccessTime();
			return this.size;
		}

	}

	public IndexImpl(String storePath, int i) {
		try {
			storage = new StoreImpl(storePath, i);

			// Current free space is total address space
			Space initialFree = new Space(i, 0L);
			freeSpaces = new TreeSet<IndexImpl.Space>();
			putFreeSpace(initialFree);

			// TODO consider other data structures for index
			// needs to be protected and sorted by key ordering
			locationTable = new TreeMap<KeyImpl, Space>();
		} catch (Exception ex) {
			// TODO think about what needs handling!
			ex.printStackTrace();
		}

	}

	/**
	 * Safely add/update location information This checks that we are adding a
	 * space that is positioned
	 * 
	 * @param k
	 *            key to insert/update
	 * @param s
	 *            space for key
	 * @throws Exception
	 */
	private void putNewLocation(KeyImpl k, Space s) throws Exception {
		if (s.positioned) {
			System.out.println("Putting key " + k);
			locationTable.put(k, s);
		} else
			throw new Exception("Cannot use unpositioned address space");
	}
	private void putFreeSpace(Space s) throws Exception {
		if (s.positioned) {
			System.out.println("Putting free space");
			freeSpaces.add(s);
		} else
			throw new Exception("Cannot use unpositioned address space");
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException {

		// System.out.println("Trying to insert key " + k);
		if (locationTable.containsKey(k))
			throw new KeyAlreadyPresentException(k);
		else {
			System.out.println("1. Trying to insert key " + k);
			int countFreespaces = freeSpaces.size();
			int countAllocations = locationTable.size();
			// serialise valuelist to byte array
			byte[] data = serializer.toByteArray(v);

			try {
				System.out.println("inserting key " + k.toString());
				// Create space that fits this data:
				Space toAllocate = new Space(data.length);

				// find the free space that is smallest possible space to fit
				// this new data:
				Space selectedFreeSpace = freeSpaces.higher(toAllocate);

				// create new replacement free space (non-positioned):
				Space newFreeSpace = new Space(selectedFreeSpace.size
						- data.length);

				// put the data and store its location
				toAllocate.setOffset(selectedFreeSpace.getOffset());

				putNewLocation(k, toAllocate);
				storage.write(toAllocate.getOffset(), data);

				// update the freespaces structure
				newFreeSpace.setOffset(selectedFreeSpace.offset + data.length);
				putFreeSpace(newFreeSpace);
				freeSpaces.remove(selectedFreeSpace);

			} catch (Exception e) {
				// do nothing - i.e. rollback this insert
				e.printStackTrace();
			}

			// Check that the tables are correctly updated
			assert (countFreespaces == freeSpaces.size());
			assert (countAllocations == locationTable.size() + 1);
			// TODO: handle case where not big enough freespace
		}

	}

	@Override
	public void remove(KeyImpl k) throws KeyNotFoundException {
		try {
		if (locationTable.containsKey(k)) {
			// Locate space, add it to freespaces and remove mapping from key
			Space locationData = locationTable.get(k);
			putFreeSpace(locationData);
			locationTable.remove(k);

			System.out.println("defragging!");
			defragmentFreeSpace();

		} else
			throw new KeyNotFoundException(k);
		}
		catch(KeyNotFoundException ex) {
			throw new KeyNotFoundException(k);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	/**
	 * Returns the value list for the specified key
	 * @param k key to lookup
	 */
	public ValueListImpl get(KeyImpl k) throws KeyNotFoundException,
			IOException {

		if (locationTable.containsKey(k)) {
			Space locationData = locationTable.get(k);

			System.out.println("Gettign address: " + locationData.getOffset());
			// this could throw IOException!
			byte[] data = storage.read(locationData.getOffset(),
					locationData.getSize());

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
		if (locationTable.containsKey(k)) {

			Space locationData = locationTable.get(k);

			// find the size of the new value and the old value
			int oldSize = locationData.getSize();
			byte[] newValue = serializer.toByteArray(v);
			int newSize = newValue.length;

			// Handle size differences in the new and old value:
			if (oldSize == newSize) {
				// replace data in store
				storage.write(locationData.getOffset(),
						serializer.toByteArray(v));
			} else if (oldSize > newSize) {
				// If less memory is needed we can create a new free space.

				try {
					Space newSpace = new Space(newSize,
							locationData.getOffset());

					System.out.println("size of new smaller value: " + newSize);

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
	 * Defragmenter
	 * This searches the free memory spaces for neighbours (consecutive spaces)
	 * Then it merges those.
	 * 
	 * Recursive definition!
	 */
	private void defragmentFreeSpace() {
		// for each freespace, find any neighbours
		Space free = freeSpaces.first();
		long previousFreeSpace = getFreeSpace();
		Set<Space> neighbours = new HashSet<Space>();
		for (Space otherfree : freeSpaces) {
			if (neighbours.size() == 2)
				break;
			else {
				if (isNeighbour(free, otherfree))
					neighbours.add(otherfree);
			}
		}
		if (neighbours.size() > 0) {
			// Merge the free spaces
			int totalSize = 0;
			long minOffset = free.getOffset();
			for (Space n : neighbours) {
				totalSize = totalSize + n.getSize();
				if (n.getOffset() < minOffset)
					minOffset = n.getOffset();
			}
			Space mergedSpace = new Space(totalSize, minOffset);

			freeSpaces.removeAll(neighbours);
			freeSpaces.remove(free);
			freeSpaces.add(mergedSpace);
			
			//TODO defragmentFreeSpace();

		} else {
			// base case - stop when no neighbours
		}
		assert (previousFreeSpace == getFreeSpace());
	}

	/**
	 * Helper method determines if 2 spaces are next to each other This can be
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