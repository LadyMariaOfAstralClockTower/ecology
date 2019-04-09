package com.sap.sap006.sap_com;

public class ZOA2SAP_YFKAPOV_DEVProxy implements com.sap.sap006.sap_com.ZOA2SAP_YFKAPOV_DEV_PortType {
  private String _endpoint = null;
  private com.sap.sap006.sap_com.ZOA2SAP_YFKAPOV_DEV_PortType zOA2SAP_YFKAPOV_DEV_PortType = null;
  
  public ZOA2SAP_YFKAPOV_DEVProxy() {
    _initZOA2SAP_YFKAPOV_DEVProxy();
  }
  
  public ZOA2SAP_YFKAPOV_DEVProxy(String endpoint) {
    _endpoint = endpoint;
    _initZOA2SAP_YFKAPOV_DEVProxy();
  }
  
  private void _initZOA2SAP_YFKAPOV_DEVProxy() {
    try {
      zOA2SAP_YFKAPOV_DEV_PortType = (new com.sap.sap006.sap_com.ZOA2SAP_YFKAPOV_DEV_ServiceLocator()).getZSAP_OA_006();
      if (zOA2SAP_YFKAPOV_DEV_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)zOA2SAP_YFKAPOV_DEV_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)zOA2SAP_YFKAPOV_DEV_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (zOA2SAP_YFKAPOV_DEV_PortType != null)
      ((javax.xml.rpc.Stub)zOA2SAP_YFKAPOV_DEV_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sap.sap006.sap_com.ZOA2SAP_YFKAPOV_DEV_PortType getZOA2SAP_YFKAPOV_DEV_PortType() {
    if (zOA2SAP_YFKAPOV_DEV_PortType == null)
      _initZOA2SAP_YFKAPOV_DEVProxy();
    return zOA2SAP_YFKAPOV_DEV_PortType;
  }
  
  public void ZSAP_OA_006(com.sap.sap006.sap_com.holders.TABLE_OF_ZSINTTURNHolder IT_NECETR, com.sap.sap006.sap_com.holders.TABLE_OF_ZSYFKRETURNHolder IT_YFK_RETURN) throws java.rmi.RemoteException{
    if (zOA2SAP_YFKAPOV_DEV_PortType == null)
      _initZOA2SAP_YFKAPOV_DEVProxy();
    zOA2SAP_YFKAPOV_DEV_PortType.ZSAP_OA_006(IT_NECETR, IT_YFK_RETURN);
  }
  
  
}