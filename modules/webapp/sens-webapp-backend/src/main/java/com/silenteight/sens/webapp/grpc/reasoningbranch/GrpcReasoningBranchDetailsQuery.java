package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest.Builder;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDetailsDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchDetailsQuery;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import io.vavr.control.Try;

import java.util.Optional;

import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.sens.webapp.audit.api.AuditMarker.REASONING_BRANCH;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static io.vavr.control.Try.success;
import static java.util.Optional.empty;

@RequiredArgsConstructor
class GrpcReasoningBranchDetailsQuery implements ReasoningBranchDetailsQuery {

  private final BranchSolutionMapper mapper;
  private final BranchGovernanceBlockingStub branches;
  private final AuditLog auditLog;

  @Override
  public Optional<BranchDetailsDto> findByTreeIdAndBranchId(long treeId, long branchId) {
    auditLog.logInfo(REASONING_BRANCH,
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
        .onSuccess(this::logFetchingSuccess)
        .onFailure(this::logFetchingFailed)
        .get();
  }

  private void logFetchingFailed(Throwable throwable) {
    auditLog.logError(REASONING_BRANCH, "Could not fetch Branch details", throwable);
  }

  private void logFetchingSuccess(Optional<BranchDetailsDto> branchDetails) {
    auditLog.logInfo(REASONING_BRANCH, "Fetched Branch details. {}", branchDetails);
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
