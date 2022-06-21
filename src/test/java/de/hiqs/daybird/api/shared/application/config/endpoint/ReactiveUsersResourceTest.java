package de.hiqs.daybird.api.shared.application.config.endpoint;

import de.hiqs.daybird.api.shared.AbstractMockWebServerTest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ReactiveUsersResourceTest extends AbstractMockWebServerTest {

    @Test
    public void getMeTest() {

        Response response = given()
                .auth().oauth2(getAccessToken("admin", "123"))
                .header("Content-type", "application/json")
                .when().get("/users/me")
                .andReturn();

        System.out.println(response.prettyPrint());
    }
}
