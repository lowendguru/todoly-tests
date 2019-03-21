package apiTests.common;

import static io.restassured.RestAssured.given;

import java.util.Properties;

import data.UserBody;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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

	// Errors
	public static String ERROR_MESSAGE_EMAIL_EXISTS = properties.getProperty("errorMessageEmailExists");
	public static int ERROR_CODE_EMAIL_EXISTS = Integer.parseInt(properties.getProperty("errorCodeEmailExists"));
	public static String ERROR_MESSAGE_INVALID_EMAIL = properties.getProperty("errorMessageInvalidEmail");
	public static int ERROR_CODE_INVALID_EMAIL = Integer.parseInt(properties.getProperty("errorCodeInvalidEmail"));
	public static String ERROR_MESSAGE_INVALID_FULLNAME = properties.getProperty("errorMessageInvalidFullName");
	public static int ERROR_CODE_INVALID_FULLNAME = Integer
			.parseInt(properties.getProperty("errorCodeInvalidFullName"));
	public static String ERROR_MESSAGE_INVALID_INPUT = properties.getProperty("errorMessageInvalidInput");
	public static int ERROR_CODE_INVALID_INPUT = Integer.parseInt(properties.getProperty("errorCodeInvalidInput"));
	public static String ERROR_MESSAGE_PASSWORD_SHORT = properties.getProperty("errorMessagePasswordShort");
	public static int ERROR_CODE_PASSWORD_SHORT = Integer.parseInt(properties.getProperty("errorCodePasswordShort"));
	
	

	
	protected Response createUser(UserBody body) {

		return given()
				.log().all()
				.contentType("application/json")
				.body(body)
				.when()
				.post()
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.extract().response();
	}

}
