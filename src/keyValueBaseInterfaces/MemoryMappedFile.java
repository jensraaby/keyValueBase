package keyValueBaseInterfaces;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;

 /**
  * @author PCSD - DIKU
  * 
  * This class overcomes the size limitation of 2GB of the library
  * MappedByteBuffer. Offers basic read/write functionality and
  * its usage is similar to MappedByteBuffer class.
  */
public class MemoryMappedFile{

	private static final long PAGE_SIZE = Integer.MAX_VALUE;
	private ArrayList<MappedByteBuffer> buffers = new ArrayList<MappedByteBuffer>();
	protected long position = 0;
	protected long totalSize;
	
	public MemoryMappedFile(FileChannel channel, FileChannel.MapMode mode, long offset, long totalSize)
			throws IndexOutOfBoundsException, IOException {
		
		this.totalSize = totalSize;
		long start = 0, length = 0;
		long fileSize = channel.size();
		
		if (offset + totalSize > fileSize)
			throw(new IndexOutOfBoundsException("The length specified is beyond the file boundaries."));
		
		for (int index = 0; start + length < fileSize; index++) {
		    if ((fileSize / PAGE_SIZE) == index)
		    	length = (fileSize - index *  PAGE_SIZE);
		    else
		    	length = PAGE_SIZE;
		    start = index * PAGE_SIZE;
		    buffers.add(index, channel.map(mode, start, length));
		}
	}
	
	/**
	* <p> Writes the given byte array into this buffer at the given
	* offset, at most the given length amount in bytes, and then
	* increments the position. </p>
	*
	* @param src
	* The byte array to be written to this buffer
	* @param offset
	* The starting position from where to write
	* @param length
	* The maximum amount of bytes copied to this buffer
	*
	* @return void
	*
	* @throws BufferOverflowException
	* If this buffer's current position is not smaller than its limit
	*
	* @throws IndexOutOfBoundsException
	* If this function attempts to access a position larger than the buffer limits
	*/
	public void put(byte[] src, long offset, long length)
			throws IndexOutOfBoundsException, BufferOverflowException{
		
		int page = (int)(offset/PAGE_SIZE);
		int index = (int)(offset%PAGE_SIZE);

		if (offset + src.length > totalSize)
			throw(new IndexOutOfBoundsException("The position and length of the destination array is beyond the file boundaries."));
		
		ByteBuffer srcBB = ByteBuffer.wrap(src);
		MappedByteBuffer dst;
		while (length > 0) {
			dst = (MappedByteBuffer) buffers.get(page).duplicate();
			dst.limit(dst.capacity());
			dst.position(index);
			srcBB.limit(srcBB.position()+Math.min(dst.remaining(),srcBB.capacity()-srcBB.position()));
			length -= srcBB.limit() - srcBB.position();
			dst.put(srcBB);
			page++;
			index = 0;
		}
		this.position = offset + length;
	}
	
	/**
	* <p> Writes the given byte array into this buffer at the given
	* offset, and then increments the position. </p>
	*
	* @param src
	* The byte array to be written to this buffer
	* @param offset
	* The starting position from where to write
	*
	* @return void
	*
	* @throws BufferOverflowException
	* If this buffer's current position is not smaller than its limit
	*
	* @throws IndexOutOfBoundsException
	* If this function attempts to access a position larger than the buffer limits
	*/
	public void put(byte[] src, long offset) throws IndexOutOfBoundsException, BufferOverflowException{
		this.put(src, offset, src.length);
	}

	/**
	* <p> Writes the given byte array into this buffer at the current
	* position, and then increments the position. </p>
	*
	* @param src
	* The byte array to be written to this buffer
	*
	* @return void
	*
	* @throws BufferOverflowException
	* If this buffer's current position is not smaller than its limit
	*
	* @throws IndexOutOfBoundsException
	* If this function attempts to access a position larger than the buffer limits
	*/
	public void put(byte[] src) throws IndexOutOfBoundsException, BufferOverflowException{
		this.put(src, this.position, src.length);
	}
	
	/**
	* <p> Reads the given byte array length from this buffer at the given
	* offset, and then increments the position. </p>
	*
	* @param dst
	* The destination byte array
	* @param offset
	* The starting position from where to read
	*
	* @return The read array into dst
	*
	* @throws BufferOverflowException
	* If this buffer's current position is not smaller than its limit
	*
	* @throws IndexOutOfBoundsException
	* If this function attempts to access a position larger than the buffer limits
	*/
	public void get(byte[] dst, long offset) throws IndexOutOfBoundsException, BufferOverflowException{
		int page = (int)(offset/PAGE_SIZE);
		int index = (int)(offset%PAGE_SIZE);
		int dstLength = dst.length;
		
		if (offset + dstLength > totalSize)
			throw(new IndexOutOfBoundsException("The position and length of the destination array is beyond the file boundaries."));
		
		ByteBuffer dstBB = ByteBuffer.wrap(dst);
		MappedByteBuffer src;
		
		while (dstLength > 0) {
			src = (MappedByteBuffer) buffers.get(page).duplicate();
			src.limit(index+Math.min(dstBB.remaining(),src.capacity()-index));
			src.position(index);
			dstBB.put(src);
			page++;
			dstLength -= src.limit() - index;
			index = 0;
		}
		this.position = offset + dstLength;
	}

	/**
	* <p> Reads the given byte array length from this buffer at the
	* current position, and then increments the position. </p>
	*
	* @param dst
	* The destination byte array
	*
	* @return The read array into dst
	*
	* @throws BufferOverflowException
	* If this buffer's current position is not smaller than its limit
	*
	* @throws IndexOutOfBoundsException
	* If this function attempts to access a position larger than the buffer limits
	*/
	public void get(byte[] dst) throws IndexOutOfBoundsException, BufferOverflowException{
		this.get(dst, this.position);
	}
}
