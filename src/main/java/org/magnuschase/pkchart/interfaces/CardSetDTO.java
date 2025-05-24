package org.magnuschase.pkchart.interfaces;

import java.util.List;
import lombok.Data;
import org.magnuschase.pkchart.model.CardRarity;
import org.magnuschase.pkchart.model.Language;

@Data
public class CardSetDTO {
  private Long id;
  private String name;
  private String symbol;
  private Language language;
  private Short size;
  private List<CardSimpleDTO> cards;

  @Data
  public static class CardSimpleDTO {
    private Long id;
    private String name;
    private CardRarity rarity;
    private Short number;
  }

  public static CardSetDTO fromEntity(org.magnuschase.pkchart.entity.CardSet set) {
    CardSetDTO dto = new CardSetDTO();
    dto.setId(set.getId());
    dto.setName(set.getName());
    dto.setSymbol(set.getSymbol());
    dto.setLanguage(set.getLanguage());
    dto.setSize(set.getSize());
    if (set.getCards() != null) {
      dto.setCards(
          set.getCards().stream()
              .map(
                  card -> {
                    CardSimpleDTO c = new CardSimpleDTO();
                    c.setId(card.getId());
                    c.setName(card.getName());
                    c.setRarity(card.getRarity());
                    c.setNumber(card.getNumber());
                    return c;
                  })
              .toList());
    }
    return dto;
  }
}
