package com.silenteight.serp.governance.bulkchange;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryBulkBranchChangeRepository implements BulkBranchChangeRepository {

  private final Map<UUID, BulkBranchChange> repoMap = new HashMap<>();

  @Override
  public void save(BulkBranchChange change) {
    repoMap.put(change.getBulkBranchChangeId(), change);
  }

  @Override
  public Optional<BulkBranchChange> findByBulkBranchChangeId(UUID bulkBranchChangeId) {
    return Optional.ofNullable(repoMap.get(bulkBranchChangeId));
  }

  @Override
  public Optional<BulkBranchChange> findOne(Predicate predicate) {
    return Optional.empty();
  }

  @Override
  public Iterable<BulkBranchChange> findAll(Predicate predicate) {
    return repoMap.values();
  }

  @Override
  public Iterable<BulkBranchChange> findAll(
      Predicate predicate, Sort sort) {
    return null;
  }

  @Override
  public Iterable<BulkBranchChange> findAll(
      Predicate predicate, OrderSpecifier<?>... orders) {
    return null;
  }

  @Override
  public Iterable<BulkBranchChange> findAll(OrderSpecifier<?>... orders) {
    return null;
  }

  @Override
  public Page<BulkBranchChange> findAll(
      Predicate predicate, Pageable pageable) {
    return null;
  }

  @Override
  public long count(Predicate predicate) {
    return 0;
  }

  @Override
  public boolean exists(Predicate predicate) {
    return false;
  }
}
