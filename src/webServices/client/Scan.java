
package webServices.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for scan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scan">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://webServices/}keyImpl" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://webServices/}keyImpl" minOccurs="0"/>
 *         &lt;element name="arg2" type="{http://webServices/}predicate" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scan", propOrder = {
    "arg0",
    "arg1",
    "arg2"
})
public class Scan {

    protected KeyImpl arg0;
    protected KeyImpl arg1;
    protected Predicate<?> arg2;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return
     *     possible object is
     *     {@link KeyImpl }
     *     
     */
    public KeyImpl getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyImpl }
     *     
     */
    public void setArg0(KeyImpl value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     * 
     * @return
     *     possible object is
     *     {@link KeyImpl }
     *     
     */
    public KeyImpl getArg1() {
        return arg1;
    }

    /**
     * Sets the value of the arg1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyImpl }
     *     
     */
    public void setArg1(KeyImpl value) {
        this.arg1 = value;
    }

    /**
     * Gets the value of the arg2 property.
     * 
     * @return
     *     possible object is
     *     {@link Predicate }
     *     
     */
    public Predicate getArg2() {
        return arg2;
    }

    /**
     * Sets the value of the arg2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Predicate }
     *     
     */
    public void setArg2(Predicate value) {
        this.arg2 = value;
    }

}
