/**
 * OpenGate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.blueelm.webservices;

public interface OpenGate extends javax.xml.rpc.Service {
    public java.lang.String getOpenGateSoapAddress();

    public com.blueelm.webservices.OpenGateSoap getOpenGateSoap() throws javax.xml.rpc.ServiceException;

    public com.blueelm.webservices.OpenGateSoap getOpenGateSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
