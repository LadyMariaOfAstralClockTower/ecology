/**
 * ZOA_TEST_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sap.sap_com;

public class ZOA_TEST_ServiceLocator extends org.apache.axis.client.Service implements com.sap.sap_com.ZOA_TEST_Service {

    public ZOA_TEST_ServiceLocator() {
    }


    public ZOA_TEST_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ZOA_TEST_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ZOA_TEST
    private java.lang.String ZOA_TEST_address = "http://XXSAPDEV01.highrock.com.cn:8000/sap/bc/srt/rfc/sap/zoa_test/110/zoa_test/zoa_test";

    public java.lang.String getZOA_TESTAddress() {
        return ZOA_TEST_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ZOA_TESTWSDDServiceName = "ZOA_TEST";

    public java.lang.String getZOA_TESTWSDDServiceName() {
        return ZOA_TESTWSDDServiceName;
    }

    public void setZOA_TESTWSDDServiceName(java.lang.String name) {
        ZOA_TESTWSDDServiceName = name;
    }

    public com.sap.sap_com.ZOA_TEST_PortType getZOA_TEST() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ZOA_TEST_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getZOA_TEST(endpoint);
    }

    public com.sap.sap_com.ZOA_TEST_PortType getZOA_TEST(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sap.sap_com.ZOA_TEST_BindingStub _stub = new com.sap.sap_com.ZOA_TEST_BindingStub(portAddress, this);
            _stub.setPortName(getZOA_TESTWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setZOA_TESTEndpointAddress(java.lang.String address) {
        ZOA_TEST_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sap.sap_com.ZOA_TEST_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sap.sap_com.ZOA_TEST_BindingStub _stub = new com.sap.sap_com.ZOA_TEST_BindingStub(new java.net.URL(ZOA_TEST_address), this);
                _stub.setPortName(getZOA_TESTWSDDServiceName());
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
        if ("ZOA_TEST".equals(inputPortName)) {
            return getZOA_TEST();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZOA_TEST");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZOA_TEST"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ZOA_TEST".equals(portName)) {
            setZOA_TESTEndpointAddress(address);
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
