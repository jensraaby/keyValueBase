package assignmentImplementation;

import keyValueBaseInterfaces.Key;

public class KeyImpl implements Key<KeyImpl>
{
	// Simple: use integer
    private Integer key;
    
    // Construct a key using an integer in this case
    public KeyImpl(Integer key) {
    	this.key = key;
    }
	
	@Override
	public int compareTo(KeyImpl k2) {
		return key.compareTo(k2.key);
	}
	
	public String toString() {
		return key.toString();
	}

}
