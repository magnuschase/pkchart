package org.magnuschase.pkchart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/prices")
@Tag(name = "Admin - Price API", description = "Change prices manually")
public class AdminCardPriceController {
	
}
