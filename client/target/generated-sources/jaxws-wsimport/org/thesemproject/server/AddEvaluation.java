
package org.thesemproject.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for addEvaluation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addEvaluation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="server" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fieldConditionOperator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fieldConditionValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startPeriod" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="endPeriod" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="durationCondition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addEvaluation", propOrder = {
    "server",
    "name",
    "field",
    "fieldConditionOperator",
    "fieldConditionValue",
    "startPeriod",
    "endPeriod",
    "duration",
    "durationCondition",
    "score"
})
public class AddEvaluation {

    protected String server;
    protected String name;
    protected String field;
    protected String fieldConditionOperator;
    protected String fieldConditionValue;
    protected int startPeriod;
    protected int endPeriod;
    protected double duration;
    protected String durationCondition;
    protected double score;

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServer(String value) {
        this.server = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the field property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField() {
        return field;
    }

    /**
     * Sets the value of the field property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField(String value) {
        this.field = value;
    }

    /**
     * Gets the value of the fieldConditionOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldConditionOperator() {
        return fieldConditionOperator;
    }

    /**
     * Sets the value of the fieldConditionOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldConditionOperator(String value) {
        this.fieldConditionOperator = value;
    }

    /**
     * Gets the value of the fieldConditionValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldConditionValue() {
        return fieldConditionValue;
    }

    /**
     * Sets the value of the fieldConditionValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldConditionValue(String value) {
        this.fieldConditionValue = value;
    }

    /**
     * Gets the value of the startPeriod property.
     * 
     */
    public int getStartPeriod() {
        return startPeriod;
    }

    /**
     * Sets the value of the startPeriod property.
     * 
     */
    public void setStartPeriod(int value) {
        this.startPeriod = value;
    }

    /**
     * Gets the value of the endPeriod property.
     * 
     */
    public int getEndPeriod() {
        return endPeriod;
    }

    /**
     * Sets the value of the endPeriod property.
     * 
     */
    public void setEndPeriod(int value) {
        this.endPeriod = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     */
    public void setDuration(double value) {
        this.duration = value;
    }

    /**
     * Gets the value of the durationCondition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDurationCondition() {
        return durationCondition;
    }

    /**
     * Sets the value of the durationCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDurationCondition(String value) {
        this.durationCondition = value;
    }

    /**
     * Gets the value of the score property.
     * 
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     */
    public void setScore(double value) {
        this.score = value;
    }

}
