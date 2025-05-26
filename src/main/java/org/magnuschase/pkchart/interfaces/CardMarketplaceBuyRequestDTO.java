package org.magnuschase.pkchart.interfaces;

import lombok.Data;
import org.magnuschase.pkchart.model.CardCondition;

@Data
public class CardMarketplaceBuyRequestDTO {
  private long CardId;
  private double Price;
  private CardCondition Condition;
}
