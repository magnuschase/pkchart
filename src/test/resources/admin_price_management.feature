Feature: Admin updates card prices

  As an administrator
  I want to update current prices of cards
  So that users see up-to-date market values

  Background:
    Given Admin "samuel.oak@pokelab.edu" is logged in
    And "Latias EX" from set "Paradise Dragona" and condition "CGC 9.5" currently has a price of 600.00 PLN

  Scenario: Admin updates the price of a card
    When The admin sets a new price for "Latias EX" to 625.00 PLN
    Then The card's latest price should be 625.00 PLN
    And A new price history entry should be stored

  Scenario: Admin updates multiple card prices
    Given The following cards exist:
      | Card Name | Set | Rarity | Condition | Current Price |
      | Latias EX | Paradise Dragona | SAR | CGC 9.5 | 600.00 PLN |
      | Pikachu with Grey Felt Hat | SVP Black Star Promos | Promo | Mint | 900.00 PLN |
    When The admin sets new prices:
      | Card Name | Set | Rarity | Condition | Current Price |
      | Latias EX | Paradise Dragona | SAR | CGC 9.5 | 625.00 PLN |
      | Pikachu with Grey Felt Hat | SVP Black Star Promos | Promo | Mint | 1100.00 PLN |
    Then The prices should be updated accordingly
