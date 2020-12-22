package com.silenteight.serp.governance.branchquery;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest;
import com.silenteight.proto.serp.v1.common.Pagination;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.serp.governance.branchquery.dto.ListReasoningBranchDto;

import com.google.common.collect.Iterables;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ReasoningBranchFinder {

  private final ReasoningBranchQueryRepository repository;

  private static final OrderSpecifier<Long> ORDER_BY_DECISION_TREE_ID =
      QReasoningBranchQuery.reasoningBranchQuery.decisionTreeId.asc();
  private static final OrderSpecifier<Long> ORDER_BY_FEATURE_VECTOR_ID =
      QReasoningBranchQuery.reasoningBranchQuery.featureVectorId.asc();

  @Transactional(readOnly = true)
  public ListReasoningBranchDto findAll(ListReasoningBranchesRequest request) {
    Predicate predicate = buildPredicate(request);

    return request.hasPagination()
           ? getSinglePage(predicate, request.getPagination())
           : getAll(predicate);
  }

  private ListReasoningBranchDto getSinglePage(Predicate predicate, Pagination pagination) {
    int pageIndex = pagination.getPageIndex();
    int pageSize = pagination.getPageSize();
    Pageable pageRequest = PageRequest.of(pageIndex, pageSize, buildSort());

    Iterable<ReasoningBranchQuery> reasoningBranches = repository.findAll(predicate, pageRequest);
    long totalCount = repository.count(predicate);

    return new ListReasoningBranchDto(asSummary(reasoningBranches), totalCount);
  }

  private ListReasoningBranchDto getAll(Predicate predicate) {
    Iterable<ReasoningBranchQuery> reasoningBranches = repository.findAll(predicate, buildSort());
    long totalCount = Iterables.size(reasoningBranches);

    return new ListReasoningBranchDto(asSummary((reasoningBranches)), totalCount);
  }

  private static Predicate buildPredicate(ListReasoningBranchesRequest request) {
    QReasoningBranchQuery branchQuery = QReasoningBranchQuery.reasoningBranchQuery;

    BooleanExpression expression = branchQuery.isNotNull();

    if (isEnablementFilterActive(request)) {
      expression = expression.and(buildEnabledPredicate(request, branchQuery));
    }

    if (isDecisionTreeFilterActive(request)) {
      expression = expression.and(buildDecisionTreePredicate(request, branchQuery));
    }

    if (isBranchSolutionFilterActive(request)) {
      expression = expression.and(buildSolutionPredicate(request, branchQuery));
    }

    return expression;
  }

  private static boolean isEnablementFilterActive(ListReasoningBranchesRequest request) {
    return request.hasEnablementFilter();
  }

  @Nonnull
  private static BooleanBuilder buildSolutionPredicate(
      ListReasoningBranchesRequest request, QReasoningBranchQuery branchQuery) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    request
        .getBranchSolutionFilter()
        .getSolutionsList()
        .forEach(s -> booleanBuilder.or(branchQuery.solution.eq(s)));
    return booleanBuilder;
  }

  private static BooleanExpression buildDecisionTreePredicate(
      ListReasoningBranchesRequest request, QReasoningBranchQuery branchQuery) {
    return branchQuery.decisionTreeId.in(request.getDecisionTreeFilter().getDecisionTreeIdsList());
  }

  private static BooleanExpression buildEnabledPredicate(
      ListReasoningBranchesRequest request, QReasoningBranchQuery branchQuery) {
    return branchQuery.enabled.eq(request.getEnablementFilter().getEnabled());
  }

  private static boolean isDecisionTreeFilterActive(ListReasoningBranchesRequest request) {
    return !request.getDecisionTreeFilter().getDecisionTreeIdsList().isEmpty();
  }

  private static boolean isBranchSolutionFilterActive(ListReasoningBranchesRequest request) {
    return !request.getBranchSolutionFilter().getSolutionsList().isEmpty();
  }

  private static Sort buildSort() {
    return QSort.by(ORDER_BY_DECISION_TREE_ID).and(ORDER_BY_FEATURE_VECTOR_ID);
  }

  private static List<ReasoningBranchSummary> asSummary(
      Iterable<ReasoningBranchQuery> reasoningBranchQuery) {
    return StreamSupport
        .stream(reasoningBranchQuery.spliterator(), false)
        .map(ReasoningBranchQuery::summarize)
        .collect(toList());
  }

  @Transactional(readOnly = true)
  public List<ReasoningBranchSummary> findAllByDecisionTreeId(long decisionTreeId) {

    QReasoningBranchQuery branchQuery = QReasoningBranchQuery.reasoningBranchQuery;
    BooleanExpression expression = branchQuery.decisionTreeId.eq(decisionTreeId);

    return StreamSupport
        .stream(
            repository.findAll(expression).spliterator(),
            false)
        .map(ReasoningBranchQuery::summarize)
        .collect(toList());
  }

  @Transactional(readOnly = true)
  public ReasoningBranchSummary getByDecisionTreeIdAndFeatureVectorId(
      long decisionTreeId,
      long featureVectorId) {

    QReasoningBranchQuery branchQuery = QReasoningBranchQuery.reasoningBranchQuery;
    BooleanExpression expression = branchQuery.featureVectorId.eq(featureVectorId);
    expression = expression.and(branchQuery.decisionTreeId.eq(decisionTreeId));

    return repository.findOne(expression)
        .map(ReasoningBranchQuery::summarize)
        .orElseThrow(() -> new EntityNotFoundException(
            String.format(
                "ReasoningBranch was not found for given decisionTreeId %s and featureVectorId %s",
                decisionTreeId,
                featureVectorId)));

  }
}
