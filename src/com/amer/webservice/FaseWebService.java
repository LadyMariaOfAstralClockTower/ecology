/**
 * FaseWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.amer.webservice;

public interface FaseWebService extends java.rmi.Remote {
    public java.lang.String testUserStatus(java.lang.String userCode, java.lang.String passWord) throws java.rmi.RemoteException;
    public com.amer.webservice.WebStoreModel getLocationData(java.lang.String userCode, java.lang.String passWord, java.lang.String startDate, java.lang.String endDate) throws java.rmi.RemoteException;
    public com.amer.webservice.WebProductModel getProductData(java.lang.String userCode, java.lang.String passWord, java.lang.String brand, java.lang.String startDate, java.lang.String endDate) throws java.rmi.RemoteException;
    public java.lang.String connectionStatus() throws java.rmi.RemoteException;
    public com.amer.webservice.WebBrandModel getBrandData(java.lang.String userCode, java.lang.String passWord) throws java.rmi.RemoteException;
}
