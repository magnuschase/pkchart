package org.magnuschase.pkchart.interfaces;

import lombok.Data;
import org.magnuschase.pkchart.entity.UserPortfolioEntry;
import org.magnuschase.pkchart.model.CardCondition;

import java.math.BigDecimal;

@Data
public class PortfolioEntryDTO {
    private Long id;
    private CardDTO card;
    private CardCondition condition;
    private int quantity;
    private BigDecimal currentPrice;

    public static PortfolioEntryDTO fromEntity(UserPortfolioEntry entry, BigDecimal currentPrice) {
        PortfolioEntryDTO dto = new PortfolioEntryDTO();
        dto.setId(entry.getId());
        dto.setCard(CardDTO.fromEntity(entry.getCard()));
        dto.setCondition(entry.getCondition());
        dto.setQuantity(entry.getQuantity());
        dto.setCurrentPrice(currentPrice);
        return dto;
    }
}
