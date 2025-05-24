Feature: Scraping external sources for card prices

  As a system
  I want to scrape card prices from eBay and Cardmarket
  So that I can update outdated or missing card prices

  Background:
    Given The card "Latias EX" from set "Paradise Dragona" with rarity "SAR" and condition "CGC 9.5" exists in the database

  Scenario: Scraping price from eBay when local price is outdated
    Given The last price update of "Latias EX" is older than 24 hours
    When The system scrapes the last sold listing prices from eBay
    Then The card's current price should be updated based on average of recently sold listings

  Scenario: Scraping price from Cardmarket as a fallback
    Given eBay scraping fails or returns no valid results
    When The system scrapes Cardmarket for "Latias EX"
    Then The card price should be updated using the Cardmarket price trend for the card

  Scenario: Both sources fail to provide price
    Given eBay and Cardmarket are both unavailable or blocked
    When The system attempts to update the price
    Then The card price should remain unchanged
    And A warning should be logged for administrator review

  Scenario: Preventing re-scraping of recently updated cards
    Given The price of "Latias EX" was updated less than 24 hours ago
    When The system runs the scheduled scraping task
    Then The price should not be fetched again

  Scenario: Administrator triggers manual scraping for a card
    Given The admin is logged in
    And The card "Latias EX" with rarity "SAR" exists in the database
    When The admin manually triggers price scraping for "Latias EX"
    Then The system should scrape and update the price immediately if new data is available

  Scenario: Administrator triggers batch scraping for all cards
    Given The admin is logged in
    When The admin initiates a batch scraping operation for all cards
    Then The system should scrape and update prices for all cards older than 24 hours
