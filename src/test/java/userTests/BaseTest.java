package userTests;

import java.util.Properties;

import utils.PropertiesFileReader;

/*
 * This class has common constants and methods.
 */

public class BaseTest {

	protected static Properties properties = PropertiesFileReader.getProperties();

	public static String BASE_URL = properties.getProperty("secureProtocol") + properties.getProperty("baseApiUrl");
	public static String USER_ENDPOINT = properties.getProperty("userEndpoint");
	public static String PROJECTS_ENDPOINT = properties.getProperty("projectsEndpoint");
	public static String JSON_FORMAT = properties.getProperty("jsonFormat");
	public static String XML_FORMAT = properties.getProperty("xmlFormat");
	public static String PASSWORD = properties.getProperty("testPassword");

}
