package org.magnuschase.pkchart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.CardCondition;

@Entity
@Table(name = "user_portfolio_entries")
@Getter
@Setter
public class UserPortfolioEntry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "card_id", nullable = false)
  private Card card;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CardCondition condition;

  @Column(nullable = false)
  private int quantity;
}
