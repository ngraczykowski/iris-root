package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.feature.value.FeatureValuesQuery;
import com.silenteight.sens.webapp.backend.reasoningbranch.feature.value.exception.ReasoningBranchNotFoundException;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import io.vavr.control.Try;

import java.util.List;

import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class GrpcFeatureValuesQuery implements FeatureValuesQuery {

  private final BranchGovernanceBlockingStub branchStub;

  @Override
  public List<String> findFeatureValues(long decisionTreeId, long featureVectorId) {
    log.info(REASONING_BRANCH,
        "Get Reasoning Branch Feature values using gRPC BranchGovernance."
            + " decisionTreeId={}, featureVectorId={}", decisionTreeId, featureVectorId);

    return mapStatusExceptionsToCommunicationException(
        featureValuesOf(decisionTreeId, featureVectorId))
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> failure(new ReasoningBranchNotFoundException())),
                Case($(), () -> failure(exception))))
        .onSuccess(GrpcFeatureValuesQuery::logSuccess)
        .onFailure(GrpcFeatureValuesQuery::logFailure)
        .get();
  }

  private Try<List<String>> featureValuesOf(long decisionTreeId, long featureVectorId) {
    GetReasoningBranchRequest request = GetReasoningBranchRequest.newBuilder()
        .setReasoningBranchId(toReasoningBranchId(decisionTreeId, featureVectorId))
        .build();

    return of(() -> branchStub
        .getReasoningBranch(request)
        .getReasoningBranch()
        .getFeatureValueList()
        .stream()
        .collect(toList()));
  }

  private static ReasoningBranchId toReasoningBranchId(long decisionTreeId, long featureVectorId) {
    return ReasoningBranchId.newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureVectorId(featureVectorId)
        .build();
  }

  private static void logSuccess(List<String> featureValues) {
    log.info(REASONING_BRANCH, "Found {} Reasoning Branch Feature values", featureValues.size());
  }

  private static void logFailure(Throwable throwable) {
    log.error(REASONING_BRANCH, "Could not get Reasoning Branch Feature values", throwable);
  }
}
