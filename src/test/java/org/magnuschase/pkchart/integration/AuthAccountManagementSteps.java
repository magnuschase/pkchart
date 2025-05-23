package org.magnuschase.pkchart.integration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class AuthAccountManagementSteps {

  private String email;
  private String username;
  private String password;
  private String displayName;
  private String accessToken;
  private Response response;

  @Given("The user provides a valid email {string}, username {string} and password {string}")
  public void the_user_provides_valid_registration_data(
      String email, String username, String password) {
    this.email = email;
    this.username = username;
    this.password = password;
  }

  @When("The user submits the registration form")
  public void the_user_submits_the_registration_form() {
    response =
        given()
            .contentType("application/json")
            .body(
                "{\"email\":\""
                    + email
                    + "\",\"username\":\""
                    + username
                    + "\",\"password\":\""
                    + password
                    + "\"}")
            .when()
            .post("/auth/register");
  }

  @Then("The system should respond with a 200 Success status")
  public void the_system_should_respond_with_200_success_status() {
    response.then().statusCode(200);
  }

  @Then("The user account should be created")
  public void the_user_account_should_be_created() {
    String token = response.jsonPath().getString("accessToken");
    assertNotNull(token);
    assertFalse(token.isEmpty());
  }

  @Given("The email {string} is already in use")
  public void the_email_is_already_in_use(String email) {
    this.email = email;
    // Register the user if not already present
    Response registerResponse =
        given()
            .contentType("application/json")
            .body(
                "{\"email\":\""
                    + email
                    + "\",\"username\":\"existinguser\",\"password\":\"existingpass\"}")
            .when()
            .post("/auth/register");
    // Accept both 200 (created) and 403 (already exists)
    int status = registerResponse.getStatusCode();
    assertTrue(status == 200 || status == 403);
  }

  @When("The user submits a registration form with this email")
  public void the_user_submits_a_registration_form_with_this_email() {
    response =
        given()
            .contentType("application/json")
            .body(
                "{\"email\":\"" + email + "\",\"username\":\"testuser\",\"password\":\"testpass\"}")
            .when()
            .post("/auth/register");
  }

  @Then("Inform the user that the email is already registered")
  public void inform_the_user_that_the_email_is_already_registered() {
    String body = response.getBody().asString();
    assertTrue(body.contains("already registered") || body.contains("already in use"));
  }

  @Then("The user should receive 403 Forbidden status")
  public void the_user_should_receive_403_forbidden_status() {
    response.then().statusCode(403);
  }

  @Given("The user is logged in")
  public void the_user_is_logged_in() {
    // Try to register the user
    Response registerResponse =
        given()
            .contentType("application/json")
            .body(
                "{\"email\":\"ash.ketchum@palettetown.com\",\"username\":\"ash\",\"password\":\"pikachu123\"}")
            .when()
            .post("/auth/register");
    int status = registerResponse.getStatusCode();

    // If already exists, reset password (simulate as needed)
    if (status == 403 || status == 409) {
      // Try to log in with the known password
      Response loginResponse =
          given()
              .contentType("application/json")
              .body("{\"email\":\"ash.ketchum@palettetown.com\",\"password\":\"pikachu123\"}")
              .when()
              .post("/auth/login");
      // If login fails, we may need to reset the password via a direct DB call or a special test
      // endpoint
      // For now, just assert forbidden is not returned
      assertNotEquals(
          403,
          loginResponse.getStatusCode(),
          "User exists but cannot log in with default password. Reset password in test setup.");
      loginResponse.then().statusCode(200);
      accessToken = loginResponse.jsonPath().getString("accessToken");
      assertNotNull(accessToken);
    } else {
      // Registration succeeded, get token
      accessToken = registerResponse.jsonPath().getString("accessToken");
      assertNotNull(accessToken);
    }
    this.email = "ash.ketchum@palettetown.com";
    this.password = "pikachu123";
  }

  @When("The user updates their display name to {string} and email to {string}")
  public void the_user_updates_their_display_name_and_email(String displayName, String newEmail) {
    this.displayName = displayName;
    this.email = newEmail;
    response =
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + accessToken)
            .body("{\"displayName\":\"" + displayName + "\",\"email\":\"" + newEmail + "\"}")
            .when()
            .put("/auth/user-details");
  }

  @Then("The account information should be updated successfully")
  public void the_account_information_should_be_updated_successfully() {
    response.then().statusCode(200);
    assertEquals(email, response.jsonPath().getString("email"));
    assertEquals(displayName, response.jsonPath().getString("displayName"));
  }

  @Given("The user is logged in and provides the current password {string}")
  public void the_user_is_logged_in_and_provides_current_password(String currentPassword) {
    the_user_is_logged_in();
    this.password = currentPassword;
  }

  @When("The user submits a new password {string}")
  public void the_user_submits_a_new_password(String newPassword) {
    response =
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + accessToken)
            .body(
                "{\"currentPassword\":\""
                    + password
                    + "\",\"newPassword\":\""
                    + newPassword
                    + "\"}")
            .when()
            .post("/auth/change-password");
  }

  @Then("The password should be updated")
  public void the_password_should_be_updated() {
    response.then().statusCode(200);
  }

  @Then("The user should be required to log in again with the new password")
  public void the_user_should_be_required_to_log_in_again_with_the_new_password() {
    // Try to log in with the new password
    Response loginResponse =
        given()
            .contentType("application/json")
            .body("{\"email\":\"" + email + "\",\"password\":\"pikachu456\"}")
            .when()
            .post("/auth/login");
    loginResponse.then().statusCode(200);
    String newToken = loginResponse.jsonPath().getString("accessToken");
    assertNotNull(newToken);
  }
}
