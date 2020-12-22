package com.silenteight.serp.governance.bulkchange;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface BulkBranchChangeRepository extends Repository<BulkBranchChange, Long>,
    QuerydslPredicateExecutor<BulkBranchChange> {

  void save(BulkBranchChange change);

  Optional<BulkBranchChange> findByBulkBranchChangeId(UUID bulkBranchChangeId);


}
