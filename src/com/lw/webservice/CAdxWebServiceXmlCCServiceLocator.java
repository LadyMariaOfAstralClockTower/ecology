/**
 * CAdxWebServiceXmlCCServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lw.webservice;

public class CAdxWebServiceXmlCCServiceLocator extends org.apache.axis.client.Service implements com.lw.webservice.CAdxWebServiceXmlCCService {

    public CAdxWebServiceXmlCCServiceLocator() {
    }


    public CAdxWebServiceXmlCCServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CAdxWebServiceXmlCCServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CAdxWebServiceXmlCC
    private java.lang.String CAdxWebServiceXmlCC_address = "http://139.224.192.211:28880/adxwsvc/services/CAdxWebServiceXmlCC";

    public java.lang.String getCAdxWebServiceXmlCCAddress() {
        return CAdxWebServiceXmlCC_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CAdxWebServiceXmlCCWSDDServiceName = "CAdxWebServiceXmlCC";

    public java.lang.String getCAdxWebServiceXmlCCWSDDServiceName() {
        return CAdxWebServiceXmlCCWSDDServiceName;
    }

    public void setCAdxWebServiceXmlCCWSDDServiceName(java.lang.String name) {
        CAdxWebServiceXmlCCWSDDServiceName = name;
    }

    public com.lw.webservice.CAdxWebServiceXmlCC getCAdxWebServiceXmlCC() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CAdxWebServiceXmlCC_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCAdxWebServiceXmlCC(endpoint);
    }

    public com.lw.webservice.CAdxWebServiceXmlCC getCAdxWebServiceXmlCC(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.lw.webservice.CAdxWebServiceXmlCCSoapBindingStub _stub = new com.lw.webservice.CAdxWebServiceXmlCCSoapBindingStub(portAddress, this);
            _stub.setPortName(getCAdxWebServiceXmlCCWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCAdxWebServiceXmlCCEndpointAddress(java.lang.String address) {
        CAdxWebServiceXmlCC_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.lw.webservice.CAdxWebServiceXmlCC.class.isAssignableFrom(serviceEndpointInterface)) {
                com.lw.webservice.CAdxWebServiceXmlCCSoapBindingStub _stub = new com.lw.webservice.CAdxWebServiceXmlCCSoapBindingStub(new java.net.URL(CAdxWebServiceXmlCC_address), this);
                _stub.setPortName(getCAdxWebServiceXmlCCWSDDServiceName());
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
        if ("CAdxWebServiceXmlCC".equals(inputPortName)) {
            return getCAdxWebServiceXmlCC();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.adonix.com/WSS", "CAdxWebServiceXmlCCService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.adonix.com/WSS", "CAdxWebServiceXmlCC"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CAdxWebServiceXmlCC".equals(portName)) {
            setCAdxWebServiceXmlCCEndpointAddress(address);
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
