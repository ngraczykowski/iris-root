package com.silenteight.serp.governance.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.StateFilter;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class BulkBranchChangeFinder {

  private final BulkBranchChangeRepository repository;

  Iterable<BulkBranchChange> findAll(ListBulkBranchChangesRequest request) {
    return repository.findAll(buildPredicate(request));
  }

  private static Predicate buildPredicate(ListBulkBranchChangesRequest request) {
    var predicate = new BooleanBuilder();
    var query = QBulkBranchChange.bulkBranchChange;

    if (request.hasReasoningBranchFilter())
      predicate.and(getReasoningBranchIdFilter(request, query));

    if (request.hasStateFilter())
      predicate.and(getStateFilter(request, query));

    if (request.hasBulkBranchChangeIdsFilter())
      predicate.and(getBulkBranchChangeIdsFilter(request, query));

    return predicate;
  }

  private static Predicate getReasoningBranchIdFilter(
      ListBulkBranchChangesRequest request, QBulkBranchChange query) {

    var booleanBuilder = new BooleanBuilder();

    request.getReasoningBranchFilter().getReasoningBranchIdsList()
        .forEach(branchId -> {
          long treeId = branchId.getDecisionTreeId();
          long vectorId = branchId.getFeatureVectorId();
          BooleanExpression expression =
              query.reasoningBranchIds.contains(new ReasoningBranchIdToChange(treeId, vectorId));
          booleanBuilder.or(expression);
        });

    return booleanBuilder;
  }

  private static BooleanExpression getStateFilter(
      ListBulkBranchChangesRequest request, QBulkBranchChange query) {
    return query.state.in(getStatesToBeFiltered(request.getStateFilter()));
  }

  private static List<State> getStatesToBeFiltered(StateFilter stateFilter) {
    return stateFilter.getStatesList().stream()
        .map(BulkBranchChangeStateMapper::mapToJava)
        .collect(Collectors.toList());
  }

  private static BooleanExpression getBulkBranchChangeIdsFilter(
      ListBulkBranchChangesRequest request, QBulkBranchChange query) {
    return query.bulkBranchChangeId.in(getBulkBranchChangeIdsList(request));
  }

  private static List<UUID> getBulkBranchChangeIdsList(ListBulkBranchChangesRequest request) {
    return request
        .getBulkBranchChangeIdsFilter()
        .getBulkBranchChangeIdsList()
        .stream()
        .map(Uuids::toJavaUuid)
        .collect(Collectors.toList());
  }

}
