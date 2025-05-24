package org.magnuschase.pkchart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/portfolio")
@Tag(name = "Portfolio API", description = "Portfolio management")
public class PortfolioController {
	
}
