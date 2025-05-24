package org.magnuschase.pkchart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.CardCondition;
import org.magnuschase.pkchart.model.PriceSource;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "card_prices", indexes = {
  @Index(columnList = "card_id, condition, date", name = "idx_card_condition_date")
})
@Getter
@Setter
public class CardPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "card_id", nullable = false)
  private Card card;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CardCondition condition;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PriceSource source;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;
}
