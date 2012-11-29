package assignmentImplementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.media.j3d.Background;

import keyValueBaseInterfaces.ValueSerializer;

public class ValueSerializerImpl implements ValueSerializer<ValueListImpl> {

	@Override
	public ValueListImpl fromByteArray(byte[] array) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(array);
		ObjectInputStream objInputStream = new ObjectInputStream(bis);
		Object vl;
		try {
			vl = objInputStream.readObject();
			
			if (vl instanceof ValueListImpl) {
				return (ValueListImpl) vl;
			}
			else {
				throw new IOException();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public byte[] toByteArray(ValueListImpl v) throws IOException {
		// from http://www.wikihow.com/Serialize-an-Object-in-Java
		ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
		ObjectOutputStream out = new ObjectOutputStream(bos) ;
		out.writeObject(v);
		out.close();

		// Get the bytes of the serialized object
		byte[] buf = bos.toByteArray();
		return buf;
	}

}
