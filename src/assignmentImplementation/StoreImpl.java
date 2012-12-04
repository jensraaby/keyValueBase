package assignmentImplementation;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferOverflowException;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import keyValueBaseInterfaces.MemoryMappedFile;
import keyValueBaseInterfaces.Store;

public class StoreImpl implements Store
{
	/**
	 * @author Jens Raaby
	 * Store the memory mapped file privately
	 */
	private MemoryMappedFile memory;
	private RandomAccessFile raf;
	
	// Locking: parallel reads are OK.
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	private ReadLock r = lock.readLock();
	private WriteLock w = lock.writeLock();
	
	/**
	 * 
	 * @param filePath
	 * The path to a file for storage
	 * @param diskSize
	 * An initial file size (effectively it reserves space on the disk)
	 * @param totalSize
	 * Maximum addressable space. When the file is full, it can increase in size. 
	 * But I think an exception should be thrown if the initial size is > the total  
	 */
	public StoreImpl(String filePath, long totalSize) {
		
		
		try {
			// memory not persistent: file deleted when program completes!
			File storeFile = new File(filePath);
			storeFile.deleteOnExit();
			
			// Based partially on http://www.javamex.com/tutorials/io/nio_mapped_buffer.shtml
			// Create a file to hold data on disk
			raf = new RandomAccessFile(storeFile,"rw");
			
			// Preallocate Set file size (bytes)
			raf.setLength(totalSize); // e.g size = 2048, size will be 2.1 MB
			FileChannel fc = raf.getChannel();
			System.out.println("File size: " + fc.size());
			
			//n.b. buffer sets the initial position - 0 should be an OK start
			memory = new MemoryMappedFile(fc, FileChannel.MapMode.READ_WRITE, 0, totalSize);
		}
		catch (IOException ex) 
		{
			//TODO: handle exceptions
			ex.printStackTrace();
		}
		catch (IndexOutOfBoundsException ex) 
		{
			//TODO: handle exceptions
			ex.printStackTrace();
		}
		
		
	}
	
	//TODO: remove this method!
	public void closeFile() 
	{
		try {
			raf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Read takes a position
	 * 
	 * @param position
	 * The starting position in the address space
	 * 
	 * @param length
	 * The byte length - assumed to fit within the memory file (the calling method should check this)
	 */
	@Override
	public byte[] read(Long position, int length) {
		
		byte[] block = new byte[length];
		System.out.println("read byte length: " + block.length);
		
		memory.get(block, position);

		return block;
	}

	@Override
	public void write(Long position, byte[] value) {
		try {
			memory.put(value, position);
		}
		catch (BufferOverflowException ex) {
			ex.printStackTrace();
		}
		catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		
	}
	
}
