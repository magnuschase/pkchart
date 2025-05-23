Feature: Requesting and approving new or invalid cards

  As a user
  I want to request the addition or removal of cards from the system
  So that the database stays accurate

  Background:
    Given Admin "samuel.oak@pokelab.edu" is logged in

  Scenario: User requests a new card to be added
    When The user requests the addition of "Latias & Latios GX" with rarity "SIR" from set "Team Up"
    Then The request should be visible to the admin

  Scenario: Admin approves a card addition
    Given There is a pending request for "Latias & Latios GX" with rarity "SIR" from set "Team Up"
    When The admin approves the request
    Then "Latias & Latios GX" with rarity "SIR" from set "Team Up" should be added to the available card list

  Scenario: User requests removal of an incorrect card
    When The user requests removal of "Fake Pikachu"
    Then The request should be sent to the admin for review

  Scenario: Admin rejects an invalid request
    Given There is a pending request for "Fake Pikachu"
    When The admin rejects the request
    Then "Fake Pikachu" should not be removed from the database
    And The user should be notified about the decision
