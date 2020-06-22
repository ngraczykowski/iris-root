package com.silenteight.sens.webapp.grpc.decisiontree;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sens.webapp.backend.decisiontree.details.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.details.DecisionTreeNotFound;
import com.silenteight.sens.webapp.backend.decisiontree.details.DecisionTreeQuery;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import com.google.rpc.Code;
import io.vavr.control.Try;

import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;

@RequiredArgsConstructor
class GrpcDecisionTreeQuery implements DecisionTreeQuery {

  private final DecisionTreeGovernanceBlockingStub decisionTreeStub;

  @Override
  public DecisionTreeDto getDecisionTree(long treeId) {
    GetDecisionTreeRequest request = GetDecisionTreeRequest
        .newBuilder()
        .setDecisionTreeId(treeId)
        .build();

    Try<DecisionTreeSummary> decisionTree =
        of(() -> decisionTreeStub
            .getDecisionTree(request)
            .getDecisionTree());

    return mapStatusExceptionsToCommunicationException(decisionTree)
        .recoverWith(GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(Code.NOT_FOUND)), () -> failure(DecisionTreeNotFound.of(treeId))),
                Case($(), () -> failure(exception))
            ))
        .map(decisionTreeSummary -> new DecisionTreeDto(decisionTreeSummary.getId()))
        .get();
  }
}
