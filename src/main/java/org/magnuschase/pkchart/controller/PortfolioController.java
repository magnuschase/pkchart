package org.magnuschase.pkchart.controller;

import org.magnuschase.pkchart.interfaces.PortfolioAddCardRequestDTO;
import org.magnuschase.pkchart.interfaces.PortfolioDTO;
import org.magnuschase.pkchart.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/portfolio")
@Tag(name = "Portfolio API", description = "Portfolio management")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<PortfolioDTO> getPortfolio(@AuthenticationPrincipal UserDetails userDetails) {
        PortfolioDTO cardApprovalRequest =
                portfolioService.getPortfolioForUser(userDetails);
        return ResponseEntity.ok(cardApprovalRequest);
    }

    @PutMapping("/add/{cardId}")
    public ResponseEntity<PortfolioDTO> addCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cardId,
            @RequestBody PortfolioAddCardRequestDTO portfolioAddCardRequestDTO
            ) {

        PortfolioDTO dto =
                portfolioService.addCard(userDetails, portfolioAddCardRequestDTO, cardId);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/remove/{entryId}")
    public ResponseEntity<PortfolioDTO> removeCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long entryId
    ) {
        PortfolioDTO dto =
                portfolioService.removeCard(userDetails, entryId);
        return ResponseEntity.ok(dto);
    }
}
