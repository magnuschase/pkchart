Feature: Managing personal card portfolio

  As a registered user
  I want to manage my collection of Pokemon cards
  So that I can track their value over time

  Background:
    Given The card "Latias EX" from set "Paradise Dragona" with rarity "SAR" in condition "CGC 9.5" exists with a current price of 600.00 PLN
    And User "ash.ketchum@palettetown.com" is logged in

  Scenario: User adds a card to their portfolio
    When The user adds "Latias EX" from set "Paradise Dragona" with rarity "SAR" in condition "CGC 9.5" to their portfolio
    Then "Latias EX" with rarity "SAR" from set "Paradise Dragona" should appear in the user's portfolio
    And The system should display its current price: 600.00 PLN

  Scenario: User removes a card from their portfolio
    Given "Latias EX" with rarity "SAR" from set "Paradise Dragona" is in the user's portfolio
    When The user removes "Latias EX" with rarity "SAR" from set "Paradise Dragona" from their portfolio
    Then The card should no longer appear in the portfolio

  Scenario: User views portfolio statistics
    Given The user's portfolio contains "Latias EX" with rarity "SAR" from set "Paradise Dragona" and "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos"
    When The user views their portfolio statistics
    Then The system should show total portfolio value
    And Show historical prices for each card
