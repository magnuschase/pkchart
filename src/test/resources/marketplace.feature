@Optional
Feature: Trading cards through marketplace

  As a user
  I want to list my cards for sale or search for ones to buy
  So that I can interact with other collectors

  Background:
    Given "Latias EX" from set "Paradise Dragona" and condition "CGC 9.5" is in the user's portfolio
    And User "ash.ketchum@palettetown.com" is logged in

  Scenario: User lists a card for sale
    When The user creates a sale listing for "Latias EX" from set "Paradise Dragona" and condition "CGC 9.5" with price 625.00 PLN and contact "ash.ketchum@palettetown.com"
    Then The listing should be visible in the marketplace
    And It should include the card name, price, and contact information

  Scenario: User creates a buy offer
    When The user creates a buy offer for "Riolu" from set "Scarlet ex" with rarity "AR" with offer price 50.00 PLN
    Then The buy offer should appear in the marketplace
    And Other users should be able to contact the buyer

  Scenario: User views marketplace listings
    When The user visits the marketplace
    Then All active sale and buy listings should be visible
