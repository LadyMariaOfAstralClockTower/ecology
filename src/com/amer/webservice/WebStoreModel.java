/**
 * WebStoreModel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.amer.webservice;

public class WebStoreModel  implements java.io.Serializable {
    private java.lang.String startDate;

    private java.lang.String endDate;

    private java.lang.String recordRows;

    private java.lang.String status;

    private java.lang.String errorValue;

    private com.amer.webservice.WebStoreDetailModel[] items;

    public WebStoreModel() {
    }

    public WebStoreModel(
           java.lang.String startDate,
           java.lang.String endDate,
           java.lang.String recordRows,
           java.lang.String status,
           java.lang.String errorValue,
           com.amer.webservice.WebStoreDetailModel[] items) {
           this.startDate = startDate;
           this.endDate = endDate;
           this.recordRows = recordRows;
           this.status = status;
           this.errorValue = errorValue;
           this.items = items;
    }


    /**
     * Gets the startDate value for this WebStoreModel.
     * 
     * @return startDate
     */
    public java.lang.String getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this WebStoreModel.
     * 
     * @param startDate
     */
    public void setStartDate(java.lang.String startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the endDate value for this WebStoreModel.
     * 
     * @return endDate
     */
    public java.lang.String getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this WebStoreModel.
     * 
     * @param endDate
     */
    public void setEndDate(java.lang.String endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the recordRows value for this WebStoreModel.
     * 
     * @return recordRows
     */
    public java.lang.String getRecordRows() {
        return recordRows;
    }


    /**
     * Sets the recordRows value for this WebStoreModel.
     * 
     * @param recordRows
     */
    public void setRecordRows(java.lang.String recordRows) {
        this.recordRows = recordRows;
    }


    /**
     * Gets the status value for this WebStoreModel.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this WebStoreModel.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the errorValue value for this WebStoreModel.
     * 
     * @return errorValue
     */
    public java.lang.String getErrorValue() {
        return errorValue;
    }


    /**
     * Sets the errorValue value for this WebStoreModel.
     * 
     * @param errorValue
     */
    public void setErrorValue(java.lang.String errorValue) {
        this.errorValue = errorValue;
    }


    /**
     * Gets the items value for this WebStoreModel.
     * 
     * @return items
     */
    public com.amer.webservice.WebStoreDetailModel[] getItems() {
        return items;
    }


    /**
     * Sets the items value for this WebStoreModel.
     * 
     * @param items
     */
    public void setItems(com.amer.webservice.WebStoreDetailModel[] items) {
        this.items = items;
    }

    public com.amer.webservice.WebStoreDetailModel getItems(int i) {
        return this.items[i];
    }

    public void setItems(int i, com.amer.webservice.WebStoreDetailModel _value) {
        this.items[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WebStoreModel)) return false;
        WebStoreModel other = (WebStoreModel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.recordRows==null && other.getRecordRows()==null) || 
             (this.recordRows!=null &&
              this.recordRows.equals(other.getRecordRows()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.errorValue==null && other.getErrorValue()==null) || 
             (this.errorValue!=null &&
              this.errorValue.equals(other.getErrorValue()))) &&
            ((this.items==null && other.getItems()==null) || 
             (this.items!=null &&
              java.util.Arrays.equals(this.items, other.getItems())));
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
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getRecordRows() != null) {
            _hashCode += getRecordRows().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getErrorValue() != null) {
            _hashCode += getErrorValue().hashCode();
        }
        if (getItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WebStoreModel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.faseforce.net", "webStoreModel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordRows");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recordRows"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errorValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("items");
        elemField.setXmlName(new javax.xml.namespace.QName("", "items"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.faseforce.net", "webStoreDetailModel"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
