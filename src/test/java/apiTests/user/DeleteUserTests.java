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

public class DeleteUserTests extends BaseTest {

	@BeforeClass(groups = { "user", "wip", "happyPath" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;

	}

	@Test(groups = { "user", "happyPath" })
	public void deleteUserTest() {

		// Precondition: create user and get token
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		createUser(body);

		String token = getToken(body);

		int userId = getUserId(token);

		given()
				.basePath("/")
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.when()
				.delete(USER_ENDPOINT + "/" + userId + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("Id", IsEqual.equalTo(userId));

		// verify that user does not exist
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
				.body("ErrorMessage", IsEqual.equalTo(ERROR_MESSAGE_ACCOUNT_DOES_NOT_EXIST),
						"ErrorCode", IsEqual.equalTo(ERROR_CODE_ACCOUNT_DOES_NOT_EXIST));

	}

}
