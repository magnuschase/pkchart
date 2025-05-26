Feature: Trading cards through marketplace

  As a user
  I want to list my cards for sale or search for ones to buy
  So that I can interact with other collectors

  Background:
    Given User "ash.ketchum@palettetown.com" is logged in
    And Admin "samuel.oak@pokelab.edu" is logged in
    And The card "Latias EX" from set "Paradise Dragona" exists
    And The card "Latios EX" from set "Paradise Dragona" exists
    And The user adds "Latias EX" from set "Paradise Dragona" in condition "CGC 9.5" to their portfolio


  Scenario: User lists a card for sale
    When The user creates a sale listing for "Latias EX" from set "Paradise Dragona" and condition "CGC 9.5" with price 625.00 PLN
    Then The listing should be visible in the marketplace
    And It should include the card name, price, and contact information

  Scenario: User creates a buy offer
    When The user creates a buy offer for "Latios EX" from set "Paradise Dragona" and condition "PSA 10" with offer price 200.00 PLN
    Then The buy offer should appear in the marketplace
    And Other users should be able to contact the buyer

  Scenario: User views marketplace listings
    When The user creates a sale listing for "Latias EX" from set "Paradise Dragona" and condition "CGC 9.5" with price 625.00 PLN
    When The user creates a buy offer for "Latios EX" from set "Paradise Dragona" and condition "PSA 10" with offer price 200.00 PLN
    When The user visits the marketplace
    Then All active sale and buy listings should be visible
