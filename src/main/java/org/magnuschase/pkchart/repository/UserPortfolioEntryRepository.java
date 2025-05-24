package org.magnuschase.pkchart.repository;

import java.util.List;

import org.magnuschase.pkchart.entity.UserPortfolioEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPortfolioEntryRepository extends JpaRepository<UserPortfolioEntry, Long> {
    List<UserPortfolioEntry> findAllByUserId(Long userId);
}