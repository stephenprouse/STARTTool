package us.prouse.starttool;

import com.blueelm.webservices.OpenGate;
import com.blueelm.webservices.OpenGateSoap;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.Node;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Start7 {

	public static void GetVisits(Boolean debugMode) throws ParserConfigurationException, SAXException, IOException, ServiceException {
		
		// Connection strings
		String password = ConnectionStrings.password;
		String conString = "data source=UPC;user=OpenGate;password="+password+";hcis=UPC.LIVEF;Ring=UPC.LIVEF";
		if (debugMode == true) {
			conString = "data source=UPC;user=OpenGate;password="+password+";hcis=UPC.TEST6.15F;Ring=UPC.TEST6.15F";
		}
		String sql = "SELECT m.AcctNumber AS [Account], v.RegistrationDateTime AS [RegDateTime,, DateTime],  v.DischDateTime AS [DcDateTime,, DateTime], v.VisitType AS [Type] FROM RegAcct.RoomBedX x JOIN RegAcct.Main m JOIN HimRec.VisitData v ON RegAcct.Patient WHERE v.VisitType in ('IN','INO') AND v.DischDateTime is not null";
		String parameters = "";
		String result = "";

		List<String> accountArray = new ArrayList<String>();

		DateTime sixMonthsAgo = DateTime.now().minusDays(180);

                OpenGate service = new com.blueelm.webservices.OpenGateLocator();
                OpenGateSoap port = service.getOpenGateSoap();
                result = port.executeMAT(conString, sql, parameters);

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
						String account = el.getElementsByTagName("Account").item(0).getTextContent();
						String dcDt = el.getElementsByTagName("DcDateTime").item(0).getTextContent().substring(0, 10);

						DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
						DateTime dt = formatter.parseDateTime(dcDt);
						if (dt.isAfter(sixMonthsAgo)) {
							if (!accountArray.contains(account)) {
								accountArray.add(account);
								CreateHL7.CreateHL7Message(debugMode, "B.SIX", "", account, "Y");
							}
						}
					}
				}
			}
		}
	}
}