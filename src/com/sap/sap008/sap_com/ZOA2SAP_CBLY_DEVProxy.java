package com.sap.sap008.sap_com;

public class ZOA2SAP_CBLY_DEVProxy implements com.sap.sap008.sap_com.ZOA2SAP_CBLY_DEV {
  private String _endpoint = null;
  private com.sap.sap008.sap_com.ZOA2SAP_CBLY_DEV zOA2SAP_CBLY_DEV = null;
  
  public ZOA2SAP_CBLY_DEVProxy() {
    _initZOA2SAP_CBLY_DEVProxy();
  }
  
  public ZOA2SAP_CBLY_DEVProxy(String endpoint) {
    _endpoint = endpoint;
    _initZOA2SAP_CBLY_DEVProxy();
  }
  
  private void _initZOA2SAP_CBLY_DEVProxy() {
    try {
      zOA2SAP_CBLY_DEV = (new com.sap.sap008.sap_com.ZSAP_OA_008_ServiceLocator()).getZSAP_OA_008();
      if (zOA2SAP_CBLY_DEV != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)zOA2SAP_CBLY_DEV)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)zOA2SAP_CBLY_DEV)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (zOA2SAP_CBLY_DEV != null)
      ((javax.xml.rpc.Stub)zOA2SAP_CBLY_DEV)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sap.sap008.sap_com.ZOA2SAP_CBLY_DEV getZOA2SAP_CBLY_DEV() {
    if (zOA2SAP_CBLY_DEV == null)
      _initZOA2SAP_CBLY_DEVProxy();
    return zOA2SAP_CBLY_DEV;
  }
  
  public void ZSAP_OA_008(com.sap.sap008.sap_com.holders.TABLE_OF_ZSLYRETURNHolder IT_LY_RETURN, com.sap.sap008.sap_com.holders.TABLE_OF_ZSINTTURNHolder IT_NECETR) throws java.rmi.RemoteException{
    if (zOA2SAP_CBLY_DEV == null)
      _initZOA2SAP_CBLY_DEVProxy();
    zOA2SAP_CBLY_DEV.ZSAP_OA_008(IT_LY_RETURN, IT_NECETR);
  }
  
  
}