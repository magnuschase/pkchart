package org.magnuschase.pkchart.integration;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.magnuschase.pkchart.interfaces.PortfolioAddCardRequestDTO;
import org.magnuschase.pkchart.interfaces.PortfolioDTO;
import org.magnuschase.pkchart.interfaces.PortfolioEntryDTO;
import org.magnuschase.pkchart.model.CardCondition;
import org.magnuschase.pkchart.model.CardRarity;
import org.magnuschase.pkchart.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PortfolioManagementSteps {

  @Value("${local.server.port}")
  private int port;

  @Autowired private TestContext testContext;

  private Response lastResponse;

  public String generateSymbol(String setName) {
    // Take first letters of each word, uppercase, max 6 chars
    String symbol =
        Arrays.stream(setName.split("\\s+"))
            .map(word -> word.substring(0, 1).toUpperCase())
            .collect(Collectors.joining());
    return symbol.substring(0, Math.min(6, symbol.length()));
  }

  @Given("The card {string} from set {string} exists")
  public void the_card_from_set_exists(String cardName, String setName) {
    // Add the card directly for test setup
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", cardName);
    payload.put("rarity", CardRarity.SAR);
    payload.put("setName", setName);
    payload.put("number", 1);
    payload.put("language", Language.ENG);
    // Ensure set exists
    Map<String, Object> setPayload = new HashMap<>();
    setPayload.put("name", setName);
    setPayload.put("symbol", generateSymbol(setName));
    setPayload.put("language", Language.ENG);
    setPayload.put("size", 1);
    RestAssured.given()
        .port(port)
        .header("Authorization", "Bearer " + testContext.adminToken)
        .contentType("application/json")
        .body(setPayload)
        .put("/set/add");
    // Add card
    RestAssured.given()
        .port(port)
        .header("Authorization", "Bearer " + testContext.adminToken)
        .contentType("application/json")
        .body(payload)
        .put("/card/add");
    // Verify
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.adminToken)
            .get("/card/all");
    Assertions.assertTrue(response.getBody().asString().contains(cardName));
  }

  @Given("Current price of {string} from set {string} is {double} PLN")
  public void current_price_of_card_is(String cardName, String setName, double price) {
    // Find cardId by listing all cards and matching name and set
    Response cardsResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.adminToken)
            .get("/card/all");
    Assertions.assertEquals(HttpStatus.OK.value(), cardsResponse.getStatusCode());
    String body = cardsResponse.getBody().asString();
    int idx = body.indexOf(cardName);
    Assertions.assertTrue(idx != -1, "Card not found in database: " + cardName);
    int idIdx = body.lastIndexOf("\"id\":", idx);
    int idEnd = body.indexOf(",", idIdx);
    Long cardId = Long.parseLong(body.substring(idIdx + 5, idEnd).replaceAll("[^0-9]", ""));

    // Prepare price request
    Map<String, Object> pricePayload = new HashMap<>();
    pricePayload.put("date", "2024-05-25");
    pricePayload.put("price", price);
    Response priceResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.adminToken)
            .contentType("application/json")
            .body(pricePayload)
            .put("/prices/update/" + cardId);

    Assertions.assertEquals(HttpStatus.OK.value(), priceResponse.getStatusCode());
  }

  @Given("User {string} is logged in")
  public void user_is_logged_in(String email) {
    String password = "pikachu123";
    Map<String, Object> register = new HashMap<>();
    register.put("email", email);
    register.put("username", "ash");
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
    testContext.userToken = loginResp.jsonPath().getString("accessToken");
  }

  @When("The user adds {string} from set {string} in condition {string} to their portfolio")
  public void the_user_adds_card_to_portfolio(String cardName, String setName, String condition) {
    // Find cardId by listing all cards and matching name and set
    Response cardsResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .get("/card/all");
    String body = cardsResponse.getBody().asString();
    // Extract cardId (simple parsing, assuming unique name+set)
    int idx = body.indexOf(cardName);
    Assertions.assertTrue(idx != -1, "Card not found in database: " + cardName);
    int idIdx = body.lastIndexOf("\"id\":", idx);
    int idEnd = body.indexOf(",", idIdx);
    Long cardId = Long.parseLong(body.substring(idIdx + 5, idEnd).replaceAll("[^0-9]", ""));

    PortfolioAddCardRequestDTO req = new PortfolioAddCardRequestDTO();
    req.setCondition(CardCondition.fromDisplayName(condition));
    req.setQuantity(1);

    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .contentType("application/json")
            .body(req)
            .put("/portfolio/add/" + cardId);

    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
  }

  @Then("{string} from set {string} should appear in the user's portfolio")
  public void card_should_appear_in_portfolio(String cardName, String setName) {
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .get("/portfolio");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertTrue(response.getBody().asString().contains(cardName));
  }

  @Then("The system should display its current price: {double} PLN")
  public void system_should_display_current_price(double price) {
    Assertions.assertTrue(lastResponse.getBody().asString().contains(String.valueOf(price)));
  }

  @Given("{string} from set {string} is in the user's portfolio")
  public void card_is_in_users_portfolio(String cardName, String setName) {
    // Add the card if not already present
    the_user_adds_card_to_portfolio(cardName, setName, "CGC 9.5");
  }

  @When("The user removes {string} from set {string} from their portfolio")
  public void user_removes_card_from_portfolio(String cardName, String setName) {
    // Find entryId by listing portfolio and matching card name
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .get("/portfolio");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    // Iterate through cards to find the entryid
    PortfolioDTO portfolio = response.getBody().as(PortfolioDTO.class);
    PortfolioEntryDTO entry =
        portfolio.getCards().stream()
            .filter(
                e ->
                    cardName.equals(e.getCard().getName())
                        && setName.equals(e.getCard().getSet().getName()))
            .findFirst()
            .orElseThrow(
                () ->
                    new AssertionError(
                        "Card not found in portfolio: " + cardName + " from set " + setName));
    Long entryId = entry.getId();

    // Remove the card
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .delete("/portfolio/remove/" + entryId);
    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
  }

  @Then("The card should no longer appear in the portfolio")
  public void card_should_no_longer_appear_in_portfolio() {
    // Use last removed card name
    String cardName = "Latias EX";
    Response response =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .get("/portfolio");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertFalse(response.getBody().asString().contains(cardName));
  }

  @Given("The user's portfolio contains {string} from set {string}")
  public void portfolio_contains_card(String cardName, String setName) {
    the_user_adds_card_to_portfolio(cardName, setName, "CGC 9.5");
  }

  @When("The user views their portfolio statistics")
  public void user_views_portfolio_statistics() {
    lastResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .get("/portfolio");
    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
  }

  @Then("The system should show total portfolio value")
  public void system_should_show_total_portfolio_value() {
    Assertions.assertTrue(lastResponse.getBody().asString().contains("totalPrice"));
  }

  @Then("All cards should be listed in the portfolio")
  public void all_cards_listed_with_prices() {
    Assertions.assertTrue(lastResponse.getBody().asString().contains("cards"));
    String body = lastResponse.getBody().asString();
    Assertions.assertTrue(body.contains("Latias EX"));
    Assertions.assertTrue(body.contains("Pikachu with Grey Felt Hat"));
  }
}
