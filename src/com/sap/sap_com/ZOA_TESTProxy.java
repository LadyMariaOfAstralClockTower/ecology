package com.sap.sap_com;

public class ZOA_TESTProxy implements com.sap.sap_com.ZOA_TEST_PortType {
  private String _endpoint = null;
  private com.sap.sap_com.ZOA_TEST_PortType zOA_TEST_PortType = null;
  
  public ZOA_TESTProxy() {
    _initZOA_TESTProxy();
  }
  
  public ZOA_TESTProxy(String endpoint) {
    _endpoint = endpoint;
    _initZOA_TESTProxy();
  }
  
  private void _initZOA_TESTProxy() {
    try {
      zOA_TEST_PortType = (new com.sap.sap_com.ZOA_TEST_ServiceLocator()).getZOA_TEST();
      if (zOA_TEST_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)zOA_TEST_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)zOA_TEST_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (zOA_TEST_PortType != null)
      ((javax.xml.rpc.Stub)zOA_TEST_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sap.sap_com.ZOA_TEST_PortType getZOA_TEST_PortType() {
    if (zOA_TEST_PortType == null)
      _initZOA_TESTProxy();
    return zOA_TEST_PortType;
  }
  
  public java.lang.String ZTEST_001(com.sap.sap_com.holders.TABLE_OF_MAKTHolder IT_ITEM, java.lang.String IV_STR) throws java.rmi.RemoteException{
    if (zOA_TEST_PortType == null)
      _initZOA_TESTProxy();
    return zOA_TEST_PortType.ZTEST_001(IT_ITEM, IV_STR);
  }
  
  
}