package us.prouse.starttool;

import com.blueelm.webservices.OpenGate;
import com.blueelm.webservices.OpenGateSoap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
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

public class Start1 {
	
	/*
	 * Start1 gets the current inpatient census and builds a table of all
	 * their medications. The program then loops through each medication
	 * looking for patients on particular high risk medications as provided
	 * on the text document that is loaded into the Dictionary.
	 * 
	 * Once high risk medications were identified the program loops through
	 * all medications and counts them for polypharmacy. If medications
	 * are 10 or more the patietn is considered for polypharmacy.
	 */

	public static void GetMeds(Boolean debugMode) throws SAXException, IOException, ParserConfigurationException, ServiceException {
		
		// value for password stored elsewhere
		String password = ConnectionStrings.password;
		String conString = "data source=UPC;user=OpenGate;password="+password+";hcis=UPC.LIVEN;database=PHA.UCH";
		if (debugMode == true) {
			conString = "data source=UPC;user=OpenGate;password="+password+";hcis=UPC.TEST6.15N;database=PHA.UCH";
		}

		String sql = "SELECT " + "ADM.PAT.acct.number, PHA.RX.med, "
				+ "{@PHA.RX.med's.generic^PHA.GENERIC.mnemonic} AS [generic], "
				+ "PHA.RX.med's.generic's.name as [generic_name], "
				+ "{+PHA.GENERIC.drug.classes[PHA.GENERIC.mnemonic,\"\"]^PHA.CLASS.mnemonic} as [class], "
				+ "PHA.CLASS.name as [class_name], PHA.RX.order.type, PHA.RX.schedule, PHA.RX.dose.and.unit, PHA.RX.route "
				+ "FROM ADM.PAT.room.bed.index "
				+ "JOIN ADM.PAT.main JOIN PHA.PAT.main JOIN PHA.RX.pat.tr.x JOIN PHA.RX.main "
				+ "WHERE PHA.RX.status = \"AC\" AND PHA.RX.schedule = \"SCH\" AND PHA.RX.route <> \"XX\" AND PHA.RX.med <> \"FLUSH\"";
		String parameters = "";
		String result = "";

		// using to evaluate if Generic is HEPARIN and route of SQ
		Boolean heparinSubQ;

		// using to evaluate if Generic is ENOXAP and dose is 40 mg or 30 mg
		Boolean enoxaparin;

		// using to evaluate if Generic is ASPIRIN
		Boolean aspirin;

		String specialMeds = "68:20.08,20:12.04,24:04.08,28:08.08,20:12.18";
		/*
		 * Insulin = 68:20.08 Anticoagulants = 20:12.04 Digoxin = 24:04.08
		 * Narcotics = 28:08.08 Platelet-Aggregation Inhibitor = 20:12.18
		 */

		/*
		 * instead of just using a File Reader I'm using an InputStream so
		 * that I can pull the file from the jar. Otherwise every the files
		 * to be read in need to be in the runtime path which is a maintenance
		 * issue for Mirth
		 */
        
		// Create hash map for holding drugs to be exclude from total
		InputStream stream = Start1.class.getResourceAsStream("ExcludedDrugs.txt");
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		while ((line = reader.readLine()) != null) {
			String parts[] = line.split("\\|");
			map.put(parts[0], parts[1]);
		}
		reader.close();

		// Create the Special Medications table
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("accountNumber");
		model.addColumn("med");
		model.addColumn("class");
		model.addColumn("generic");
		model.addColumn("dose");
		model.addColumn("route");

		/*
		// Make sure to dispose at the end and comment out for headless running
		JTable dtSpecMeds = new JTable(model);

		JFrame f = new JFrame();
		f.setSize(300, 300);
		f.add(new JScrollPane(dtSpecMeds));
		f.setVisible(true);
		*/

		// Array for holding special med accounts
		List<String> accountArray = new ArrayList<String>();

		// Array for holding all accounts to later count totals per account
		List<String> accountArrayAll = new ArrayList<String>();
                
                OpenGate service = new com.blueelm.webservices.OpenGateLocator();
                OpenGateSoap port = service.getOpenGateSoap();
                result = port.executeCS(conString, sql, parameters);


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
						if (el.getElementsByTagName("class").item(0) != null) {

							String dose = "";
							String drugClass = ((drugClass = el.getElementsByTagName("class").item(0)
									.getTextContent()) != null) ? drugClass : "NULL";
							String med = ((med = el.getElementsByTagName("PHA.RX.med").item(0)
									.getTextContent()) != null) ? med : "NULL";
							String generic = ((generic = el.getElementsByTagName("generic").item(0)
									.getTextContent()) != null) ? generic : "NULL";
							String route = ((route = el.getElementsByTagName("PHA.RX.route").item(0)
									.getTextContent()) != null) ? route : "NULL";
							if (el.getElementsByTagName("PHA.RX.dose.and.unit").item(0) != null) {
								dose = ((dose = el.getElementsByTagName("PHA.RX.dose.and.unit").item(0)
										.getTextContent()) != null) ? dose : "NULL";
							}
							String accountNumber = el.getElementsByTagName("ADM.PAT.acct.number").item(0)
									.getTextContent();

							// Check if the current med is to be excluded
							heparinSubQ = generic.contains("HEPARI") && route.contains("SQ");
							enoxaparin = generic.contains("ENOXAP") && (dose.contains("30") || dose.contains("40"));
							aspirin = generic.contains("ASPIR");

							// Count of all meds
							if (!map.containsKey(med)) {
								accountArrayAll.add(accountNumber);
							}

							// Special Medications Evaluation
							// 1st look to see if med in on excluded list
							if (specialMeds.contains(drugClass) && map.containsKey(med) == false) {
								// 2nd check
								if (heparinSubQ == false && enoxaparin == false && aspirin == false) {
									// 3rd check to see if account not already
									// added
									if (!accountArray.contains(accountNumber)) {
										accountArray.add(accountNumber);
										model.addRow(
												new Object[] { accountNumber, med, drugClass, generic, dose, route });
										CreateHL7.CreateHL7Message(debugMode, "B.HRM", "", accountNumber, "Y");
									}
								}
							}
						}
					}
				}
			}
		}

		// Sort Array, count all unique values.
		Collections.sort(accountArrayAll);
		String currAccount = "";
		String prevAccount = "";
		for (int i = 0; i < accountArrayAll.size(); i++) {
			currAccount = accountArrayAll.get(i);
			int medCount = 0;
			for (Iterator<String> j = accountArrayAll.iterator(); j.hasNext();) {
				String account = j.next();
				if (account.equals(currAccount)) {
					medCount++;
				}
			}
			if (medCount >= 10 && !currAccount.equals(prevAccount)) {
				CreateHL7.CreateHL7Message(debugMode, "B.PM", "", currAccount, "Y");
			}
			prevAccount = currAccount;
		}
		
		// Close table window
		//f.setVisible(false);
		//f.dispose();
	}
}
