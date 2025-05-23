package org.magnuschase.pkchart.entity;

import java.util.List;

import org.magnuschase.pkchart.model.Language;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sets")
@Getter
@Setter
public class CardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Column(nullable = false)
    private Short size;

    @OneToMany(mappedBy = "set")
    private List<Card> cards;
}
