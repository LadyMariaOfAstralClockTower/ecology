/**
 * ZSPRETURN.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sap.sap002.sap_com;

public class ZSPRETURN  implements java.io.Serializable {
    private java.lang.String BANFN;

    private java.lang.String BNFPO;

    private java.lang.String FRGZU;

    public ZSPRETURN() {
    }

    public ZSPRETURN(
           java.lang.String BANFN,
           java.lang.String BNFPO,
           java.lang.String FRGZU) {
           this.BANFN = BANFN;
           this.BNFPO = BNFPO;
           this.FRGZU = FRGZU;
    }


    /**
     * Gets the BANFN value for this ZSPRETURN.
     * 
     * @return BANFN
     */
    public java.lang.String getBANFN() {
        return BANFN;
    }


    /**
     * Sets the BANFN value for this ZSPRETURN.
     * 
     * @param BANFN
     */
    public void setBANFN(java.lang.String BANFN) {
        this.BANFN = BANFN;
    }


    /**
     * Gets the BNFPO value for this ZSPRETURN.
     * 
     * @return BNFPO
     */
    public java.lang.String getBNFPO() {
        return BNFPO;
    }


    /**
     * Sets the BNFPO value for this ZSPRETURN.
     * 
     * @param BNFPO
     */
    public void setBNFPO(java.lang.String BNFPO) {
        this.BNFPO = BNFPO;
    }


    /**
     * Gets the FRGZU value for this ZSPRETURN.
     * 
     * @return FRGZU
     */
    public java.lang.String getFRGZU() {
        return FRGZU;
    }


    /**
     * Sets the FRGZU value for this ZSPRETURN.
     * 
     * @param FRGZU
     */
    public void setFRGZU(java.lang.String FRGZU) {
        this.FRGZU = FRGZU;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ZSPRETURN)) return false;
        ZSPRETURN other = (ZSPRETURN) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BANFN==null && other.getBANFN()==null) || 
             (this.BANFN!=null &&
              this.BANFN.equals(other.getBANFN()))) &&
            ((this.BNFPO==null && other.getBNFPO()==null) || 
             (this.BNFPO!=null &&
              this.BNFPO.equals(other.getBNFPO()))) &&
            ((this.FRGZU==null && other.getFRGZU()==null) || 
             (this.FRGZU!=null &&
              this.FRGZU.equals(other.getFRGZU())));
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
        if (getBANFN() != null) {
            _hashCode += getBANFN().hashCode();
        }
        if (getBNFPO() != null) {
            _hashCode += getBNFPO().hashCode();
        }
        if (getFRGZU() != null) {
            _hashCode += getFRGZU().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ZSPRETURN.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSPRETURN"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BANFN");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BANFN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BNFPO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BNFPO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FRGZU");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FRGZU"));
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
