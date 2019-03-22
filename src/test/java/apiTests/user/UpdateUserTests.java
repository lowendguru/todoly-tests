package apiTests.user;

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

public class UpdateUserTests extends BaseTest {

	@BeforeClass(groups = { "user", "wip", "happyPath" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;
		RestAssured.basePath = USER_ENDPOINT + JSON_FORMAT;

	}

	@Test(groups = { "user", "happyPath" })
	public void updateUserValidNameTest() {
		// Create user
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		createUser(body);

		String token = getToken(body);
		int userId = getUserId(token);

		// update user
		UserBody body2 = new UserBody()
				.withFullName(RandomValueGenerator.getRandomSentence(2));

		given()
				.basePath("/")
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.body(body2)
				.when()
				.put(USER_ENDPOINT + "/" + userId + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// verify that name has changed to new one
				.body("FullName", IsEqual.equalTo(body2.getFullName()));
	}

	@Test(groups = { "user" })
	public void updateInvalidEmailTest() {
		// Create user
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		createUser(body);

		String token = getToken(body);
		int userId = getUserId(token);

		// another body for user
		UserBody body2 = new UserBody()
				.withEmail("@" + RandomValueGenerator.getRandomText(10) + ".com");

		given()
				.basePath("/")
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.body(body2)
				.when()
				.put(USER_ENDPOINT + "/" + userId + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// verify that email has not changed to an invalid email
				.body("Email", IsNot.not(body2.getEmail()));
	}

}
