package org.magnuschase.pkchart.repository;

import java.util.Optional;
import org.magnuschase.pkchart.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {
  Optional<Card> findByNameAndRarityAndSet_Name(String name, String rarity, String setName);

  @Query(
      value =
          "SELECT c.* FROM cards c JOIN sets s ON c.set_id = s.id WHERE to_tsvector('simple', c.name) @@ plainto_tsquery('simple', :name) AND to_tsvector('simple', s.name) @@ plainto_tsquery('simple', :setName) AND c.rarity = :rarity LIMIT 1",
      nativeQuery = true)
  Optional<Card> findFirstByNameAndRarityAndSetNameFullText(
      @Param("name") String name, @Param("rarity") String rarity, @Param("setName") String setName);
}
