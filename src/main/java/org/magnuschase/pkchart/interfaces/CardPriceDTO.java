package org.magnuschase.pkchart.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import org.magnuschase.pkchart.entity.CardPrice;

@Data
public class CardPriceDTO {
  private Long id;
  private LocalDate date;
  private BigDecimal price;
  private CardDTO card;

  public static CardPriceDTO fromEntity(CardPrice cardPrice) {
    CardPriceDTO cardPriceDTO = new CardPriceDTO();
    cardPriceDTO.setId(cardPrice.getId());
    cardPriceDTO.setDate(cardPrice.getDate());
    cardPriceDTO.setPrice(cardPrice.getPrice());
    cardPriceDTO.setCard(CardDTO.fromEntity(cardPrice.getCard()));

    return cardPriceDTO;
  }
}
