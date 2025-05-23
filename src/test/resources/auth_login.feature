Feature: Logging in and obtaining access token

  As a user
  I want to log in to the system
  So that I can perform authenticated actions using my access token

  Scenario: Successful login with valid credentials
    Given The user provides a valid email "wolfey.glick@incineroar.com" and password "ilovebigalolancats"
    When The user submits the login request
    Then The system should respond with a 200 OK status
    And Return a valid access token

  Scenario: Failed login with invalid credentials
    Given The user provides an incorrect email or password
    When The user submits the login request
    Then The system should respond with a 403 Forbidden status
    And No token should be returned

  Scenario: Administrator logs in successfully
    Given The admin provides a valid email "samuel.oak@pokelab.edu" and password "pokelab456"
    When The admin submits the login request
    Then The system should respond with a 200 OK status
    And Return a valid admin access token

  Scenario: Administrator login fails with wrong credentials
    Given The admin provides an incorrect email or password
    When The admin submits the login request
    Then The system should respond with a 403 Forbidden status
    And No token should be returned
