package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.BranchChange;
import com.silenteight.proto.serp.v1.api.BranchChange.Builder;
import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.BranchSolutionChange;
import com.silenteight.proto.serp.v1.api.ChangeBranchesRequest;
import com.silenteight.proto.serp.v1.api.EnablementChange;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.ReasoningBranchUpdateRepository;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdatedBranch;

import io.vavr.control.Try;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static io.vavr.API.$;
import static io.vavr.API.Case;

@RequiredArgsConstructor
@Slf4j
class GrpcReasoningBranchUpdateRepository implements ReasoningBranchUpdateRepository {

  private final BranchGovernanceBlockingStub governanceBlockingStub;

  @Override
  @SuppressWarnings("unchecked") // https://github.com/vavr-io/vavr/issues/2411
  public Try<Void> save(UpdatedBranch updatedBranch) {
    log.debug("Saving updated branch in gRPC repository. branchId={}", updatedBranch.getBranchId());

    Try<Void> tryUpdate = Try.of(() -> createRequest(updatedBranch))
        .mapFailure(Case(
            $(instanceOf(IllegalArgumentException.class)),
            AiSolutionNotSupportedException::new))
        .flatMapTry(request -> Try.run(() -> governanceBlockingStub.changeBranches(request)));

    return mapStatusExceptionsToCommunicationException(tryUpdate)
        .mapFailure(Case($(codeIs(NOT_FOUND)), BranchNotFoundException::new));
  }

  private static ChangeBranchesRequest createRequest(UpdatedBranch updatedBranch) {
    Builder branchChange = BranchChange.newBuilder();

    branchChange.setReasoningBranchId(buildGrpcBranchId(updatedBranch.getBranchId()));

    updatedBranch.getNewAiSolution()
        .map(GrpcReasoningBranchUpdateRepository::buildSolutionChange)
        .ifPresent(branchChange::setSolutionChange);

    updatedBranch.getNewIsActive()
        .map(GrpcReasoningBranchUpdateRepository::buildEnablementChange)
        .ifPresent(branchChange::setEnablementChange);

    return ChangeBranchesRequest.newBuilder()
        .addBranchChange(branchChange)
        .build();
  }

  private static ReasoningBranchId buildGrpcBranchId(BranchId branchId) {
    return ReasoningBranchId.newBuilder()
        .setFeatureVectorId(branchId.getBranchNo())
        .setDecisionTreeId(branchId.getTreeId())
        .build();
  }

  private static EnablementChange buildEnablementChange(boolean newStatus) {
    return EnablementChange.newBuilder()
        .setEnabled(newStatus)
        .build();
  }

  private static BranchSolutionChange buildSolutionChange(String newSolution) {
    return BranchSolutionChange.newBuilder()
        .setSolution(BranchSolution.valueOf(newSolution))
        .build();
  }
}
