package org.magnuschase.pkchart.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.magnuschase.pkchart.entity.CardPrice;
import org.magnuschase.pkchart.interfaces.*;
import org.magnuschase.pkchart.interfaces.CardPriceRequestMultipleDTO.SingleCardPriceRequest;
import org.magnuschase.pkchart.repository.CardPriceRepository;
import org.magnuschase.pkchart.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CardPriceService {
  @Autowired private CardPriceRepository cardPriceRepository;
  @Autowired private CardRepository cardRepository;

  public CardPriceDTO updatePrice(CardPriceRequestDTO request, Long cardId) {
    CardPrice cardPrice = new CardPrice();
    cardPrice.setPrice(BigDecimal.valueOf(request.getPrice()));
    cardPrice.setDate(LocalDate.parse(request.getDate()));
    cardPrice.setCard(
        cardRepository
            .findById(cardId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")));

    CardPrice result = cardPriceRepository.save(cardPrice);

    return CardPriceDTO.fromEntity(result);
  }

  public List<CardPriceDTO> updatePriceMultiple(CardPriceRequestMultipleDTO request) {
    if (request.getPrices() == null || request.getPrices().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No card prices provided");
    }

    List<CardPriceDTO> updatedPrices = new ArrayList<>();
    for (SingleCardPriceRequest priceRequest : request.getPrices()) {
      if (priceRequest.getCardId() == null
          || Double.isNaN(priceRequest.getPrice())
          || priceRequest.getDate() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid card price request");
      }

      CardPriceDTO newPrice = updatePrice(priceRequest, priceRequest.getCardId());
      updatedPrices.add(newPrice);
    }

    return updatedPrices;
  }
}
