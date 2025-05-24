package org.magnuschase.pkchart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.magnuschase.pkchart.entity.CardSet;
import org.magnuschase.pkchart.interfaces.SetRequestDTO;
import org.magnuschase.pkchart.repository.CardSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardSetService {
  @Autowired private CardSetRepository cardSetRepository;

  public CardSet addSet(SetRequestDTO request) {
    CardSet set = new CardSet();
    set.setName(request.getName());
    set.setSymbol(request.getSymbol());
    set.setLanguage(request.getLanguage());
    set.setSize(request.getSize());
    return cardSetRepository.save(set);
  }

  public CardSet removeSet(SetRequestDTO request) {
    Optional<CardSet> set =
        cardSetRepository.findFirstByNameFullTextAndLanguage(
            request.getName(), request.getLanguage().name());
    if (set.isPresent()) {
      cardSetRepository.delete(set.get());
      return set.get();
    } else {
      throw new IllegalArgumentException("Set not found: " + request.getName());
    }
  }

  public List<CardSet> getAllSets() {
    List<CardSet> sets = cardSetRepository.findAll();
    if (sets.isEmpty()) {
      return new ArrayList<>();
    }
    return sets;
  }
}
