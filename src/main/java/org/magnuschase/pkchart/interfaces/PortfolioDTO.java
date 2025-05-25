package org.magnuschase.pkchart.interfaces;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class PortfolioDTO {
  private BigDecimal totalPrice;
  private List<PortfolioEntryDTO> cards;
}
