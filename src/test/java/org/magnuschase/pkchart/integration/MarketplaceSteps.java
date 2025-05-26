package org.magnuschase.pkchart.integration;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.magnuschase.pkchart.model.CardCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class MarketplaceSteps {
  @Value("${local.server.port}")
  private int port;

  @Autowired private TestContext testContext;

  private Response lastResponse;


    @When("The user creates a sale listing for {string} from set {string} and condition {string} with price {double} PLN")
    public void the_user_creates_a_sale_listing(String cardName, String setName, String condition, double price) {
        Long portfolioEntryId = findPortfolioEntryId(cardName, setName, condition);
        Map<String, Object> payload = new HashMap<>();
        payload.put("portfolioEntryId", portfolioEntryId);
        payload.put("price", price);

        lastResponse = RestAssured.given()
                .port(port)
                .header("Authorization", "Bearer " + testContext.userToken)
                .contentType("application/json")
                .body(payload)
                .put("/marketplace/sell");

        Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
    }

    @Then("The listing should be visible in the marketplace")
    public void the_listing_should_be_visible_in_the_marketplace() {
        Response response = RestAssured.given()
                .port(port)
                .header("Authorization", "Bearer " + testContext.userToken)
                .contentType("application/json")
                .get("/marketplace");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        String body = response.getBody().asString();
        Assertions.assertTrue(body.contains("Latias EX"));
    }

    @Then("It should include the card name, price, and contact information")
    public void it_should_include_card_name_price_contact() {
        String body = lastResponse.getBody().asString();
        Assertions.assertTrue(body.contains("Latias EX"));
        Assertions.assertTrue(body.contains("625.0"));
        Assertions.assertTrue(body.contains("ash.ketchum@palettetown.com")); // or whatever contact info is expected
    }

    @When("The user creates a buy offer for {string} from set {string} and condition {string} with offer price {double} PLN")
    public void the_user_creates_a_buy_offer(String cardName, String setName, String condition, double price) {
      Response cardsResponse = RestAssured.given()
        .port(port)
        .header("Authorization", "Bearer " + testContext.userToken)
        .get("/card/all");
    	String body = cardsResponse.getBody().asString();
    	int idx = body.indexOf(cardName);
    	Assertions.assertTrue(idx != -1, "Card not found in database: " + cardName);
    	int idIdx = body.lastIndexOf("\"id\":", idx);
    	int idEnd = body.indexOf(",", idIdx);
    	Long cardId = Long.parseLong(body.substring(idIdx + 5, idEnd).replaceAll("[^0-9]", ""));
  
        Map<String, Object> payload = new HashMap<>();
        payload.put("cardId", cardId);
        payload.put("condition", CardCondition.fromDisplayName(condition));
        payload.put("price", price);

        lastResponse = RestAssured.given()
                .port(port)
                .header("Authorization", "Bearer " + testContext.userToken)
                .contentType("application/json")
                .body(payload)
                .put("/marketplace/buy");

        Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
    }

    @Then("The buy offer should appear in the marketplace")
    public void the_buy_offer_should_appear_in_the_marketplace() {
        Response response = RestAssured.given()
                .port(port)
                .header("Authorization", "Bearer " + testContext.userToken)
                .contentType("application/json")
                .get("/marketplace");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        String body = response.getBody().asString();
        Assertions.assertTrue(body.contains("Latios EX"));
        Assertions.assertTrue(body.contains("200.0"));
    }

    @Then("Other users should be able to contact the buyer")
    public void other_users_should_be_able_to_contact_the_buyer() {
        String body = lastResponse.getBody().asString();
        Assertions.assertTrue(body.contains("ash.ketchum@palettetown.com")); // or expected contact info
    }

    @When("The user visits the marketplace")
    public void the_user_visits_the_marketplace() {
        lastResponse = RestAssured.given()
                .port(port)
                .header("Authorization", "Bearer " + testContext.userToken)
                .contentType("application/json")
                .get("/marketplace");
        Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode());
    }

    @Then("All active sale and buy listings should be visible")
    public void all_active_sale_and_buy_listings_should_be_visible() {
        String body = lastResponse.getBody().asString();
        Assertions.assertTrue(body.contains("Latias EX"));
        Assertions.assertTrue(body.contains("Latios EX"));
    }

	private Long findPortfolioEntryId(String cardName, String setName, String condition) {
    Response response = RestAssured.given()
            .port(port)
            .header("Authorization", "Bearer " + testContext.userToken)
            .get("/portfolio");
    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    var entries = response.jsonPath().getList("cards");
		CardCondition expectedCondition = CardCondition.fromDisplayName(condition);
    for (Object entryObj : entries) {
        Map<String, Object> entry = (Map<String, Object>) entryObj;
        Map<String, Object> card = (Map<String, Object>) entry.get("card");
        String entryCardName = (String) card.get("name");
        String entrySetName = (String) ((Map<String, Object>) card.get("set")).get("name");
        String entryCondition = (String) entry.get("condition");
        if (cardName.equals(entryCardName)
                && setName.equals(entrySetName)
                && expectedCondition.name().equals(entryCondition)) {
            return ((Number) entry.get("id")).longValue();
        }
    }
    throw new AssertionError("Portfolio entry not found for: " + cardName + ", " + setName + ", " + condition);
	}
}