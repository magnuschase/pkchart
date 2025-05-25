package org.magnuschase.pkchart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marketplace")
@Tag(name = "Marketplace API", description = "Buy and sell cards in the marketplace")
public class MarketplaceController {}
