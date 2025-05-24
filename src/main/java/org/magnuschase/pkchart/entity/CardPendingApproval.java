package org.magnuschase.pkchart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.CardRarity;
import org.magnuschase.pkchart.model.Language;
import org.magnuschase.pkchart.model.RequestStatus;

@Entity
@Table(name = "cards_pending_approval")
@Getter
@Setter
public class CardPendingApproval {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CardRarity rarity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Language language;

  @Column(nullable = false)
  private Short number;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "set_id", nullable = false)
  private CardSet set;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestStatus status;
}
