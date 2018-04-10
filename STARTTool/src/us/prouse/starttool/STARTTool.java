/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.prouse.starttool;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.xml.sax.SAXException;

/**
 *
 * @author SProuse
 */
public class STARTTool {

    /**
     * @param args the command line arguments
     * @throws ServiceException 
     */
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ServiceException {
        // TODO code application logic here
        Boolean debugMode = true;
        Start1.GetMeds(debugMode);
        Start3.GetProblems(debugMode);
        Start7.GetVisits(debugMode);
    }
}