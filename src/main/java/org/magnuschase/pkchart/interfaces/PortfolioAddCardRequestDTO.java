package org.magnuschase.pkchart.interfaces;

import lombok.Data;
import org.magnuschase.pkchart.model.CardCondition;

@Data
public class PortfolioAddCardRequestDTO {
  private CardCondition condition;
  private int quantity;
}
