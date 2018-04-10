/**
 * OpenGateSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.blueelm.webservices;

public interface OpenGateSoap extends java.rmi.Remote {
    public java.lang.String executeCS(java.lang.String conString, java.lang.String sql, java.lang.String parameters) throws java.rmi.RemoteException;
    public java.lang.String executeMAGIC(java.lang.String conString, java.lang.String sql, java.lang.String parameters) throws java.rmi.RemoteException;
    public java.lang.String executeMAT(java.lang.String conString, java.lang.String sql, java.lang.String parameters) throws java.rmi.RemoteException;
    public java.lang.String listConnectionPools(com.blueelm.webservices.Platform platform) throws java.rmi.RemoteException;
}
