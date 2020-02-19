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
import static io.vavr.API.*;
import static io.vavr.control.Try.*;
import static java.util.Optional.empty;

@RequiredArgsConstructor
@Slf4j
class GrpcReasoningBranchDetailsQuery implements ReasoningBranchDetailsQuery {

  private final BranchGovernanceBlockingStub branches;

  @Override
  public Optional<BranchDetailsDto> findByTreeIdAndBranchId(long treeId, long branchId) {
    log.debug(
        "Using gRPC BranchGovernance stub to fetch reasoning branch details. "
            + "treeId={}, branchId={}", treeId, branchId);
    Try<Optional<BranchDetailsDto>> details =
        of(() -> branches.getReasoningBranch(buildRequest(treeId, branchId)))
            .map(GrpcReasoningBranchDetailsQuery::mapToDetailsDto)
            .map(Optional::of);

    return mapStatusExceptionsToCommunicationException(details)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> success(empty())),
                Case($(), () -> failure(exception)))
        ).get();
  }

  private static BranchDetailsDto mapToDetailsDto(ReasoningBranchResponse grpcResponse) {
    ReasoningBranchSummary reasoningBranch = grpcResponse.getReasoningBranch();

    return BranchDetailsDto.builder()
        .aiSolution(reasoningBranch.getSolution().name())
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
