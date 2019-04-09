/**
 * ZOA2SAP_PORELEASE_DEV_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sap.sap004.sap_com;

public class ZOA2SAP_PORELEASE_DEV_ServiceLocator extends org.apache.axis.client.Service implements com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_Service {

    public ZOA2SAP_PORELEASE_DEV_ServiceLocator() {
    }


    public ZOA2SAP_PORELEASE_DEV_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ZOA2SAP_PORELEASE_DEV_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ZSAP_OA_004
    private java.lang.String ZSAP_OA_004_address = "http://XXSAPDEV01.highrock.com.cn:8000/sap/bc/srt/rfc/sap/zoa2sap_porelease_dev/112/zoa2sap_porelease_dev/zsap_oa_004";

    public java.lang.String getZSAP_OA_004Address() {
        return ZSAP_OA_004_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ZSAP_OA_004WSDDServiceName = "ZSAP_OA_004";

    public java.lang.String getZSAP_OA_004WSDDServiceName() {
        return ZSAP_OA_004WSDDServiceName;
    }

    public void setZSAP_OA_004WSDDServiceName(java.lang.String name) {
        ZSAP_OA_004WSDDServiceName = name;
    }

    public com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_PortType getZSAP_OA_004() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ZSAP_OA_004_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getZSAP_OA_004(endpoint);
    }

    public com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_PortType getZSAP_OA_004(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sap.sap004.sap_com.ZSAP_OA_004Stub _stub = new com.sap.sap004.sap_com.ZSAP_OA_004Stub(portAddress, this);
            _stub.setPortName(getZSAP_OA_004WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setZSAP_OA_004EndpointAddress(java.lang.String address) {
        ZSAP_OA_004_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sap.sap004.sap_com.ZSAP_OA_004Stub _stub = new com.sap.sap004.sap_com.ZSAP_OA_004Stub(new java.net.URL(ZSAP_OA_004_address), this);
                _stub.setPortName(getZSAP_OA_004WSDDServiceName());
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
        if ("ZSAP_OA_004".equals(inputPortName)) {
            return getZSAP_OA_004();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZOA2SAP_PORELEASE_DEV");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:sap-com:document:sap:rfc:functions", "ZSAP_OA_004"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ZSAP_OA_004".equals(portName)) {
            setZSAP_OA_004EndpointAddress(address);
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
