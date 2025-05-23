package org.magnuschase.pkchart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.magnuschase.pkchart.model.Language;

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

  @Column(nullable = false, unique = true)
  private String symbol;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Language language;

  @Column(nullable = true)
  private Short size;

  @OneToMany(mappedBy = "set")
  @JsonIgnore
  private List<Card> cards;
}
