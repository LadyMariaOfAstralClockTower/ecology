/**
 * WSGLWebServiceFacadeSrvProxy.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jzx.EASServices.WSGLWebServiceFacade;

public interface WSGLWebServiceFacadeSrvProxy extends java.rmi.Remote {
    public java.lang.String[][] getOrg(java.lang.String number) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public int deleteVoucher(java.lang.String companyNumber, java.lang.String period, java.lang.String voucherNumber, java.lang.String fexp) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[][] getAsstActType(java.lang.String orgNumber, java.lang.String acctTypeNum) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[][] getVoucher(java.lang.String orgNumber, java.lang.String year, java.lang.String period, int fromRow, int toRow) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[][] getAccount(java.lang.String orgNumber, int fromRow, int toRow) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public int deleteVoucherByID(java.lang.String companyNumber, java.lang.String voucherID) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String importAccountBalance(com.jzx.EASServices.WSGLWebServiceFacade.WSWSAccountBalance[] accountBalanceCols) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String exploreVoucher(java.lang.String companyId, int year, int periodNumber, java.lang.String voucherType, java.lang.String number) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String importInitAccountBalance(com.jzx.EASServices.WSGLWebServiceFacade.WSWSAccountBalance[] initAccountBalanceCol) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String checkVoucher(java.lang.String comNumber, int year, int periodNumber, java.lang.String number, double amount) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String importAssistBalance(com.jzx.EASServices.WSGLWebServiceFacade.WSWSAssistBalance[] assistBalanceCol) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[] findVoucher(java.lang.String comNumber, int year, int periodNnumber) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public void importInitAssistBalance(com.jzx.EASServices.WSGLWebServiceFacade.WSWSAssistBalance[] initAssistBalanceBalance) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public boolean deleteBalance(java.lang.String companyNumber, int year, int period) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[] importVoucher(com.jzx.EASServices.WSGLWebServiceFacade.wsvoucher.WSWSVoucher[] col, int isSubmit, int isVerify, int isCashflow) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[] importVoucherOfReturnID(com.jzx.EASServices.WSGLWebServiceFacade.wsvoucher.WSWSVoucher[] col, int isSubmit, int isVerify, int isCashflow) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[][] getAcctType(java.lang.String orgNumber) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[][] getAcctTypeDetail(java.lang.String orgNumber, java.lang.String asstActTypeNum) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[][] getAccountBalance(java.lang.String orgNumber, java.lang.String year, java.lang.String period, int fromRow, int toRow) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
    public java.lang.String[][] getAssitBalance(java.lang.String orgnumber, java.lang.String accountNumber, java.lang.String year, java.lang.String period, int fromRow, int toRow) throws java.rmi.RemoteException, com.jzx.EASServices.WSGLWebServiceFacade.client.WSInvokeException;
}
