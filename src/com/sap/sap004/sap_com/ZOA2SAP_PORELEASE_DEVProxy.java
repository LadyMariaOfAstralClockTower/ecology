package com.sap.sap004.sap_com;

public class ZOA2SAP_PORELEASE_DEVProxy implements com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_PortType {
  private String _endpoint = null;
  private com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_PortType zOA2SAP_PORELEASE_DEV_PortType = null;
  
  public ZOA2SAP_PORELEASE_DEVProxy() {
    _initZOA2SAP_PORELEASE_DEVProxy();
  }
  
  public ZOA2SAP_PORELEASE_DEVProxy(String endpoint) {
    _endpoint = endpoint;
    _initZOA2SAP_PORELEASE_DEVProxy();
  }
  
  private void _initZOA2SAP_PORELEASE_DEVProxy() {
    try {
      zOA2SAP_PORELEASE_DEV_PortType = (new com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_ServiceLocator()).getZSAP_OA_004();
      if (zOA2SAP_PORELEASE_DEV_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)zOA2SAP_PORELEASE_DEV_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)zOA2SAP_PORELEASE_DEV_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (zOA2SAP_PORELEASE_DEV_PortType != null)
      ((javax.xml.rpc.Stub)zOA2SAP_PORELEASE_DEV_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_PortType getZOA2SAP_PORELEASE_DEV_PortType() {
    if (zOA2SAP_PORELEASE_DEV_PortType == null)
      _initZOA2SAP_PORELEASE_DEVProxy();
    return zOA2SAP_PORELEASE_DEV_PortType;
  }
  
  public void ZSAP_OA_004(com.sap.sap004.sap_com.holders.TABLE_OF_ZSINTTURNHolder IT_NECETR, com.sap.sap004.sap_com.holders.TABLE_OF_ZSPOAPRVHolder IT_POAPRV) throws java.rmi.RemoteException{
    if (zOA2SAP_PORELEASE_DEV_PortType == null)
      _initZOA2SAP_PORELEASE_DEVProxy();
    zOA2SAP_PORELEASE_DEV_PortType.ZSAP_OA_004(IT_NECETR, IT_POAPRV);
  }
  
  
}