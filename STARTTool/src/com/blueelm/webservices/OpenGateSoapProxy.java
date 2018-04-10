package com.blueelm.webservices;

public class OpenGateSoapProxy implements com.blueelm.webservices.OpenGateSoap {
  private String _endpoint = null;
  private com.blueelm.webservices.OpenGateSoap openGateSoap = null;
  
  public OpenGateSoapProxy() {
    _initOpenGateSoapProxy();
  }
  
  public OpenGateSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initOpenGateSoapProxy();
  }
  
  private void _initOpenGateSoapProxy() {
    try {
      openGateSoap = (new com.blueelm.webservices.OpenGateLocator()).getOpenGateSoap();
      if (openGateSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)openGateSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)openGateSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (openGateSoap != null)
      ((javax.xml.rpc.Stub)openGateSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.blueelm.webservices.OpenGateSoap getOpenGateSoap() {
    if (openGateSoap == null)
      _initOpenGateSoapProxy();
    return openGateSoap;
  }
  
  public java.lang.String executeCS(java.lang.String conString, java.lang.String sql, java.lang.String parameters) throws java.rmi.RemoteException{
    if (openGateSoap == null)
      _initOpenGateSoapProxy();
    return openGateSoap.executeCS(conString, sql, parameters);
  }
  
  public java.lang.String executeMAGIC(java.lang.String conString, java.lang.String sql, java.lang.String parameters) throws java.rmi.RemoteException{
    if (openGateSoap == null)
      _initOpenGateSoapProxy();
    return openGateSoap.executeMAGIC(conString, sql, parameters);
  }
  
  public java.lang.String executeMAT(java.lang.String conString, java.lang.String sql, java.lang.String parameters) throws java.rmi.RemoteException{
    if (openGateSoap == null)
      _initOpenGateSoapProxy();
    return openGateSoap.executeMAT(conString, sql, parameters);
  }
  
  public java.lang.String listConnectionPools(com.blueelm.webservices.Platform platform) throws java.rmi.RemoteException{
    if (openGateSoap == null)
      _initOpenGateSoapProxy();
    return openGateSoap.listConnectionPools(platform);
  }
  
  
}