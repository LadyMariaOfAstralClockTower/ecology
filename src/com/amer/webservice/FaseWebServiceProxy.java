package com.amer.webservice;

public class FaseWebServiceProxy implements com.amer.webservice.FaseWebService {
  private String _endpoint = null;
  private com.amer.webservice.FaseWebService faseWebService = null;
  
  public FaseWebServiceProxy() {
    _initFaseWebServiceProxy();
  }
  
  public FaseWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initFaseWebServiceProxy();
  }
  
  private void _initFaseWebServiceProxy() {
    try {
      faseWebService = (new com.amer.webservice.FaseWebServiceImplServiceLocator()).getFaseWebServiceImplPort();
      if (faseWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)faseWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)faseWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (faseWebService != null)
      ((javax.xml.rpc.Stub)faseWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.amer.webservice.FaseWebService getFaseWebService() {
    if (faseWebService == null)
      _initFaseWebServiceProxy();
    return faseWebService;
  }
  
  public java.lang.String testUserStatus(java.lang.String userCode, java.lang.String passWord) throws java.rmi.RemoteException{
    if (faseWebService == null)
      _initFaseWebServiceProxy();
    return faseWebService.testUserStatus(userCode, passWord);
  }
  
  public com.amer.webservice.WebStoreModel getLocationData(java.lang.String userCode, java.lang.String passWord, java.lang.String startDate, java.lang.String endDate) throws java.rmi.RemoteException{
    if (faseWebService == null)
      _initFaseWebServiceProxy();
    return faseWebService.getLocationData(userCode, passWord, startDate, endDate);
  }
  
  public com.amer.webservice.WebProductModel getProductData(java.lang.String userCode, java.lang.String passWord, java.lang.String brand, java.lang.String startDate, java.lang.String endDate) throws java.rmi.RemoteException{
    if (faseWebService == null)
      _initFaseWebServiceProxy();
    return faseWebService.getProductData(userCode, passWord, brand, startDate, endDate);
  }
  
  public java.lang.String connectionStatus() throws java.rmi.RemoteException{
    if (faseWebService == null)
      _initFaseWebServiceProxy();
    return faseWebService.connectionStatus();
  }
  
  public com.amer.webservice.WebBrandModel getBrandData(java.lang.String userCode, java.lang.String passWord) throws java.rmi.RemoteException{
    if (faseWebService == null)
      _initFaseWebServiceProxy();
    return faseWebService.getBrandData(userCode, passWord);
  }
  
  
}