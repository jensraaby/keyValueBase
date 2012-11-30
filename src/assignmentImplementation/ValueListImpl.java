package assignmentImplementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import keyValueBaseInterfaces.ValueList;

@SuppressWarnings("serial")
public class ValueListImpl implements ValueList<ValueImpl>{
	
	private List<ValueImpl> vlist = new ArrayList<ValueImpl>();
	
	@Override
	public void add(ValueImpl v) {
		vlist.add(v);		
	}

	@Override
	public void remove(ValueImpl v) {
		vlist.remove(v);
		
	}

	@Override
	public void merge(ValueList<ValueImpl> v) {
		for (ValueImpl val : v) {
			vlist.add(val);
		}
	}

	@Override
	public String toString() {
		String output = new String("VALLIST: ");
		for (ValueImpl v : vlist) {
			output = output.concat(v.toString() + ",");
		}
		return output;
	}
	
	@Override
	public List<ValueImpl> toList() {
		return vlist;
	}
	
	@Override
	public Iterator<ValueImpl> iterator() {
		return vlist.iterator();
	}

}
