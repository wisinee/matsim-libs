//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.14 at 05:31:41 PM CET 
//


package playground.tnicolai.matsim4opus.matsim4urbansim.jaxbconfig2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accessibilityParameterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accessibilityParameterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="useLogitScaleParameterFromMATSim" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="useCarParameterFromMATSim" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="useWalkParameterFromMATSim" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="useRawSumsWithoutLn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="logitScaleParameter" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarTravelTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarTravelTimePower2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarLnTravelTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarTravelDistance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarTravelDistancePower2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarLnTravelDistance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarTravelCost" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarTravelCostPower2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaCarLnTravelCost" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkTravelTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkTravelTimePower2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkLnTravelTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkTravelDistance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkTravelDistancePower2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkLnTravelDistance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkTravelCost" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkTravelCostPower2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="betaWalkLnTravelCost" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accessibilityParameterType", propOrder = {
    "useLogitScaleParameterFromMATSim",
    "useCarParameterFromMATSim",
    "useWalkParameterFromMATSim",
    "useRawSumsWithoutLn",
    "logitScaleParameter",
    "betaCarTravelTime",
    "betaCarTravelTimePower2",
    "betaCarLnTravelTime",
    "betaCarTravelDistance",
    "betaCarTravelDistancePower2",
    "betaCarLnTravelDistance",
    "betaCarTravelCost",
    "betaCarTravelCostPower2",
    "betaCarLnTravelCost",
    "betaWalkTravelTime",
    "betaWalkTravelTimePower2",
    "betaWalkLnTravelTime",
    "betaWalkTravelDistance",
    "betaWalkTravelDistancePower2",
    "betaWalkLnTravelDistance",
    "betaWalkTravelCost",
    "betaWalkTravelCostPower2",
    "betaWalkLnTravelCost"
})
public class AccessibilityParameterType {

    protected boolean useLogitScaleParameterFromMATSim;
    protected boolean useCarParameterFromMATSim;
    protected boolean useWalkParameterFromMATSim;
    protected boolean useRawSumsWithoutLn;
    protected double logitScaleParameter;
    protected double betaCarTravelTime;
    protected double betaCarTravelTimePower2;
    protected double betaCarLnTravelTime;
    protected double betaCarTravelDistance;
    protected double betaCarTravelDistancePower2;
    protected double betaCarLnTravelDistance;
    protected double betaCarTravelCost;
    protected double betaCarTravelCostPower2;
    protected double betaCarLnTravelCost;
    protected double betaWalkTravelTime;
    protected double betaWalkTravelTimePower2;
    protected double betaWalkLnTravelTime;
    protected double betaWalkTravelDistance;
    protected double betaWalkTravelDistancePower2;
    protected double betaWalkLnTravelDistance;
    protected double betaWalkTravelCost;
    protected double betaWalkTravelCostPower2;
    protected double betaWalkLnTravelCost;

    /**
     * Gets the value of the useLogitScaleParameterFromMATSim property.
     * 
     */
    public boolean isUseLogitScaleParameterFromMATSim() {
        return useLogitScaleParameterFromMATSim;
    }

    /**
     * Sets the value of the useLogitScaleParameterFromMATSim property.
     * 
     */
    public void setUseLogitScaleParameterFromMATSim(boolean value) {
        this.useLogitScaleParameterFromMATSim = value;
    }

    /**
     * Gets the value of the useCarParameterFromMATSim property.
     * 
     */
    public boolean isUseCarParameterFromMATSim() {
        return useCarParameterFromMATSim;
    }

    /**
     * Sets the value of the useCarParameterFromMATSim property.
     * 
     */
    public void setUseCarParameterFromMATSim(boolean value) {
        this.useCarParameterFromMATSim = value;
    }

    /**
     * Gets the value of the useWalkParameterFromMATSim property.
     * 
     */
    public boolean isUseWalkParameterFromMATSim() {
        return useWalkParameterFromMATSim;
    }

    /**
     * Sets the value of the useWalkParameterFromMATSim property.
     * 
     */
    public void setUseWalkParameterFromMATSim(boolean value) {
        this.useWalkParameterFromMATSim = value;
    }

    /**
     * Gets the value of the useRawSumsWithoutLn property.
     * 
     */
    public boolean isUseRawSumsWithoutLn() {
        return useRawSumsWithoutLn;
    }

    /**
     * Sets the value of the useRawSumsWithoutLn property.
     * 
     */
    public void setUseRawSumsWithoutLn(boolean value) {
        this.useRawSumsWithoutLn = value;
    }

    /**
     * Gets the value of the logitScaleParameter property.
     * 
     */
    public double getLogitScaleParameter() {
        return logitScaleParameter;
    }

    /**
     * Sets the value of the logitScaleParameter property.
     * 
     */
    public void setLogitScaleParameter(double value) {
        this.logitScaleParameter = value;
    }

    /**
     * Gets the value of the betaCarTravelTime property.
     * 
     */
    public double getBetaCarTravelTime() {
        return betaCarTravelTime;
    }

    /**
     * Sets the value of the betaCarTravelTime property.
     * 
     */
    public void setBetaCarTravelTime(double value) {
        this.betaCarTravelTime = value;
    }

    /**
     * Gets the value of the betaCarTravelTimePower2 property.
     * 
     */
    public double getBetaCarTravelTimePower2() {
        return betaCarTravelTimePower2;
    }

    /**
     * Sets the value of the betaCarTravelTimePower2 property.
     * 
     */
    public void setBetaCarTravelTimePower2(double value) {
        this.betaCarTravelTimePower2 = value;
    }

    /**
     * Gets the value of the betaCarLnTravelTime property.
     * 
     */
    public double getBetaCarLnTravelTime() {
        return betaCarLnTravelTime;
    }

    /**
     * Sets the value of the betaCarLnTravelTime property.
     * 
     */
    public void setBetaCarLnTravelTime(double value) {
        this.betaCarLnTravelTime = value;
    }

    /**
     * Gets the value of the betaCarTravelDistance property.
     * 
     */
    public double getBetaCarTravelDistance() {
        return betaCarTravelDistance;
    }

    /**
     * Sets the value of the betaCarTravelDistance property.
     * 
     */
    public void setBetaCarTravelDistance(double value) {
        this.betaCarTravelDistance = value;
    }

    /**
     * Gets the value of the betaCarTravelDistancePower2 property.
     * 
     */
    public double getBetaCarTravelDistancePower2() {
        return betaCarTravelDistancePower2;
    }

    /**
     * Sets the value of the betaCarTravelDistancePower2 property.
     * 
     */
    public void setBetaCarTravelDistancePower2(double value) {
        this.betaCarTravelDistancePower2 = value;
    }

    /**
     * Gets the value of the betaCarLnTravelDistance property.
     * 
     */
    public double getBetaCarLnTravelDistance() {
        return betaCarLnTravelDistance;
    }

    /**
     * Sets the value of the betaCarLnTravelDistance property.
     * 
     */
    public void setBetaCarLnTravelDistance(double value) {
        this.betaCarLnTravelDistance = value;
    }

    /**
     * Gets the value of the betaCarTravelCost property.
     * 
     */
    public double getBetaCarTravelCost() {
        return betaCarTravelCost;
    }

    /**
     * Sets the value of the betaCarTravelCost property.
     * 
     */
    public void setBetaCarTravelCost(double value) {
        this.betaCarTravelCost = value;
    }

    /**
     * Gets the value of the betaCarTravelCostPower2 property.
     * 
     */
    public double getBetaCarTravelCostPower2() {
        return betaCarTravelCostPower2;
    }

    /**
     * Sets the value of the betaCarTravelCostPower2 property.
     * 
     */
    public void setBetaCarTravelCostPower2(double value) {
        this.betaCarTravelCostPower2 = value;
    }

    /**
     * Gets the value of the betaCarLnTravelCost property.
     * 
     */
    public double getBetaCarLnTravelCost() {
        return betaCarLnTravelCost;
    }

    /**
     * Sets the value of the betaCarLnTravelCost property.
     * 
     */
    public void setBetaCarLnTravelCost(double value) {
        this.betaCarLnTravelCost = value;
    }

    /**
     * Gets the value of the betaWalkTravelTime property.
     * 
     */
    public double getBetaWalkTravelTime() {
        return betaWalkTravelTime;
    }

    /**
     * Sets the value of the betaWalkTravelTime property.
     * 
     */
    public void setBetaWalkTravelTime(double value) {
        this.betaWalkTravelTime = value;
    }

    /**
     * Gets the value of the betaWalkTravelTimePower2 property.
     * 
     */
    public double getBetaWalkTravelTimePower2() {
        return betaWalkTravelTimePower2;
    }

    /**
     * Sets the value of the betaWalkTravelTimePower2 property.
     * 
     */
    public void setBetaWalkTravelTimePower2(double value) {
        this.betaWalkTravelTimePower2 = value;
    }

    /**
     * Gets the value of the betaWalkLnTravelTime property.
     * 
     */
    public double getBetaWalkLnTravelTime() {
        return betaWalkLnTravelTime;
    }

    /**
     * Sets the value of the betaWalkLnTravelTime property.
     * 
     */
    public void setBetaWalkLnTravelTime(double value) {
        this.betaWalkLnTravelTime = value;
    }

    /**
     * Gets the value of the betaWalkTravelDistance property.
     * 
     */
    public double getBetaWalkTravelDistance() {
        return betaWalkTravelDistance;
    }

    /**
     * Sets the value of the betaWalkTravelDistance property.
     * 
     */
    public void setBetaWalkTravelDistance(double value) {
        this.betaWalkTravelDistance = value;
    }

    /**
     * Gets the value of the betaWalkTravelDistancePower2 property.
     * 
     */
    public double getBetaWalkTravelDistancePower2() {
        return betaWalkTravelDistancePower2;
    }

    /**
     * Sets the value of the betaWalkTravelDistancePower2 property.
     * 
     */
    public void setBetaWalkTravelDistancePower2(double value) {
        this.betaWalkTravelDistancePower2 = value;
    }

    /**
     * Gets the value of the betaWalkLnTravelDistance property.
     * 
     */
    public double getBetaWalkLnTravelDistance() {
        return betaWalkLnTravelDistance;
    }

    /**
     * Sets the value of the betaWalkLnTravelDistance property.
     * 
     */
    public void setBetaWalkLnTravelDistance(double value) {
        this.betaWalkLnTravelDistance = value;
    }

    /**
     * Gets the value of the betaWalkTravelCost property.
     * 
     */
    public double getBetaWalkTravelCost() {
        return betaWalkTravelCost;
    }

    /**
     * Sets the value of the betaWalkTravelCost property.
     * 
     */
    public void setBetaWalkTravelCost(double value) {
        this.betaWalkTravelCost = value;
    }

    /**
     * Gets the value of the betaWalkTravelCostPower2 property.
     * 
     */
    public double getBetaWalkTravelCostPower2() {
        return betaWalkTravelCostPower2;
    }

    /**
     * Sets the value of the betaWalkTravelCostPower2 property.
     * 
     */
    public void setBetaWalkTravelCostPower2(double value) {
        this.betaWalkTravelCostPower2 = value;
    }

    /**
     * Gets the value of the betaWalkLnTravelCost property.
     * 
     */
    public double getBetaWalkLnTravelCost() {
        return betaWalkLnTravelCost;
    }

    /**
     * Sets the value of the betaWalkLnTravelCost property.
     * 
     */
    public void setBetaWalkLnTravelCost(double value) {
        this.betaWalkLnTravelCost = value;
    }

}
