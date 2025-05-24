Feature: Requesting and approving new or invalid cards

  As a user
  I want to request the addition or removal of cards from the system
  So that the database stays accurate

  Background:
    Given User "ash.ketchum@palettetown.com" is logged in
    And Admin "samuel.oak@pokelab.edu" is logged in

  Scenario: User requests a new card to be added
    Given Set "Team Up" exists
    And "Latias & Latios GX" with rarity "SIR" and set "Team Up" does not exist in the database
    When The user requests the addition of "Latias & Latios GX" with rarity "SIR" from set "Team Up"
    Then The request should be visible to the admin

  Scenario: Admin approves a card addition
    Given Set "Team Up" exists
    And There is a pending request for "Latias & Latios GX" with rarity "SIR" from set "Team Up"
    When The admin approves the request
    Then "Latias & Latios GX" with rarity "SIR" from set "Team Up" should be added to the available card list

  Scenario: User requests removal of an incorrect card
    Given "Fake Pikachu" exists in the database
    When The user requests removal of "Fake Pikachu"
    Then The request should be visible to the admin

  Scenario: Admin rejects an invalid request
   	Given "Fake Pikachu" exists in the database
    And The user requests removal of "Fake Pikachu"
    And The request should be visible to the admin
    When The admin rejects the request
    Then "Fake Pikachu" should not be removed from the database

  Scenario: User requests a new card set to be added
    When The user requests the addition of "Heat Wave Arena" set
    Then The request should be visible to the admin

  Scenario: Admin approves a card set addition
	  Given The user requests the addition of "Heat Wave Arena" set
		And The request should be visible to the admin
    When The admin approves the request for the set
    Then "Heat Wave Arena" should be added to the available sets
