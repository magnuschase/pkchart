package org.magnuschase.pkchart.interfaces;

import lombok.Data;

@Data
public class CardPriceRequestDTO {
  private String date;
  private double price;
}
