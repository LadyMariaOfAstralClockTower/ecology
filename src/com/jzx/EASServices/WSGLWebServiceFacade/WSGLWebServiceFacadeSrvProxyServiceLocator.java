/**
 * WSGLWebServiceFacadeSrvProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jzx.EASServices.WSGLWebServiceFacade;

public class WSGLWebServiceFacadeSrvProxyServiceLocator extends org.apache.axis.client.Service implements com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxyService {

    public WSGLWebServiceFacadeSrvProxyServiceLocator() {
    }


    public WSGLWebServiceFacadeSrvProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSGLWebServiceFacadeSrvProxyServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSGLWebServiceFacade
    private java.lang.String WSGLWebServiceFacade_address = "http://172.172.0.20:6888/ormrpc/services/WSGLWebServiceFacade";

    public java.lang.String getWSGLWebServiceFacadeAddress() {
        return WSGLWebServiceFacade_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSGLWebServiceFacadeWSDDServiceName = "WSGLWebServiceFacade";

    public java.lang.String getWSGLWebServiceFacadeWSDDServiceName() {
        return WSGLWebServiceFacadeWSDDServiceName;
    }

    public void setWSGLWebServiceFacadeWSDDServiceName(java.lang.String name) {
        WSGLWebServiceFacadeWSDDServiceName = name;
    }

    public com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxy getWSGLWebServiceFacade() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSGLWebServiceFacade_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSGLWebServiceFacade(endpoint);
    }

    public com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxy getWSGLWebServiceFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSoapBindingStub _stub = new com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSGLWebServiceFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSGLWebServiceFacadeEndpointAddress(java.lang.String address) {
        WSGLWebServiceFacade_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSoapBindingStub _stub = new com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSoapBindingStub(new java.net.URL(WSGLWebServiceFacade_address), this);
                _stub.setPortName(getWSGLWebServiceFacadeWSDDServiceName());
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
        if ("WSGLWebServiceFacade".equals(inputPortName)) {
            return getWSGLWebServiceFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://172.172.0.20:6888/ormrpc/services/WSGLWebServiceFacade", "WSGLWebServiceFacadeSrvProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://172.172.0.20:6888/ormrpc/services/WSGLWebServiceFacade", "WSGLWebServiceFacade"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSGLWebServiceFacade".equals(portName)) {
            setWSGLWebServiceFacadeEndpointAddress(address);
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
