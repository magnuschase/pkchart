package org.magnuschase.pkchart.integration;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdminPriceManagementSteps {

  @Value("${local.server.port}")
  private int port;

  @Autowired private TestContext testContext;

  @Given("The admin sets a new price for {string} from set {string} to {double} PLN")
  public void the_admin_sets_a_new_price_for_card(String cardName, String setName, double price) {
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

  @Given(
      "The admin sets a new price for {string} from set {string} to {double} PLN and {string} from set {string} to {double} PLN")
  public void the_admin_sets_a_new_price_for_multiple_cards(
      String cardName,
      String setName,
      double price,
      String cardName2,
      String setName2,
      double price2) {
    // Find cardId by listing all cards and matching name and set
    Response cardsResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.adminToken)
            .get("/card/all");
    Assertions.assertEquals(HttpStatus.OK.value(), cardsResponse.getStatusCode());
    String body = cardsResponse.getBody().asString();
    // Get first cardId
    int idx = body.indexOf(cardName);
    Assertions.assertTrue(idx != -1, "Card not found in database: " + cardName);
    int idIdx = body.lastIndexOf("\"id\":", idx);
    int idEnd = body.indexOf(",", idIdx);
    Long cardId = Long.parseLong(body.substring(idIdx + 5, idEnd).replaceAll("[^0-9]", ""));
    // Get second cardId
    int idx2 = body.indexOf(cardName2);
    Assertions.assertTrue(idx2 != -1, "Card not found in database: " + cardName2);
    int idIdx2 = body.lastIndexOf("\"id\":", idx2);
    int idEnd2 = body.indexOf(",", idIdx2);
    Long cardId2 = Long.parseLong(body.substring(idIdx2 + 5, idEnd2).replaceAll("[^0-9]", ""));
    // Prepare price request
    Map<String, Object> pricePayload = new HashMap<>();
    pricePayload.put("date", "2024-05-25");
    pricePayload.put("price", price);
    pricePayload.put("cardId", cardId);
    Map<String, Object> pricePayload2 = new HashMap<>();
    pricePayload2.put("date", "2024-05-25");
    pricePayload2.put("price", price2);
    pricePayload2.put("cardId", cardId2);

    Map<String, Object> payload = new HashMap<>();
    payload.put("prices", Arrays.asList(pricePayload, pricePayload2));

    Response priceResponse =
        RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.adminToken)
            .contentType("application/json")
            .body(payload)
            .put("/prices/update-multiple");

    Assertions.assertEquals(HttpStatus.OK.value(), priceResponse.getStatusCode());
  }
}
