package org.magnuschase.pkchart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.magnuschase.pkchart.entity.*;
import org.magnuschase.pkchart.interfaces.*;
import org.magnuschase.pkchart.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@Tag(name = "Cards API")
public class CardController {
  @Autowired private CardService cardService;

  @PutMapping("/add")
  public ResponseEntity<CardDTO> addCard(@RequestBody CardRequestDTO request) {
    Card card = cardService.addCard(request);
    return ResponseEntity.ok(CardDTO.fromEntity(card));
  }

  @DeleteMapping("/remove")
  public ResponseEntity<CardDTO> removeCard(@RequestBody CardRequestDTO request) {
    Card card = cardService.removeCard(request);
    return ResponseEntity.ok(CardDTO.fromEntity(card));
  }

  @GetMapping("/all")
  public ResponseEntity<List<CardDTO>> getAllCards() {
    List<CardDTO> dtos = cardService.getAllCards().stream().map(CardDTO::fromEntity).toList();
    return ResponseEntity.ok(dtos);
  }
}
