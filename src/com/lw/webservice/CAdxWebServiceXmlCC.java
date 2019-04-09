/**
 * CAdxWebServiceXmlCC.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lw.webservice;

public interface CAdxWebServiceXmlCC extends java.rmi.Remote {
    public com.lw.webservice.CAdxResultXml run(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String inputXml) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml save(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String objectXml) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml delete(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml read(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml query(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, int listSize) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml getDescription(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml insertLines(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, java.lang.String blocKey, java.lang.String lineKey, java.lang.String lineXml) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml deleteLines(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, java.lang.String blocKey, java.lang.String[] lineKeys) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml getDataXmlSchema(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml modify(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, java.lang.String objectXml) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml actionObjectKeys(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String actionCode, com.lw.webservice.CAdxParamKeyValue[] objectKeys) throws java.rmi.RemoteException;
    public com.lw.webservice.CAdxResultXml actionObject(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String actionCode, java.lang.String objectXml) throws java.rmi.RemoteException;
}
