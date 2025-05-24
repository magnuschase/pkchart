package org.magnuschase.pkchart.repository;

import java.util.List;
import java.util.Optional;

import org.magnuschase.pkchart.entity.Card;
import org.magnuschase.pkchart.entity.CardSet;
import org.magnuschase.pkchart.entity.User;
import org.magnuschase.pkchart.entity.UserPortfolioEntry;
import org.magnuschase.pkchart.model.CardCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPortfolioEntryRepository extends JpaRepository<CardSet, Long> {
    List<UserPortfolioEntry> findAllByUserId(Long userId);

    Optional<UserPortfolioEntry> findByUserAndCardAndCondition(User user, Card card, CardCondition condition);
}