package br.com.cedran.web;

import br.com.cedran.dto.RequestDTO;
import br.com.cedran.utils.JsonUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestControllerTest {

    @Value("http://localhost:${local.server.port}")
    private String serverUrl;

    @Test
    public void testBlocking() {
        given()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(JsonUtils.toJson(new RequestDTO("Test!")))
        .when()
            .post(serverUrl + "/blocking-endpoint")
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", Matchers.equalTo("Test!"))
            .body("description", Matchers.equalTo("Blocking endpoint"));

    }

    @Test
    public void testNonBlocking() {
        given()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(JsonUtils.toJson(new RequestDTO("Test!")))
        .when()
            .post(serverUrl + "/nonblocking-endpoint")
            .prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", Matchers.equalTo("Test!"))
            .body("description", Matchers.equalTo("Non blocking endpoint"));

    }
}
