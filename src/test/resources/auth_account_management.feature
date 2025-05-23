Feature: User registration and account management

  As a new user
  I want to register an account
  So that I can start using the application

  Scenario: User registers successfully
    Given The user provides a valid email "misty.waterflower@cerulean.com", username "misty" and password "psyduck321"
    When The user submits the registration form
    Then The system should respond with a 201 Created status
    And The user account should be created

  Scenario: User attempts to register with an existing email
    Given The email "ash.ketchum@palettetown.com" is already in use
    When The user submits a registration form with this email
    Then The system should respond with a 409 Conflict status
    And Inform the user that the email is already registered

  Scenario: User updates account information
    Given The user is logged in
    When The user updates their display name to "Ash Ketchum" and contact email to "ash.ketchum@pokeworld.com"
    Then The account information should be updated successfully

  Scenario: User changes password
    Given The user is logged in and provides the current password "pikachu123"
    When The user submits a new password "pikachu456"
    Then The password should be updated
    And The user should be required to log in again with the new password