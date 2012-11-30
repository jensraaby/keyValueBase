package assignmentImplementation;

import keyValueBaseInterfaces.Value;

@SuppressWarnings("serial")
public class ValueImpl implements Value
{
	private Integer value;
	
	public ValueImpl(Integer value) {
		this.value = value;
	}

	public String toString() {
		return this.value.toString();
	}
	
}

