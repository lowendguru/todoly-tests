package apiTests.user;

import static io.restassured.RestAssured.given;

import org.hamcrest.core.IsEqual;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apiTests.common.BaseTest;
import data.UserBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import utils.RandomValueGenerator;

public class GetUserTests extends BaseTest {

	@BeforeClass(groups = { "user", "wip",  "happyPath" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;

	}

	@Test(groups = { "user", "happyPath" })
	public void getUserTest() {

		// Precondition: create user and get token
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		createUser(body);

		String token = getToken(body);

		given()
				.basePath("/")
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.when()
				.get(USER_ENDPOINT + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("Email", IsEqual.equalTo(body.getEmail()));

	}

}
