Feature: Managing personal card portfolio

  As a registered user
  I want to manage my collection of Pokemon cards
  So that I can track their value over time

  Background:
    Given User "ash.ketchum@palettetown.com" is logged in
    And Admin "samuel.oak@pokelab.edu" is logged in
    And The card "Latias EX" from set "Paradise Dragona" exists
    And The card "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos" exists
    And Current price of "Latias EX" from set "Paradise Dragona" is 600.00 PLN
    And Current price of "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos" is 950.00 PLN

  Scenario: User adds a card to their portfolio
    When The user adds "Latias EX" from set "Paradise Dragona" in condition "CGC 9.5" to their portfolio
    Then "Latias EX" from set "Paradise Dragona" should appear in the user's portfolio
    And The system should display its current price: 600.00 PLN

  Scenario: User removes a card from their portfolio
    Given "Latias EX" from set "Paradise Dragona" is in the user's portfolio
    When The user removes "Latias EX" from set "Paradise Dragona" from their portfolio
    Then The card should no longer appear in the portfolio

  Scenario: User views portfolio statistics
    Given The user's portfolio contains "Latias EX" from set "Paradise Dragona"
    And The user's portfolio contains "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos"
    When The user views their portfolio statistics
    Then The system should show total portfolio value
    And All cards should be listed in the portfolio
