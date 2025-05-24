package org.magnuschase.pkchart.interfaces;

import lombok.Data;
import org.magnuschase.pkchart.model.RequestStatus;

@Data
public class CardRemovalRequestDTO {
  private Long id;
  private CardDTO card;
  private String reason;
  private RequestStatus status;

  public static CardRemovalRequestDTO fromEntity(
      org.magnuschase.pkchart.entity.CardRemovalRequest entity) {
    CardRemovalRequestDTO dto = new CardRemovalRequestDTO();
    dto.setId(entity.getId());
    if (entity.getCard() != null) {
      dto.setCard(CardDTO.fromEntity(entity.getCard()));
    }
    dto.setReason(entity.getReason());
    dto.setStatus(entity.getStatus());
    return dto;
  }
}
