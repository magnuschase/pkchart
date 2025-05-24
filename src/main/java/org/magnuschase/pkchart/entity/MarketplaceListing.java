package org.magnuschase.pkchart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.CardCondition;
import org.magnuschase.pkchart.model.MarketplaceListingType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "marketplace_listings")
@Getter
@Setter
public class MarketplaceListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Type: BUY or SELL
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarketplaceListingType type;

    // Who created the listing
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Card being listed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    // Optional: specific condition required or offered
    @Enumerated(EnumType.STRING)
    private CardCondition condition;

    // Offer price or sale price
    @Column(nullable = false)
    private BigDecimal price;

    // Optional contact info (can default to user's email)
    @Column(nullable = true)
    private String contactInfo;

    // Auto timestamp
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean active = true;
}
