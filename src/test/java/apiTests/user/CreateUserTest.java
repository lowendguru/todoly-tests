package apiTests.user;

import static io.restassured.RestAssured.given;

import org.hamcrest.core.IsEqual;
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

	@BeforeTest(groups = { "user" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;
		RestAssured.basePath = USER_ENDPOINT + JSON_FORMAT;

	}

	@Test(groups = { "user" })
	public void createUserBasic() {

		// create a body with mandatory fields
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
						"Password", IsEqual.equalTo(null));
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_EMAIL_EXISTS));

	}

}
