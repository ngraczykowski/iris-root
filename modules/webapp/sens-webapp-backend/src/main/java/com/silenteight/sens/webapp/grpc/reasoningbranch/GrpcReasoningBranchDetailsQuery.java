package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest.Builder;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDetailsDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchDetailsQuery;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import io.vavr.control.Try;

import java.util.Optional;

import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static io.vavr.control.Try.success;
import static java.util.Optional.empty;

@RequiredArgsConstructor
@Slf4j
class GrpcReasoningBranchDetailsQuery implements ReasoningBranchDetailsQuery {

  private final BranchSolutionMapper mapper;
  private final BranchGovernanceBlockingStub branches;

  @Override
  public Optional<BranchDetailsDto> findByTreeIdAndBranchId(long treeId, long branchId) {
    log.debug(REASONING_BRANCH,
        "Fetching Reasoning Branch details using gRPC BranchGovernance. treeId={}, branchId={}",
        treeId, branchId);

    Try<Optional<BranchDetailsDto>> details =
        of(() -> branches.getReasoningBranch(buildRequest(treeId, branchId)))
            .map(this::mapToDetailsDto)
            .map(Optional::of);

    return mapStatusExceptionsToCommunicationException(details)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> success(empty())),
                Case($(), () -> failure(exception)))
        )
        .onSuccess(GrpcReasoningBranchDetailsQuery::logFetchingSuccess)
        .onFailure(GrpcReasoningBranchDetailsQuery::logFetchingFailed)
        .get();
  }

  private static void logFetchingFailed(Throwable throwable) {
    log.error(REASONING_BRANCH, "Could not fetch Branch details", throwable);
  }

  private static void logFetchingSuccess(Optional<BranchDetailsDto> branchDetails) {
    log.info(REASONING_BRANCH, "Fetched Branch details. {}", branchDetails);
  }

  private BranchDetailsDto mapToDetailsDto(ReasoningBranchResponse grpcResponse) {
    ReasoningBranchSummary reasoningBranch = grpcResponse.getReasoningBranch();

    return BranchDetailsDto.builder()
        .aiSolution(mapper.map(reasoningBranch.getSolution()))
        .isActive(reasoningBranch.getEnabled())
        .reasoningBranchId(reasoningBranch.getReasoningBranchId().getFeatureVectorId())
        .build();
  }

  private static GetReasoningBranchRequest buildRequest(long treeId, long branchId) {
    Builder builder = GetReasoningBranchRequest.newBuilder();

    builder.getReasoningBranchIdBuilder()
        .setFeatureVectorId(branchId)
        .setDecisionTreeId(treeId);

    return builder.build();
  }
}
