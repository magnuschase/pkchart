package org.magnuschase.pkchart.repository;

import java.util.Optional;
import org.magnuschase.pkchart.entity.CardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardSetRepository extends JpaRepository<CardSet, Long> {
  Optional<CardSet> findByName(String name);

  @Query(
      value =
          "SELECT * FROM sets WHERE to_tsvector('simple', name) @@ plainto_tsquery('simple', :name) LIMIT 1",
      nativeQuery = true)
  Optional<CardSet> findFirstByNameFullText(@Param("name") String name);

  @Query(
      value =
          "SELECT * FROM sets WHERE to_tsvector(:language, name) @@ plainto_tsquery(:language, :name) LIMIT 1",
      nativeQuery = true)
  Optional<CardSet> findFirstByNameFullTextAndLanguage(
      @Param("name") String name, @Param("language") String language);
}
