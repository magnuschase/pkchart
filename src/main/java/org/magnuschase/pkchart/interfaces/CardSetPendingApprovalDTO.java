package org.magnuschase.pkchart.interfaces;

import lombok.Data;
import org.magnuschase.pkchart.model.Language;
import org.magnuschase.pkchart.model.RequestStatus;

@Data
public class CardSetPendingApprovalDTO {
  private Long id;
  private String name;
  private String symbol;
  private Language language;
  private Short size;
  private RequestStatus status;

  public static CardSetPendingApprovalDTO fromEntity(
      org.magnuschase.pkchart.entity.CardSetPendingApproval entity) {
    CardSetPendingApprovalDTO dto = new CardSetPendingApprovalDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setSymbol(entity.getSymbol());
    dto.setLanguage(entity.getLanguage());
    dto.setSize(entity.getSize());
    dto.setStatus(entity.getStatus());
    return dto;
  }
}
