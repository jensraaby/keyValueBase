package assignmentImplementation;

import keyValueBaseInterfaces.Value;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement
@XmlAccessorType
@SuppressWarnings("serial")
public class ValueImpl implements Value
{
	@XmlElement
	private Integer value;
	
	public ValueImpl(Integer value) {
		this.value = value;
	}
	
	public ValueImpl() {
    }

	public String toString() {
		return this.value.toString();
	}
	
}

