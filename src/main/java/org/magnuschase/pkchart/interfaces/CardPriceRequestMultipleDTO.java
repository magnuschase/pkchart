package org.magnuschase.pkchart.interfaces;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class CardPriceRequestMultipleDTO {
  private List<SingleCardPriceRequest> prices;

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class SingleCardPriceRequest extends CardPriceRequestDTO {
    private Long cardId;
  }
}
