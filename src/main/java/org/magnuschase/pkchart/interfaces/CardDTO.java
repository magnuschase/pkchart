package org.magnuschase.pkchart.interfaces;

import lombok.Data;
import org.magnuschase.pkchart.model.CardRarity;
import org.magnuschase.pkchart.model.Language;

@Data
public class CardDTO {
  private Long id;
  private String name;
  private CardRarity rarity;
  private Short number;
  private CardSetSimpleDTO set;

  @Data
  public static class CardSetSimpleDTO {
    private Long id;
    private String name;
    private String symbol;
    private Language language;
    private Short size;
  }

  public static CardDTO fromEntity(org.magnuschase.pkchart.entity.Card card) {
    CardDTO dto = new CardDTO();
    dto.setId(card.getId());
    dto.setName(card.getName());
    dto.setRarity(card.getRarity());
    dto.setNumber(card.getNumber());
    if (card.getSet() != null) {
      CardSetSimpleDTO setDto = new CardSetSimpleDTO();
      setDto.setId(card.getSet().getId());
      setDto.setName(card.getSet().getName());
      setDto.setSymbol(card.getSet().getSymbol());
      setDto.setLanguage(card.getSet().getLanguage());
      setDto.setSize(card.getSet().getSize());
      dto.setSet(setDto);
    }
    return dto;
  }
}
