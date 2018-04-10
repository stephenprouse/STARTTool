package us.prouse.starttool;

//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jcifs.smb.SmbFileOutputStream;

public class CreateHL7 {
	public static void CreateHL7Message(Boolean debugMode, String query, String mrn, String account, String yesNo)
			throws IOException {
		
		/**
		 * 
		 */

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String dt = dateFormat.format(date);
		String outMessage = "";
		String msh = "MSH|^~\\&|START|START|||" + dt + "||ORU^R01|" + dt + "|P|2.5\n";
		String pid = "PID|1||" + mrn + "|||||||||||||||" + account + "||\n";
		String obr = "OBR|||||||" + dt + "\n";
		String obx = "OBX|1|NM|" + query + "||" + yesNo + "||||||F";
		outMessage = outMessage + msh + pid + obr + obx;

		/*
		 * // Create files instead of using file share try { File file = new
		 * File("START_" + query + "_" + mrn + "_" + account + "-" + dt +
		 * ".HL7"); FileWriter fileWriter = new FileWriter(file);
		 * fileWriter.write(outMessage); fileWriter.flush(); fileWriter.close();
		 * } catch (IOException e) { e.printStackTrace(); }
		 */

		String smbServer = "C-3PO";
		String smbShare = "data/PCS_inbox_LIVE";
		if (debugMode == true) {
			smbShare = "data/PCS_inbox_TEST";
		}
		String smbUser = ConnectionStrings.mirthUser;
		String smbPassword = ConnectionStrings.mirthPwd;
		String fileName = "START_" + query + "_" + mrn + "_" + account + "-" + dt + ".HL7";

		// https://jcifs.samba.org/src/docs/api/
		SmbFileOutputStream fileConn = new SmbFileOutputStream(
				"smb://" + smbUser + ":" + smbPassword + "@" + smbServer + "/" + smbShare + "/" + fileName);
		fileConn.write(outMessage.getBytes());
		fileConn.close();
	}
}