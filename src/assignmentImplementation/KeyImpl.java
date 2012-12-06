package assignmentImplementation;

import keyValueBaseInterfaces.Key;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement
@XmlAccessorType
public final class KeyImpl implements Key<KeyImpl>
{
	// Simple: use integer
	@XmlElement
    private Integer key;
    
    // Construct a key using an integer in this case
    public KeyImpl(int key) {
    	this.key = Integer.valueOf(key);
    }
    
    public KeyImpl() {
    	key = -1; // horrible horrible horrible
    }
	
	@Override
	public int compareTo(KeyImpl k2) {
		return key.compareTo(k2.key);
	}
	
	public String toString() {
		return key.toString();
	}

}
