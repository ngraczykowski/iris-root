package com.silenteight.sens.webapp.grpc.circuitbreaker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.DiscrepancyCircuitBreakerGrpc.DiscrepancyCircuitBreakerBlockingStub;
import com.silenteight.proto.serp.v1.api.DiscrepantBranch;
import com.silenteight.proto.serp.v1.api.DiscrepantBranchId;
import com.silenteight.sens.webapp.backend.circuitbreaker.DiscrepancyCircuitBreakerQuery;
import com.silenteight.sens.webapp.backend.circuitbreaker.DiscrepantBranchDto;
import com.silenteight.sens.webapp.backend.circuitbreaker.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import com.google.protobuf.Empty;
import io.vavr.control.Try;

import java.util.List;

import static com.silenteight.protocol.utils.MoreTimestamps.toInstant;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class GrpcDiscrepancyCircuitBreakerQuery implements DiscrepancyCircuitBreakerQuery {

  @NonNull
  private final DiscrepancyCircuitBreakerBlockingStub discrepancyBlockingStub;

  @Override
  public List<DiscrepantBranchDto> listDiscrepantBranches() {
    Try<List<DiscrepantBranchDto>> discrepantBranches =
        of(() -> discrepancyBlockingStub
            .listDiscrepantBranches(Empty.newBuilder().build())
            .getDiscrepantBranchesList()
            .stream()
            .map(this::toDto)
            .collect(toList()));

    return mapStatusExceptionsToCommunicationException(discrepantBranches)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(), () -> failure(exception))))
        .get();
  }

  private DiscrepantBranchDto toDto(DiscrepantBranch discrepantBranch) {
    DiscrepantBranchId branchId = discrepantBranch.getDiscrepantBranchId();
    return new DiscrepantBranchDto(
        toBranchIdDto(branchId),
        toInstant(discrepantBranch.getDetectedAt()));
  }

  private ReasoningBranchIdDto toBranchIdDto(DiscrepantBranchId branchId) {
    return new ReasoningBranchIdDto(branchId.getDecisionTreeId(), branchId.getFeatureVectorId());
  }
}
