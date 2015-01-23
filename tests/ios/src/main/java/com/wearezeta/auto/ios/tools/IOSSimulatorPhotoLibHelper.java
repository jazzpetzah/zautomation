package com.wearezeta.auto.ios.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IOSSimulatorPhotoLibHelper {
	private static final String IOS_LIB_PATH_TEMPLATE8 = System
			.getProperty("user.home")
			+ "/Library/Developer/CoreSimulator/Devices/";

	private static final String IOS_LIB_PATH_TEMPLATE7 = System
			.getProperty("user.home")
			+ "/Library/Application Support/iPhone Simulator/%s";

	private static final String MEDIA_PATH_TEMPLATE = "%s/Media";
	public static final String PHOTO_DATA_PATH_TEMPLATE = "%s/PhotoData";
	public static final String PHOTOS_ROOT_TEMPLATE = "%s/DCIM/100APPLE";
	public static final String SIMCTL = "/Applications/Xcode.app/Contents/Developer/usr/bin/simctl";

	private static final Logger log = ZetaLogger
			.getLog(IOSSimulatorPhotoLibHelper.class.getSimpleName());

	public IOSSimulatorPhotoLibHelper() {
		// TODO Auto-generated constructor stub
	}

	private static String GetiOSLibPath(String simulatorVersion)
			throws Exception {
		String result = "";
		if (simulatorVersion.equals("8.0") || simulatorVersion.equals("8.1")) {
			result = IOS_LIB_PATH_TEMPLATE8;
		} else {
			result = String.format(IOS_LIB_PATH_TEMPLATE7, simulatorVersion);
		}
		return result;
	}

	private static String GetMediaPath(String libPath) {
		return String.format(MEDIA_PATH_TEMPLATE, libPath);
	}

	private static boolean compareDeviceNameFromPlist(String filePath,
			String deviceName) throws SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(filePath);

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("string");

		boolean flag = false;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				// get the employee element
				Element el = (Element) nl.item(i);

				if (el.getTextContent().equals(deviceName)) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	private static String FindSimultorFolder(String simulatorVersion)
			throws Exception {
		String deviceName = CommonUtils.getDeviceName(CommonUtils.class);
		String libPath = GetiOSLibPath(simulatorVersion);

		if (!new File(libPath).exists()) {
			throw new Exception(String.format(
					"IOS simulator v. %s has not been found on this system",
					simulatorVersion));
		}
		String result = "";

		File photosRootObj = new File(libPath);
		File[] directoryListing = photosRootObj.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isDirectory()) {
					File device = new File(child.getAbsolutePath()
							+ "/device.plist");
					if (device.exists()) {
						try {
							if (compareDeviceNameFromPlist(
									device.getAbsolutePath(),
									deviceName)) {
								result = child.getAbsolutePath();
							}
						} catch (Exception ex) {
							log.error(ex.getStackTrace());
						}
					}
				}
			}
		}

		return result;
	}

	public static void CreateSimulatorPhotoLib(final String simulatorVersion,
			String[] srcPictPaths, boolean shouldDoCleanup, boolean useSimCtl)
			throws Exception {

		String simPath = FindSimultorFolder(simulatorVersion);
		String libPath = simPath + "/data/";

		if (!new File(libPath).exists()) {
			log.error(String.format(
					"IOS simulator v. %s has not been found on this system",
					simulatorVersion));
			return;
		}
		File mediaObj = new File(GetMediaPath(libPath));
		if (mediaObj.exists()) {
			if (useSimCtl) {
				CommonUtils.executeOsXCommand(new String[] { "/bin/bash", "-c",
						SIMCTL + " erase " + new File(simPath).getName() });
			}
			FileUtils.deleteDirectory(mediaObj);
		}

		File photosRootObj = new File(libPath);

		for (String picturePath : srcPictPaths) {
			if (useSimCtl) {
				CommonUtils.executeOsXCommand(new String[] { "/bin/bash", "-c",
						SIMCTL + " boot " + new File(simPath).getName() });
				CommonUtils.executeOsXCommand(new String[] {
						"/bin/bash",
						"-c",
						SIMCTL + " addphoto " + new File(simPath).getName()
								+ " " + picturePath });
				CommonUtils.executeOsXCommand(new String[] { "/bin/bash", "-c",
						SIMCTL + " shutdown " + new File(simPath).getName() });
			} else {
				File from = new File(picturePath);
				File to = new File(photosRootObj.getAbsolutePath(),
						from.getName());
				if (from.isDirectory()) {
					FileUtils.copyDirectory(from, to, false);
				} else {
					FileUtils.copyFile(from, to);
				}
			}
		}
	}
}
