package apiTests.user;

import static io.restassured.RestAssured.given;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import apiTests.common.BaseTest;
import data.UserBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import utils.RandomValueGenerator;

/*
 * Tests for the User endpoint
 */

public class CreateUserTest extends BaseTest {

	@BeforeTest(groups = { "user", "wip" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;
		RestAssured.basePath = USER_ENDPOINT + JSON_FORMAT;

	}

	@Test(groups = { "user", "happyPath" })
	public void createUserBasic() {

		// create a body with mandatory fields and valid values
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		given()
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
				.body("Email", IsEqual.equalTo(body.getEmail()),
						"FullName", IsEqual.equalTo(body.getFullName()),
						"Password", IsEqual.equalTo(null),
						"IsProUser", IsEqual.equalTo(false));
	}

	@Test(groups = { "user" })
	public void createUserEmptyPassword() {

		// create a body with empty password
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword("");

		given()
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_PASSWORD_SHORT),
						"ErrorCode", IsEqual.equalTo(ERROR_CODE_PASSWORD_SHORT));

	}

	@Test(groups = { "user" })
	public void createUserNoEmail() {

		// create a body with no email
		UserBody body = new UserBody()
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		given()
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_INVALID_EMAIL),
						"ErrorCode", IsEqual.equalTo(ERROR_CODE_INVALID_EMAIL));

	}

	@Test(groups = { "user" })
	public void createUserInvalidFormatEmail() {
		// create a body with invalid email
		UserBody body = new UserBody()
				.withEmail("someInvalid Email@mail.com")
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		given()
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_INVALID_EMAIL),
						"ErrorCode", IsEqual.equalTo(ERROR_CODE_INVALID_EMAIL));

	}

	@Test(groups = { "user" })
	public void createUserEmptyName() {
		// create a body with empty name
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName("")
				.withPassword(PASSWORD);

		given()
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_INVALID_FULLNAME),
						"ErrorCode", IsEqual.equalTo(ERROR_CODE_INVALID_FULLNAME));

	}

	@Test(groups = { "user" })
	public void createUserMinimumName() {
		// create a body with minimum allowed length for FullName
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomText(1))
				.withPassword(PASSWORD);

		given()
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
				.body("Email", IsEqual.equalTo(body.getEmail()),
						"FullName", IsEqual.equalTo(body.getFullName()),
						"Password", IsEqual.equalTo(null));
	}

	@Test(groups = { "user" })
	public void createUserMaximumName() {

		// create a body with maximum allowed length for FullName
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomText(100))
				.withPassword(PASSWORD);

		given()
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
				.body("Email", IsEqual.equalTo(body.getEmail()),
						"FullName", IsEqual.equalTo(body.getFullName()),
						"Password", IsEqual.equalTo(null));
	}

	@Test(groups = { "user" })
	public void createUserExceedMaximumName() {

		// create a body exceeding the allowed length for FullName
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomText(101))
				.withPassword(PASSWORD);

		given()
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_INVALID_INPUT),
						"ErrorCode", IsEqual.equalTo(ERROR_CODE_INVALID_INPUT));
	}

	@Test(groups = { "user" })
	public void createUserExistingEmail() {

		// create a body with mandatory fields
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		// Precondition: user already exists
		createUser(body);

		// Try to create user with same email
		given()
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_EMAIL_EXISTS),
						"ErrorCode", IsEqual.equalTo(ERROR_CODE_EMAIL_EXISTS));

	}

	@Test(groups = { "user" })
	public void createUserWithAddItemMoreExpanded() {

		// create a body with mandatory fields and valid values
		// including
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD)
				.withAddItemMoreExpanded("false");

		given()
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
				.body("Email", IsEqual.equalTo(body.getEmail()),
						"FullName", IsEqual.equalTo(body.getFullName()),
						"Password", IsEqual.equalTo(null),
						"IsProUser", IsEqual.equalTo(false),
						"AddItemMoreExpanded", IsEqual.equalTo(false));
	}

	@Test(groups = { "user" })
	public void createUserWithInvalidAddItemMoreExpanded() {

		// create a body with mandatory fields and valid values
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD)
				.withAddItemMoreExpanded("asd");

		given()
				.log().all()
				.contentType("application/json")
				.body(body)
				.when()
				.post()
				.then()
				.log().all()
				.assertThat()
				// expecting errors to be handled, no 500 errors
				.statusCode(200)
				.contentType(ContentType.JSON);
	}

	@Test(groups = { "user" })
	public void createUserWithDefaultProjectId() {

		// create a body with mandatory fields and DefaultProjectId
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD)
				.withDefaultProjectId("0");

		given()
				.log().all()
				.contentType("application/json")
				.body(body)
				.when()
				.post()
				.then()
				.log().all()
				.assertThat()
				// expecting errors to be handled
				.statusCode(200)
				.contentType(ContentType.JSON)
				// DefaultProjectId is expected to be auto-generated
				.body("Email", IsEqual.equalTo(body.getEmail()),
						"FullName", IsEqual.equalTo(body.getFullName()),
						"Password", IsEqual.equalTo(null),
						"IsProUser", IsEqual.equalTo(false),
						"DefaultProjectId", IsNot.not(0));
	}

}
