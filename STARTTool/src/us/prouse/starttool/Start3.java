package us.prouse.starttool;

import com.blueelm.webservices.OpenGate;
import com.blueelm.webservices.OpenGateSoap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class Start3 {

	public static void GetProblems(Boolean debugMode) throws IOException, ParserConfigurationException, SAXException, ServiceException {
		
		String password = ConnectionStrings.password;
		String conString = "data source=UPC;user=OpenGate;password="+password+";ring=UPC.LIVEF;hcis=UPC.LIVEF";
		if (debugMode == true) {
			conString = "data source=UPC;user=OpenGate;password="+password+";ring=UPC.TEST6.15F;hcis=UPC.TEST6.15F";
		}
		String sql = "SELECT m.AcctNumber AS [Account], p.ProblemDictOid AS [Problem], p.ProblemStatus AS [Status], p.ProblemSelectedDescription AS [Description], c.AssocCode AS [Code], c.AssocCodeType as [Type] FROM RegAcct.RoomBedX x JOIN RegAcct.Main m JOIN EmrPat.Problems p ON RegAcct.Patient JOIN MisPatProblem.Main on p.ProblemDictOid JOIN MisPatProblem.AssociatedCodes c WHERE p.ProblemStatus not IN ('Inactive','Resolved','Deleted','Ruled-out') and c.AssocCodeType <> 'SNOMED'";
		String parameters = "";
		String fileName = "ProblemList.txt";
		String result = "";
		List<String> accountArray = new ArrayList<String>();

		// Create hash map for problems of interest
		// use to get from the src and not need in the runtime path
		InputStream stream = Start3.class.getResourceAsStream(fileName);
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		while ((line = reader.readLine()) != null) {
			// http://stackoverflow.com/questions/21524642/splitting-string-with-pipe-character
			String parts[] = line.split("\\|");
			map.put(parts[0], parts[1]);
		}
		reader.close();

                OpenGate service = new com.blueelm.webservices.OpenGateLocator();
                OpenGateSoap port = service.getOpenGateSoap();
                result = port.executeMAT(conString, sql, parameters);

		// http://stackoverflow.com/questions/562160/in-java-how-do-i-parse-xml-as-a-string-instead-of-a-file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(result));
		Document dom = builder.parse(is);

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getChildNodes();

		if (nl != null) {
			int length = nl.getLength();
			for (int i = 0; i < length; i++) {
				if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) nl.item(i);
					if (el.getNodeName().contains("Table0")) {
						if (el.getElementsByTagName("Account").item(0) != null) {
							String accountNumber = el.getElementsByTagName("Account").item(0).getTextContent();
							String problem = el.getElementsByTagName("Problem").item(0).getTextContent();

							if (map.containsKey(problem)) {
								if (accountArray.contains(accountNumber) == false) {
									accountArray.add(accountNumber);
									CreateHL7.CreateHL7Message(debugMode, "B.DX", "", accountNumber, "Y");
								}
							}
						}
					}
				}
			}
		}
	}
}
