/**
 * FaseWebServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.amer.webservice;

public class FaseWebServiceImplServiceLocator extends org.apache.axis.client.Service implements com.amer.webservice.FaseWebServiceImplService {

    public FaseWebServiceImplServiceLocator() {
    }


    public FaseWebServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FaseWebServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FaseWebServiceImplPort
    private java.lang.String FaseWebServiceImplPort_address = "https://amersales.faseforce.net/WebService/webService";

    public java.lang.String getFaseWebServiceImplPortAddress() {
        return FaseWebServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FaseWebServiceImplPortWSDDServiceName = "FaseWebServiceImplPort";

    public java.lang.String getFaseWebServiceImplPortWSDDServiceName() {
        return FaseWebServiceImplPortWSDDServiceName;
    }

    public void setFaseWebServiceImplPortWSDDServiceName(java.lang.String name) {
        FaseWebServiceImplPortWSDDServiceName = name;
    }

    public com.amer.webservice.FaseWebService getFaseWebServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FaseWebServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFaseWebServiceImplPort(endpoint);
    }

    public com.amer.webservice.FaseWebService getFaseWebServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.amer.webservice.FaseWebServiceImplServiceSoapBindingStub _stub = new com.amer.webservice.FaseWebServiceImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getFaseWebServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFaseWebServiceImplPortEndpointAddress(java.lang.String address) {
        FaseWebServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.amer.webservice.FaseWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.amer.webservice.FaseWebServiceImplServiceSoapBindingStub _stub = new com.amer.webservice.FaseWebServiceImplServiceSoapBindingStub(new java.net.URL(FaseWebServiceImplPort_address), this);
                _stub.setPortName(getFaseWebServiceImplPortWSDDServiceName());
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
        if ("FaseWebServiceImplPort".equals(inputPortName)) {
            return getFaseWebServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.faseforce.net", "FaseWebServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.faseforce.net", "FaseWebServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FaseWebServiceImplPort".equals(portName)) {
            setFaseWebServiceImplPortEndpointAddress(address);
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
