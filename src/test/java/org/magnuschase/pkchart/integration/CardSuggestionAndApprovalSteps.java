package org.magnuschase.pkchart.integration;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.magnuschase.pkchart.model.Language;
import org.magnuschase.pkchart.model.RequestType;
import org.magnuschase.pkchart.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CardSuggestionAndApprovalSteps {
  @Value("${local.server.port}")
  private int port;

  @Autowired private AuthService authService;

  private String adminToken;
  private String userToken;
  private Response lastResponse;
  private Map<String, Object> lastRequestPayload = new HashMap<>();

  @Given("{string} with rarity {string} and set {string} does not exist in the database")
  public void card_with_rarity_and_set_does_not_exist(
      String cardName, String rarity, String setName) {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .get("/card/all");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertFalse(response.getBody().asString().contains(cardName));
  }

  @Given("{string} exists in the database")
  public void card_exists_in_database(String cardName) {
    // Add the card directly for test setup
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", cardName);
    payload.put("rarity", "COMMON"); // Default rarity for test
    payload.put("setName", "Test Set");
    payload.put("number", 1);
    payload.put("language", Language.ENG);
    // Ensure set exists
    Map<String, Object> setPayload = new HashMap<>();
    setPayload.put("name", "Test Set");
    setPayload.put("symbol", "TEST");
    setPayload.put("language", Language.ENG);
    setPayload.put("size", 1);
    RestAssured.given()
        .port(port)
        .header("Authorization", "Bearer " + adminToken)
        .contentType("application/json")
        .body(setPayload)
        .put("/set/add");
    // Add card
    RestAssured.given()
        .port(port)
        .header("Authorization", "Bearer " + adminToken)
        .contentType("application/json")
        .body(payload)
        .put("/card/add");
    // Verify
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .get("/card/all");
    Assertions.assertTrue(response.getBody().asString().contains(cardName));
  }

  @Given("Set {string} exists")
  public void set_exists(String name) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", name);
    payload.put("symbol", "TEST");
    payload.put("language", Language.ENG);
    payload.put("size", 0);
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .contentType("application/json")
            .body(payload)
            .put("/set/add");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
  }

  @Given("Admin {string} is logged in")
  public void admin_is_logged_in(String email) {
    // Register admin if not exists, promote, then login
    String password = "adminpass";
    Map<String, Object> register = new HashMap<>();
    register.put("email", email);
    register.put("username", email);
    register.put("password", password);
    RestAssured.given()
        .port(port)
        .contentType("application/json")
        .body(register)
        .post("/auth/register");
    // Promote to admin
    authService.promoteUserRole(email);
    // Login
    Map<String, Object> login = new HashMap<>();
    login.put("email", email);
    login.put("password", password);
    Response loginResp =
        RestAssured.given()
            .port(port)
            .contentType("application/json")
            .body(login)
            .post("/auth/login");
    adminToken = loginResp.jsonPath().getString("accessToken");
  }

  private void ensureUserLoggedIn() {
    if (userToken == null) {
      String email = "user@example.com";
      String password = "userpass";
      Map<String, Object> register = new HashMap<>();
      register.put("email", email);
      register.put("username", email);
      register.put("password", password);
      RestAssured.given()
          .port(port)
          .contentType("application/json")
          .body(register)
          .post("/auth/register");
      Map<String, Object> login = new HashMap<>();
      login.put("email", email);
      login.put("password", password);
      Response loginResp =
          RestAssured.given()
              .port(port)
              .contentType("application/json")
              .body(login)
              .post("/auth/login");
      userToken = loginResp.jsonPath().getString("accessToken");
    }
  }

  @When("The user requests the addition of {string} with rarity {string} from set {string}")
  public void user_requests_card_addition(String cardName, String rarity, String setName) {
    ensureUserLoggedIn();
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", cardName);
    payload.put("rarity", rarity);
    payload.put("setName", setName);
    payload.put("language", Language.ENG);
    payload.put("number", 0);
    lastRequestPayload = payload;
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + userToken)
            .contentType("application/json")
            .body(payload)
            .post("/requests/card/add");
  }

  @Then("The request should be visible to the admin")
  public void request_should_be_visible_to_admin() {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .get("/requests/all");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    // Debugging: print the response body
    Assertions.assertTrue(
        response.getBody().asString().contains((String) lastRequestPayload.get("name")));
  }

  @Given("There is a pending request for {string} with rarity {string} from set {string}")
  public void pending_request_exists(String cardName, String rarity, String setName) {
    user_requests_card_addition(cardName, rarity, setName);
    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
  }

  private Long extractRequestId(Response response, Map<String, Object> payload, RequestType type) {
    // Find the request ID in the JSON response for the correct type and name
    String body = response.getBody().asString();
    String typeKey =
        switch (type) {
          case CARD_ADD -> "cardAdditions";
          case CARD_REMOVE -> "cardRemovals";
          case SET_ADD -> "setAdditions";
        };
    int idx = body.indexOf(typeKey);
    if (idx == -1) throw new IllegalArgumentException("Type section not found");
    int nameIdx = body.indexOf((String) payload.get("name"), idx);
    if (nameIdx == -1) throw new IllegalArgumentException("Request name not found");
    // Find the nearest "id":<number> before the name
    int idIdx = body.lastIndexOf("\"id\":", nameIdx);
    if (idIdx == -1) throw new IllegalArgumentException("Request id not found");
    int start = idIdx + 5;
    int end = body.indexOf(',', start);
    if (end == -1) end = body.indexOf('}', start);
    return Long.parseLong(body.substring(start, end).replaceAll("[^0-9]", ""));
  }

  @When("The admin approves the request")
  public void admin_approves_request() {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .get("/requests/all");
    Long requestId = extractRequestId(response, lastRequestPayload, RequestType.CARD_ADD);
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .post("/requests/approve/" + requestId + "?type=CARD_ADD");
  }

  @Then(
      "{string} with rarity {string} from set {string} should be added to the available card list")
  public void card_should_be_added(String cardName, String rarity, String setName) {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + userToken)
            .get("/card/all");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertTrue(response.getBody().asString().contains(cardName));
  }

  @When("The user requests removal of {string}")
  public void user_requests_removal(String cardName) {
    ensureUserLoggedIn();
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", cardName);
    payload.put("rarity", "COMMON");
    payload.put("setName", "Test Set");
    payload.put("number", 1);
    payload.put("language", Language.ENG);
    payload.put("reason", "This card does not exist, it's entirely fake.");
    lastRequestPayload = payload;
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + userToken)
            .contentType("application/json")
            .body(payload)
            .post("/requests/card/remove");
  }

  @Then("The request should be sent to the admin for review")
  public void removal_request_sent_to_admin() {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .get("/requests/all");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertTrue(
        response.getBody().asString().contains((String) lastRequestPayload.get("name")));
  }

  @Given("There is a pending request for {string}")
  public void pending_removal_request_exists(String cardName) {
    user_requests_removal(cardName);
    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
  }

  @When("The admin rejects the request")
  public void admin_rejects_request() {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .get("/requests/all");
    Long requestId = extractRequestId(response, lastRequestPayload, RequestType.CARD_REMOVE);
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .post("/requests/reject/" + requestId + "?type=CARD_REMOVE");
  }

  @Then("{string} should not be removed from the database")
  public void card_should_not_be_removed(String cardName) {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + userToken)
            .get("/card/all");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertTrue(response.getBody().asString().contains(cardName));
  }

  @Then("The user should be notified about the decision")
  public void user_should_be_notified() {
    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
  }

  @When("The user requests the addition of {string} set")
  public void user_requests_set_addition(String setName) {
    ensureUserLoggedIn();
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", setName);
    payload.put("language", Language.ENG);
    payload.put("symbol", "TEST");
    payload.put("size", 0);
    lastRequestPayload = payload;
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + userToken)
            .contentType("application/json")
            .body(payload)
            .post("/requests/set/add");
  }

  @Given("There is a pending request for {string} set")
  public void pending_set_request_exists(String setName) {
    user_requests_set_addition(setName);
    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
  }

  @When("The admin approves the request for the set")
  public void admin_approves_set_request() {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .get("/requests/all");
    Long requestId = extractRequestId(response, lastRequestPayload, RequestType.SET_ADD);
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + adminToken)
            .post("/requests/approve/" + requestId + "?type=SET_ADD");
  }

  @Then("{string} should be added to the available sets")
  public void set_should_be_added(String setName) {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + userToken)
            .get("/set/all");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertTrue(response.getBody().asString().contains(setName));
  }
}
