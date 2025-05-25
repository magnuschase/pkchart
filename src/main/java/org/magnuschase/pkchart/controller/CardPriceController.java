package org.magnuschase.pkchart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.magnuschase.pkchart.interfaces.CardPriceDTO;
import org.magnuschase.pkchart.interfaces.CardPriceRequestDTO;
import org.magnuschase.pkchart.interfaces.CardPriceRequestMultipleDTO;
import org.magnuschase.pkchart.service.CardPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prices")
@Tag(name = "Price API", description = "Card Price management")
public class CardPriceController {

  @Autowired private CardPriceService cardPriceService;

  @PutMapping("/update/{cardId}")
  public ResponseEntity<CardPriceDTO> updatePrice(
      @RequestBody CardPriceRequestDTO request, @PathVariable Long cardId) {
    CardPriceDTO updated = cardPriceService.updatePrice(request, cardId);
    return ResponseEntity.ok(updated);
  }

  @PutMapping("/update-multiple")
  public ResponseEntity<List<CardPriceDTO>> updatePriceMultiple(
      @RequestBody CardPriceRequestMultipleDTO request) {
    List<CardPriceDTO> updated = cardPriceService.updatePriceMultiple(request);
    return ResponseEntity.ok(updated);
  }
}
