package apiTests.project;

import static io.restassured.RestAssured.given;

import org.hamcrest.core.IsEqual;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apiTests.common.BaseTest;
import data.UserBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.RandomValueGenerator;

public class GetProjectByIdTests extends BaseTest {

	@BeforeClass(groups = { "project", "wip", "happyPath" })
	public void setUp() {
		RestAssured.baseURI = BASE_URL;
		RestAssured.basePath = PROJECTS_ENDPOINT + JSON_FORMAT;

	}

	@Test(groups = { "project", "happyPath" })
	public void getProjectById() {

		// Precondition: existing user
		UserBody body = new UserBody()
				.withEmail(RandomValueGenerator.getRandomEmail())
				.withFullName(RandomValueGenerator.getRandomSentence(2))
				.withPassword(PASSWORD);

		Response responseCreate = createUser(body);
		int defaultProjectId = responseCreate.path("DefaultProjectId");

		String token = getToken(body);

		// get one project by id
		given()
				.basePath("/")
				.log().all()
				.contentType(ContentType.JSON)
				.header("Token", token)
				.when()
				.get(PROJECTS_ENDPOINT + "/" + defaultProjectId + JSON_FORMAT)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.contentType(ContentType.JSON)
				// check that the project is the one that was requested project
				.body("Id", IsEqual.equalTo(defaultProjectId));

	}

}
