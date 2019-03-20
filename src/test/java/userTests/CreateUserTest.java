package userTests;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import utils.RandomValueGenerator;

/*
 * Tests for the User endpoint
 */

public class CreateUserTest extends BaseTest {

	@BeforeTest
	public void setUp() {
		RestAssured.baseURI = BASE_URL;
		RestAssured.basePath = USER_ENDPOINT + JSON_FORMAT;

	}

	@Test
	public void createUserBasic() {

		given()
				.log().all()
				.contentType("application/json")
				.body(RandomValueGenerator.getUserBodyBasic())
				.when()
				.post()
				.then()
				.log().all()
				.and()
				.assertThat()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON);

	}
}
