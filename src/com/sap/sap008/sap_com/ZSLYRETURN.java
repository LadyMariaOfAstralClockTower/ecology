/**
 * ZSLYRETURN.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sap.sap008.sap_com;

public class ZSLYRETURN  implements java.io.Serializable {
    private java.lang.String RSNUM;

    private java.lang.String XWAOK;

    public ZSLYRETURN() {
    }

    public ZSLYRETURN(
           java.lang.String RSNUM,
           java.lang.String XWAOK) {
           this.RSNUM = RSNUM;
           this.XWAOK = XWAOK;
    }


    /**
     * Gets the RSNUM value for this ZSLYRETURN.
     * 
     * @return RSNUM
     */
    public java.lang.String getRSNUM() {
        return RSNUM;
    }


    /**
     * Sets the RSNUM value for this ZSLYRETURN.
     * 
     * @param RSNUM
     */
    public void setRSNUM(java.lang.String RSNUM) {
        this.RSNUM = RSNUM;
    }


    /**
     * Gets the XWAOK value for this ZSLYRETURN.
     * 
     * @return XWAOK
     */
    public java.lang.String getXWAOK() {
        return XWAOK;
    }


    /**
     * Sets the XWAOK value for this ZSLYRETURN.
     * 
     * @param XWAOK
     */
    public void setXWAOK(java.lang.String XWAOK) {
        this.XWAOK = XWAOK;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ZSLYRETURN)) return false;
        ZSLYRETURN other = (ZSLYRETURN) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.RSNUM==null && other.getRSNUM()==null) || 
             (this.RSNUM!=null &&
              this.RSNUM.equals(other.getRSNUM()))) &&
            ((this.XWAOK==null && other.getXWAOK()==null) || 
             (this.XWAOK!=null &&
              this.XWAOK.equals(other.getXWAOK())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRSNUM() != null) {
            _hashCode += getRSNUM().hashCode();
        }
        if (getXWAOK() != null) {
            _hashCode += getXWAOK().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSLYRETURN.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSLYRETURN"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RSNUM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RSNUM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XWAOK");
        elemField.setXmlName(new javax.xml.namespace.QName("", "XWAOK"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
