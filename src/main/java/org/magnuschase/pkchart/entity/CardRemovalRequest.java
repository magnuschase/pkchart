package org.magnuschase.pkchart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.RequestStatus;

@Entity
@Table(name = "card_removal_requests")
@Getter
@Setter
public class CardRemovalRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "card_id", nullable = false)
  private Card card;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestStatus status;
}
