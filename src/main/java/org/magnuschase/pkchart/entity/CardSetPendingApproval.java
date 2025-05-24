package org.magnuschase.pkchart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.Language;
import org.magnuschase.pkchart.model.RequestStatus;

@Entity
@Table(name = "card_sets_pending_approval")
@Getter
@Setter
public class CardSetPendingApproval {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false, unique = true)
  private String symbol;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Language language;

  @Column(nullable = false)
  private Short size;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestStatus status;
}
