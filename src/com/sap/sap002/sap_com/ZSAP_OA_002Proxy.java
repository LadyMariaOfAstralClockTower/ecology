package com.sap.sap002.sap_com;

public class ZSAP_OA_002Proxy implements com.sap.sap002.sap_com.ZSAP_OA_002_PortType {
  private String _endpoint = null;
  private com.sap.sap002.sap_com.ZSAP_OA_002_PortType zSAP_OA_002_PortType = null;
  
  public ZSAP_OA_002Proxy() {
    _initZSAP_OA_002Proxy();
  }
  
  public ZSAP_OA_002Proxy(String endpoint) {
    _endpoint = endpoint;
    _initZSAP_OA_002Proxy();
  }
  
  private void _initZSAP_OA_002Proxy() {
    try {
      zSAP_OA_002_PortType = (new com.sap.sap002.sap_com.ZSAP_OA_002_ServiceLocator()).getOA_002();
      if (zSAP_OA_002_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)zSAP_OA_002_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)zSAP_OA_002_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (zSAP_OA_002_PortType != null)
      ((javax.xml.rpc.Stub)zSAP_OA_002_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sap.sap002.sap_com.ZSAP_OA_002_PortType getZSAP_OA_002_PortType() {
    if (zSAP_OA_002_PortType == null)
      _initZSAP_OA_002Proxy();
    return zSAP_OA_002_PortType;
  }
  
  public void ZSAP_OA_002(com.sap.sap002.sap_com.holders.TABLE_OF_ZSINTTURNHolder IT_NECETR, com.sap.sap002.sap_com.holders.TABLE_OF_ZSPRETURNHolder IT_PRETURN) throws java.rmi.RemoteException{
    if (zSAP_OA_002_PortType == null)
      _initZSAP_OA_002Proxy();
    zSAP_OA_002_PortType.ZSAP_OA_002(IT_NECETR, IT_PRETURN);
  }
  
  
}