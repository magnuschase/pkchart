package org.magnuschase.pkchart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.magnuschase.pkchart.entity.Card;
import org.magnuschase.pkchart.entity.CardSet;
import org.magnuschase.pkchart.interfaces.CardRequestDTO;
import org.magnuschase.pkchart.repository.CardRepository;
import org.magnuschase.pkchart.repository.CardSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
  @Autowired private CardRepository cardRepository;

  @Autowired private CardSetRepository cardSetRepository;

  public Card addCard(CardRequestDTO request) {
    Card card = new Card();
    card.setName(request.getName());
    card.setRarity(request.getRarity());
    CardSet set =
        cardSetRepository
            .findFirstByNameFullText(request.getSetName())
            .orElseThrow(
                () -> new IllegalArgumentException("Set not found: " + request.getSetName()));
    card.setName(request.getName());
    card.setRarity(request.getRarity());
    card.setNumber(request.getNumber());
    card.setSet(set);
    return cardRepository.save(card);
  }

  public Card removeCard(CardRequestDTO request) {
    Optional<Card> card =
        cardRepository.findFirstByNameAndRarityAndSetNameFullText(
            request.getName(), request.getRarity().name(), request.getSetName());
    if (card.isPresent()) {
      cardRepository.delete(card.get());
      return card.get();
    } else {
      throw new IllegalArgumentException("Card not found: " + request.getName());
    }
  }

  public List<Card> getAllCards() {
    List<Card> cards = cardRepository.findAll();
    if (cards.isEmpty()) {
      return new ArrayList<>();
    }
    return cards;
  }
}
