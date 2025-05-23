package org.magnuschase.pkchart.integration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.magnuschase.pkchart.interfaces.RegisterRequestDTO;
import org.magnuschase.pkchart.model.Role;
import org.magnuschase.pkchart.security.JwtUtil;
import org.magnuschase.pkchart.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthLoginSteps {
  @Autowired private JwtUtil jwtUtil;
  @Autowired private AuthService authService;
  private String email;
  private String password;
  private Response response;
  private String accessToken;

  @Given("The user provides a valid email {string} and password {string}")
  public void the_user_provides_valid_credentials(String email, String password) {
    // Register the user directly using AuthService
    RegisterRequestDTO registerRequest = new RegisterRequestDTO();
    registerRequest.setEmail(email);
    registerRequest.setPassword(password);
    registerRequest.setUsername(email); // Set username to avoid null constraint violation
    authService.register(registerRequest);
    // Save credentials for next steps
    this.email = email;
    this.password = password;
  }

  @Given("The user provides an incorrect email or password")
  public void the_user_provides_invalid_credentials() {
    this.email = "wrong.email@domain.com";
    this.password = "wrongpassword";
  }

  @Given("The admin provides a valid email {string} and password {string}")
  public void the_admin_provides_valid_credentials(String email, String password) {
    // Register the user directly using AuthService
    RegisterRequestDTO registerRequest = new RegisterRequestDTO();
    registerRequest.setEmail(email);
    registerRequest.setPassword(password);
    registerRequest.setUsername(email); // Set username to avoid null constraint violation
    authService.register(registerRequest);
    // Promote the user to admin role
    authService.promoteUserRole(email);
    // Save credentials for next steps
    this.email = email;
    this.password = password;
  }

  @Given("The admin provides an incorrect email or password")
  public void the_admin_provides_invalid_credentials() {
    this.email = "wrong.admin@domain.com";
    this.password = "wrongadminpassword";
  }

  @When("The user submits the login request")
  public void the_user_submits_the_login_request() {
    response =
        given()
            .contentType("application/json")
            .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
            .when()
            .post("/auth/login");
  }

  @When("The admin submits the login request")
  public void the_admin_submits_the_login_request() {
    the_user_submits_the_login_request();
  }

  @Then("The system should respond with a 200 OK status")
  public void the_system_should_respond_with_200_ok() {
    response.then().statusCode(200);
  }

  @Then("Return a valid access token")
  public void return_a_valid_access_token() {
    accessToken = response.jsonPath().getString("accessToken");
    assertNotNull(accessToken);
    assertFalse(accessToken.isEmpty());
  }

  @Then("Return a valid admin access token")
  public void return_a_valid_admin_access_token() {
    accessToken = response.jsonPath().getString("accessToken");
    assertNotNull(accessToken);
    assertFalse(accessToken.isEmpty());
    Role role = jwtUtil.extractRole(accessToken);
    assertEquals(Role.ADMIN, role);
  }

  @Then("The system should respond with a 403 Forbidden status")
  public void the_system_should_respond_with_403_forbidden() {
    response.then().statusCode(403);
  }

  @Then("No token should be returned")
  public void no_token_should_be_returned() {
    String body = response.getBody().asString();
    if (body == null || body.isBlank()) {
      // No body, so no token
      return;
    }
    assertNull(response.jsonPath().getString("accessToken"));
  }
}
