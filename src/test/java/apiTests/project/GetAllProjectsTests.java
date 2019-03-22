package apiTests.project;

import static io.restassured.RestAssured.given;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apiTests.common.BaseTest;
import data.UserBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.RandomValueGenerator;

public class GetAllProjectsTests extends BaseTest {

	@BeforeClass(groups = { "project", "wip", "happyPath" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;
		RestAssured.basePath = PROJECTS_ENDPOINT + JSON_FORMAT;

	}

	@Test(groups = { "project", "happyPath" })
	public void getAllProjectsTest() {

		// Precondition: existing user
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		Response responseCreate = createUser(body);
		int defaultProjectId = responseCreate.path("DefaultProjectId");

		String token = getToken(body);

		// get all projects of user
		given()
				.basePath("/")
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.when()
				.get(PROJECTS_ENDPOINT + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// check that the first project in the array is the default project
				.body("[0].Id", IsEqual.equalTo(defaultProjectId),
						// and there are more than one
						"[1].Id", IsNot.not(0));

	}

}
