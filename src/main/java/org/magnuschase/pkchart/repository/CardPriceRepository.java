package org.magnuschase.pkchart.repository;

import org.magnuschase.pkchart.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPriceRepository extends JpaRepository<CardPrice, Long> {
    Optional<CardPrice> findFirstByCardOrderByDateDesc(Card card);
}