
package webServices.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BeginGreaterThanEndException complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BeginGreaterThanEndException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="beginKey" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="endKey" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeginGreaterThanEndException", propOrder = {
    "beginKey",
    "endKey",
    "message"
})
public class BeginGreaterThanEndException {

    protected Object beginKey;
    protected Object endKey;
    protected String message;

    /**
     * Gets the value of the beginKey property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getBeginKey() {
        return beginKey;
    }

    /**
     * Sets the value of the beginKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setBeginKey(Object value) {
        this.beginKey = value;
    }

    /**
     * Gets the value of the endKey property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEndKey() {
        return endKey;
    }

    /**
     * Sets the value of the endKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEndKey(Object value) {
        this.endKey = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

}
