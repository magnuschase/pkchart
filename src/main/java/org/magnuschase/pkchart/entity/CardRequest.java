package org.magnuschase.pkchart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.RequestType;
import org.magnuschase.pkchart.model.RequestStatus;

@Entity
@Table(name = "card_requests")
@Getter
@Setter
public class CardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
}
