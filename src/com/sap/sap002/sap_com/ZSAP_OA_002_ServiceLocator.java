/**
 * ZSAP_OA_002_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sap.sap002.sap_com;

public class ZSAP_OA_002_ServiceLocator extends org.apache.axis.client.Service implements com.sap.sap002.sap_com.ZSAP_OA_002_Service {

    public ZSAP_OA_002_ServiceLocator() {
    }


    public ZSAP_OA_002_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ZSAP_OA_002_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OA_002
    private java.lang.String OA_002_address = "http://XXSAPDEV01.highrock.com.cn:8000/sap/bc/srt/rfc/sap/zsap_oa_002/112/zsap_oa_002/oa_002";

    public java.lang.String getOA_002Address() {
        return OA_002_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OA_002WSDDServiceName = "OA_002";

    public java.lang.String getOA_002WSDDServiceName() {
        return OA_002WSDDServiceName;
    }

    public void setOA_002WSDDServiceName(java.lang.String name) {
        OA_002WSDDServiceName = name;
    }

    public com.sap.sap002.sap_com.ZSAP_OA_002_PortType getOA_002() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OA_002_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOA_002(endpoint);
    }

    public com.sap.sap002.sap_com.ZSAP_OA_002_PortType getOA_002(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sap.sap002.sap_com.OA_002Stub _stub = new com.sap.sap002.sap_com.OA_002Stub(portAddress, this);
            _stub.setPortName(getOA_002WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOA_002EndpointAddress(java.lang.String address) {
        OA_002_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sap.sap002.sap_com.ZSAP_OA_002_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sap.sap002.sap_com.OA_002Stub _stub = new com.sap.sap002.sap_com.OA_002Stub(new java.net.URL(OA_002_address), this);
                _stub.setPortName(getOA_002WSDDServiceName());
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
        if ("OA_002".equals(inputPortName)) {
            return getOA_002();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSAP_OA_002");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "OA_002"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OA_002".equals(portName)) {
            setOA_002EndpointAddress(address);
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
