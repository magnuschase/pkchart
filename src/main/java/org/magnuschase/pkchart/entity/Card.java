package org.magnuschase.pkchart.entity;

import org.magnuschase.pkchart.model.CardRarity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

		@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardRarity rarity;

		@Column(nullable = false)
    private Short number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    private CardSet set;
}
