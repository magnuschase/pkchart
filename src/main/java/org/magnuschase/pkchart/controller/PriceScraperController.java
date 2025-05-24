package org.magnuschase.pkchart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/prices/scrape")
@Tag(name = "Admin - Price Scraper API", description = "Scrape prices from external sources")
public class PriceScraperController {
	
}
