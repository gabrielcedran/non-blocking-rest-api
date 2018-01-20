package br.com.cedran.web;

import br.com.cedran.client.ExternalResponseDTO;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import static br.com.cedran.utils.JsonUtils.toJson;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.jayway.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestEndpointsControllerTest {

    @Value("http://localhost:${local.server.port}")
    private String serverUrl;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(
            wireMockConfig()
                    .port(9000)
                    .notifier(new Slf4jNotifier(true)));

    @Before
    public void setUp() {
        wireMockRule.stubFor(
            get(urlEqualTo("/name/1"))
                .willReturn(
                    aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(toJson(new ExternalResponseDTO("Wire Mock!")))));
    }

    @Test
    public void testBlocking() {
        given()
//            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//            .body(JsonUtils.toJson(new ExternalResponseDTO("Test!")))
        .when()
            .get(serverUrl + "/blocking-endpoint/{id}", 1L)
            .prettyPeek()
        .then()
            .statusCode(OK.value())
            .body("name", Matchers.equalTo("Wire Mock!"))
            .body("description", Matchers.equalTo("Blocking endpoint"));

    }

    @Test
    public void testNonBlocking() {
        given()
//            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//            .body(JsonUtils.toJson(new ExternalResponseDTO("Test!")))
            .param("id", 1L)
        .when()
            .get(serverUrl + "/nonblocking-endpoint")
            .prettyPeek()
        .then()
            .statusCode(OK.value())
            .body("name", Matchers.equalTo("Wire Mock!"))
            .body("description", Matchers.equalTo("Non blocking endpoint"));

    }
}
