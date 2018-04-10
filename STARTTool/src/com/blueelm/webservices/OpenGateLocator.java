/**
 * OpenGateLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.blueelm.webservices;

public class OpenGateLocator extends org.apache.axis.client.Service implements com.blueelm.webservices.OpenGate {

    public OpenGateLocator() {
    }


    public OpenGateLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OpenGateLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OpenGateSoap
    private java.lang.String OpenGateSoap_address = "https://r2-d2.uchs.org/OpenGateWeb/OpenGate.asmx";

    public java.lang.String getOpenGateSoapAddress() {
        return OpenGateSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OpenGateSoapWSDDServiceName = "OpenGateSoap";

    public java.lang.String getOpenGateSoapWSDDServiceName() {
        return OpenGateSoapWSDDServiceName;
    }

    public void setOpenGateSoapWSDDServiceName(java.lang.String name) {
        OpenGateSoapWSDDServiceName = name;
    }

    public com.blueelm.webservices.OpenGateSoap getOpenGateSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OpenGateSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOpenGateSoap(endpoint);
    }

    public com.blueelm.webservices.OpenGateSoap getOpenGateSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.blueelm.webservices.OpenGateSoapStub _stub = new com.blueelm.webservices.OpenGateSoapStub(portAddress, this);
            _stub.setPortName(getOpenGateSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOpenGateSoapEndpointAddress(java.lang.String address) {
        OpenGateSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.blueelm.webservices.OpenGateSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.blueelm.webservices.OpenGateSoapStub _stub = new com.blueelm.webservices.OpenGateSoapStub(new java.net.URL(OpenGateSoap_address), this);
                _stub.setPortName(getOpenGateSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("OpenGateSoap".equals(inputPortName)) {
            return getOpenGateSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://blueelm.com/webservices/", "OpenGate");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://blueelm.com/webservices/", "OpenGateSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OpenGateSoap".equals(portName)) {
            setOpenGateSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
