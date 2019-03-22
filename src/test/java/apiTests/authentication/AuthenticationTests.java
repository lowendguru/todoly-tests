package apiTests.authentication;

import static io.restassured.RestAssured.given;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apiTests.common.BaseTest;
import data.UserBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import utils.RandomValueGenerator;

public class AuthenticationTests extends BaseTest {

	@BeforeClass(groups = { "authentication", "wip", "happyPath" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;

	}

	@Test(groups = { "authentication", "happyPath" })
	public void getTokenTest() {

		// Precondition: create User
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);
		createUser(body);

		given()
				.basePath(AUTHENTICATION_ENDPOINT)
				.log().all()
				.contentType(ContentType.JSON)
				.auth().preemptive().basic(body.getEmail(), body.getPassword())
				.when()
				.get("/token" + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("UserEmail", IsEqual.equalTo(body.getEmail()),
						"TokenString", IsNot.not(""));

		// TODO add assertion for expiration time 5 hours in the future

	}

	@Test(groups = { "authentication", "happyPath" })
	public void isAuthenticatedValidTest() {

		// Precondition: create user and get token
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		createUser(body);

		String token = getToken(body);

		given()
				.basePath(AUTHENTICATION_ENDPOINT)
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.when()
				.get("/isauthenticated" + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// expecting the boolean to be in a valid JSON format
				.body("IsAuthenticated", IsEqual.equalTo(true));

	}

	@Test(groups = { "authentication" })
	public void isAuthenticatedInvalidTest() {

		given()
				.basePath(AUTHENTICATION_ENDPOINT)
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", "invalidTokenValue")
				.when()
				.get("/isauthenticated" + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// expecting the boolean to be in a valid JSON format
				.body("IsAuthenticated", IsEqual.equalTo(false));

	}

	@Test(groups = { "authentication", "happyPath" })
	public void deleteTokenTest() {
		// verify that token can be deleted
		// Precondition: create user and get token
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		createUser(body);

		String token = getToken(body);

		// delete the token
		given()
				.basePath(AUTHENTICATION_ENDPOINT)
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.when()
				.delete("/token" + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// token is returned in the body
				.body("TokenString", IsEqual.equalTo(token));

		// verify that token is not valid any more
		given()
				.basePath(AUTHENTICATION_ENDPOINT)
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.when()
				.get("/isauthenticated" + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// expecting the boolean to be in a valid JSON format
				.body("IsAuthenticated", IsEqual.equalTo(false));
	}

}
