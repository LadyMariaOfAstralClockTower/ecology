package com.lw.webservice;

public class CAdxWebServiceXmlCCProxy implements com.lw.webservice.CAdxWebServiceXmlCC {
  private String _endpoint = null;
  private com.lw.webservice.CAdxWebServiceXmlCC cAdxWebServiceXmlCC = null;
  
  public CAdxWebServiceXmlCCProxy() {
    _initCAdxWebServiceXmlCCProxy();
  }
  
  public CAdxWebServiceXmlCCProxy(String endpoint) {
    _endpoint = endpoint;
    _initCAdxWebServiceXmlCCProxy();
  }
  
  private void _initCAdxWebServiceXmlCCProxy() {
    try {
      cAdxWebServiceXmlCC = (new com.lw.webservice.CAdxWebServiceXmlCCServiceLocator()).getCAdxWebServiceXmlCC();
      if (cAdxWebServiceXmlCC != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cAdxWebServiceXmlCC)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cAdxWebServiceXmlCC)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cAdxWebServiceXmlCC != null)
      ((javax.xml.rpc.Stub)cAdxWebServiceXmlCC)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.lw.webservice.CAdxWebServiceXmlCC getCAdxWebServiceXmlCC() {
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC;
  }
  
  public com.lw.webservice.CAdxResultXml run(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String inputXml) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.run(callContext, publicName, inputXml);
  }
  
  public com.lw.webservice.CAdxResultXml save(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String objectXml) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.save(callContext, publicName, objectXml);
  }
  
  public com.lw.webservice.CAdxResultXml delete(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.delete(callContext, publicName, objectKeys);
  }
  
  public com.lw.webservice.CAdxResultXml read(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.read(callContext, publicName, objectKeys);
  }
  
  public com.lw.webservice.CAdxResultXml query(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, int listSize) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.query(callContext, publicName, objectKeys, listSize);
  }
  
  public com.lw.webservice.CAdxResultXml getDescription(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.getDescription(callContext, publicName);
  }
  
  public com.lw.webservice.CAdxResultXml insertLines(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, java.lang.String blocKey, java.lang.String lineKey, java.lang.String lineXml) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.insertLines(callContext, publicName, objectKeys, blocKey, lineKey, lineXml);
  }
  
  public com.lw.webservice.CAdxResultXml deleteLines(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, java.lang.String blocKey, java.lang.String[] lineKeys) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.deleteLines(callContext, publicName, objectKeys, blocKey, lineKeys);
  }
  
  public com.lw.webservice.CAdxResultXml getDataXmlSchema(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.getDataXmlSchema(callContext, publicName);
  }
  
  public com.lw.webservice.CAdxResultXml modify(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, com.lw.webservice.CAdxParamKeyValue[] objectKeys, java.lang.String objectXml) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.modify(callContext, publicName, objectKeys, objectXml);
  }
  
  public com.lw.webservice.CAdxResultXml actionObjectKeys(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String actionCode, com.lw.webservice.CAdxParamKeyValue[] objectKeys) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.actionObjectKeys(callContext, publicName, actionCode, objectKeys);
  }
  
  public com.lw.webservice.CAdxResultXml actionObject(com.lw.webservice.CAdxCallContext callContext, java.lang.String publicName, java.lang.String actionCode, java.lang.String objectXml) throws java.rmi.RemoteException{
    if (cAdxWebServiceXmlCC == null)
      _initCAdxWebServiceXmlCCProxy();
    return cAdxWebServiceXmlCC.actionObject(callContext, publicName, actionCode, objectXml);
  }
  
  
}