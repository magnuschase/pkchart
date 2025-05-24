package org.magnuschase.pkchart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.magnuschase.pkchart.entity.*;
import org.magnuschase.pkchart.interfaces.*;
import org.magnuschase.pkchart.service.CardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/set")
@Tag(name = "Card Sets / Expansions API")
public class CardSetController {
  @Autowired private CardSetService cardSetService;

  @PutMapping("/add")
  public ResponseEntity<CardSetDTO> addSet(@RequestBody SetRequestDTO request) {
    CardSet set = cardSetService.addSet(request);
    return ResponseEntity.ok(CardSetDTO.fromEntity(set));
  }

  @DeleteMapping("/remove")
  public ResponseEntity<CardSetDTO> removeSet(@RequestBody SetRequestDTO request) {
    CardSet set = cardSetService.removeSet(request);
    return ResponseEntity.ok(CardSetDTO.fromEntity(set));
  }

  @GetMapping("/all")
  public ResponseEntity<List<CardSetDTO>> getAllSets() {
    List<CardSetDTO> dtos =
        cardSetService.getAllSets().stream().map(CardSetDTO::fromEntity).toList();
    return ResponseEntity.ok(dtos);
  }
}
