package org.magnuschase.pkchart.interfaces;

import lombok.Data;
import org.magnuschase.pkchart.interfaces.CardDTO.CardSetSimpleDTO;
import org.magnuschase.pkchart.model.CardRarity;
import org.magnuschase.pkchart.model.Language;
import org.magnuschase.pkchart.model.RequestStatus;

@Data
public class CardPendingApprovalDTO {
  private Long id;
  private String name;
  private CardRarity rarity;
  private Language language;
  private Short number;
  private CardDTO.CardSetSimpleDTO set;
  private RequestStatus status;

  public static CardPendingApprovalDTO fromEntity(
      org.magnuschase.pkchart.entity.CardPendingApproval entity) {
    CardPendingApprovalDTO dto = new CardPendingApprovalDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setRarity(entity.getRarity());
    dto.setLanguage(entity.getLanguage());
    dto.setNumber(entity.getNumber());
    if (entity.getSet() != null) {
      CardSetSimpleDTO setDto = new CardSetSimpleDTO();
      setDto.setId(entity.getSet().getId());
      setDto.setName(entity.getSet().getName());
      setDto.setSymbol(entity.getSet().getSymbol());
      setDto.setLanguage(entity.getSet().getLanguage());
      setDto.setSize(entity.getSet().getSize());
      dto.setSet(setDto);
    }
    dto.setStatus(entity.getStatus());
    return dto;
  }
}
