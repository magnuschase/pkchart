package org.magnuschase.pkchart.repository;

import java.util.Optional;
import org.magnuschase.pkchart.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPriceRepository extends JpaRepository<CardPrice, Long> {
  Optional<CardPrice> findFirstByCardOrderByDateDesc(Card card);
}
