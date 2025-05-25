Feature: Admin updates card prices

  As an administrator
  I want to update current prices of cards
  So that users see up-to-date market values

  Background:
    Given Admin "samuel.oak@pokelab.edu" is logged in
		And The card "Latias EX" from set "Paradise Dragona" exists
    And Current price of "Latias EX" from set "Paradise Dragona" is 600.00 PLN

  Scenario: Admin updates the price of a card
    When The admin sets a new price for "Latias EX" from set "Paradise Dragona" to 625.00 PLN
		Then Current price of "Latias EX" from set "Paradise Dragona" is 625.00 PLN

  Scenario: Admin updates multiple card prices
    Given The card "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos" exists
		And Current price of "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos" is 950.00 PLN
		When The admin sets a new price for "Latias EX" from set "Paradise Dragona" to 625.00 PLN and "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos" to 1100.00 PLN
		Then Current price of "Latias EX" from set "Paradise Dragona" is 625.00 PLN
		And Current price of "Pikachu with Grey Felt Hat" from set "SVP Black Star Promos" is 1100.00 PLN
