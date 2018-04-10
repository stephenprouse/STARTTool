package us.prouse.starttool;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.xml.sax.SAXException;


public class start {
	
	/**
	 * 
	 * @param debugMode either true of false will select the output path for the HL7 files either in
	 * TEST or LIVE along with setting the data source string for either TEST or LIVE Meditech
	 * @throws SAXException XML parser
	 * @throws IOException IO Exception
	 * @throws ParserConfigurationException XML Parser
	 * @throws ServiceException 
	 */
	
	public void StartStart1(Boolean debugMode) {
		try {
			Start1.GetMeds(debugMode);
		}
		catch (Exception ex) {
			
		}
	}
	
	public void StartStart3(Boolean debugMode) throws SAXException, IOException, ParserConfigurationException, ServiceException {
		Start3.GetProblems(debugMode);
	}
	
	public void StartStart7(Boolean debugMode) throws SAXException, IOException, ParserConfigurationException, ServiceException {
		Start7.GetVisits(debugMode);
	}
}
