package org.magnuschase.pkchart.interfaces;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PortfolioDTO {
    private BigDecimal totalPrice;
    private List<PortfolioEntryDTO> cards;
}
