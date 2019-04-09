/**
 * ZSAP_OA_008_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sap.sap008.sap_com;

public class ZSAP_OA_008_ServiceLocator extends org.apache.axis.client.Service implements com.sap.sap008.sap_com.ZSAP_OA_008_Service {

    public ZSAP_OA_008_ServiceLocator() {
    }


    public ZSAP_OA_008_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ZSAP_OA_008_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ZSAP_OA_008
    private java.lang.String ZSAP_OA_008_address = "http://XXSAPDEV01.highrock.com.cn:8000/sap/bc/srt/rfc/sap/zoa2sap_cbly_dev/112/zsap_oa_008/zsap_oa_008";

    public java.lang.String getZSAP_OA_008Address() {
        return ZSAP_OA_008_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ZSAP_OA_008WSDDServiceName = "ZSAP_OA_008";

    public java.lang.String getZSAP_OA_008WSDDServiceName() {
        return ZSAP_OA_008WSDDServiceName;
    }

    public void setZSAP_OA_008WSDDServiceName(java.lang.String name) {
        ZSAP_OA_008WSDDServiceName = name;
    }

    public com.sap.sap008.sap_com.ZOA2SAP_CBLY_DEV getZSAP_OA_008() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ZSAP_OA_008_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getZSAP_OA_008(endpoint);
    }

    public com.sap.sap008.sap_com.ZOA2SAP_CBLY_DEV getZSAP_OA_008(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sap.sap008.sap_com.ZSAP_OA_008_BindingStub _stub = new com.sap.sap008.sap_com.ZSAP_OA_008_BindingStub(portAddress, this);
            _stub.setPortName(getZSAP_OA_008WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setZSAP_OA_008EndpointAddress(java.lang.String address) {
        ZSAP_OA_008_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sap.sap008.sap_com.ZOA2SAP_CBLY_DEV.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sap.sap008.sap_com.ZSAP_OA_008_BindingStub _stub = new com.sap.sap008.sap_com.ZSAP_OA_008_BindingStub(new java.net.URL(ZSAP_OA_008_address), this);
                _stub.setPortName(getZSAP_OA_008WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ZSAP_OA_008".equals(inputPortName)) {
            return getZSAP_OA_008();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSAP_OA_008");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSAP_OA_008"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ZSAP_OA_008".equals(portName)) {
            setZSAP_OA_008EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
