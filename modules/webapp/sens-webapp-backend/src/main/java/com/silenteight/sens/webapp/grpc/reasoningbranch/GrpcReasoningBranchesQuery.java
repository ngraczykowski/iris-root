package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.sens.webapp.backend.reasoningbranch.report.BranchWithFeaturesDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.report.ReasoningBranchesReportQuery;
import com.silenteight.sens.webapp.backend.reasoningbranch.report.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchesQuery;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import io.vavr.control.Try;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.protocol.utils.MoreTimestamps.toInstant;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static io.vavr.control.Try.success;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class GrpcReasoningBranchesQuery implements ReasoningBranchesQuery, ReasoningBranchesReportQuery {

  private final BranchSolutionMapper branchSolutionMapper;
  private final BranchGovernanceBlockingStub branchesStub;

  @Override
  public List<BranchDto> findByTreeIdAndBranchIds(
      long treeId, List<Long> branchIds) {
    log.info(REASONING_BRANCH,
        "Listing Reasoning Branches using gRPC BranchGovernance. treeId={}, branchIds={}",
        treeId, branchIds);

    Try<List<BranchDto>> reasoningBranches = reasoningBranchesOf(
        treeId, rb -> isOneOf(rb, branchIds), this::mapToBranchDto);

    return mapStatusExceptionsToCommunicationException(reasoningBranches)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> success(emptyList())),
                Case($(), () -> failure(exception))))
        .onSuccess(GrpcReasoningBranchesQuery::logListingSuccess)
        .onFailure(GrpcReasoningBranchesQuery::logListingFailure)
        .get();
  }

  @Override
  public List<BranchWithFeaturesDto> findByTreeId(long treeId) {
    log.info(REASONING_BRANCH,
        "Listing Reasoning Branches using gRPC BranchGovernance. treeId={}", treeId);

    return mapStatusExceptionsToCommunicationException(
        reasoningBranchesOf(treeId, rb -> true, this::mapToBranchDtoForReport))
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> failure(new DecisionTreeNotFoundException())),
                Case($(), () -> failure(exception))))
        .onSuccess(GrpcReasoningBranchesQuery::logListingSuccess)
        .onFailure(GrpcReasoningBranchesQuery::logListingFailure)
        .get();
  }

  private <T> Try<List<T>> reasoningBranchesOf(
      long treeId, Predicate<ReasoningBranchSummary> filter,
      Function<ReasoningBranchSummary, T> mapper) {

    return of(() -> branchesStub
        .listReasoningBranches(buildRequest(treeId))
        .getReasoningBranchList()
        .stream()
        .filter(filter)
        .map(mapper)
        .collect(toList()));
  }

  private static ListReasoningBranchesRequest buildRequest(long treeId) {
    return ListReasoningBranchesRequest.newBuilder()
        .setDecisionTreeId(treeId)
        .build();
  }

  private static boolean isOneOf(ReasoningBranchSummary reasoningBranch, List<Long> branchIds) {

    return branchIds.contains(reasoningBranch.getReasoningBranchId().getFeatureVectorId());
  }

  private BranchDto mapToBranchDto(ReasoningBranchSummary reasoningBranch) {
    return BranchDto
        .builder()
        .aiSolution(branchSolutionMapper.map(reasoningBranch.getSolution()))
        .isActive(reasoningBranch.getEnabled())
        .reasoningBranchId(reasoningBranch.getReasoningBranchId().getFeatureVectorId())
        .build();
  }

  private BranchWithFeaturesDto mapToBranchDtoForReport(ReasoningBranchSummary reasoningBranch) {
    return BranchWithFeaturesDto.builder()
        .reasoningBranchId(reasoningBranch.getReasoningBranchId().getFeatureVectorId())
        .updatedAt(toInstant(reasoningBranch.getUpdatedAt()))
        .aiSolution(branchSolutionMapper.map(reasoningBranch.getSolution()))
        .isActive(reasoningBranch.getEnabled())
        .featureValues(reasoningBranch.getFeatureValueList())
        .build();
  }

  private static <T> void logListingSuccess(List<T> reasoningBranches) {
    log.info(REASONING_BRANCH, "Found {} Reasoning Branches.", reasoningBranches.size());
  }

  private static void logListingFailure(Throwable throwable) {
    log.error(REASONING_BRANCH, "Could not list Reasoning Branches", throwable);
  }
}
