package org.magnuschase.pkchart.repository;

import org.magnuschase.pkchart.entity.CardSetPendingApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardSetPendingApprovalRepository
    extends JpaRepository<CardSetPendingApproval, Long> {}
