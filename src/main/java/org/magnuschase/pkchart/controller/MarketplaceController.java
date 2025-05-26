package org.magnuschase.pkchart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.magnuschase.pkchart.interfaces.*;
import org.magnuschase.pkchart.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marketplace")
@Tag(name = "Marketplace API", description = "Buy and sell cards in the marketplace")
public class MarketplaceController {
    
    @Autowired
    MarketplaceService marketplaceService;

    @PutMapping("/buy")
    public ResponseEntity<MarketplaceListingDTO> buyCard(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody CardMarketplaceBuyRequestDTO request) {
        MarketplaceListingDTO buyCardDTO = marketplaceService.buyCard(userDetails, request);
        return ResponseEntity.ok(buyCardDTO);
    }

    @PutMapping("/sell")
    public ResponseEntity<MarketplaceListingDTO> sellCard(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody CardMarketplaceSellRequestDTO request) {
        MarketplaceListingDTO sellCardDTO = marketplaceService.sellCard(userDetails, request);
        return ResponseEntity.ok(sellCardDTO);
    }

    @GetMapping
    public ResponseEntity<List<MarketplaceListingDTO>> getAllListings() {
        List<MarketplaceListingDTO> listings = marketplaceService.getAllListings();
        return ResponseEntity.ok(listings);
    }
}
