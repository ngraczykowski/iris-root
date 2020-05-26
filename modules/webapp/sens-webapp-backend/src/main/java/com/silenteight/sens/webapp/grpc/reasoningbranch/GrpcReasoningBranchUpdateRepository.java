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
import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchIdsNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.ChangeRequestRepository;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdatedBranches;
import com.silenteight.sens.webapp.grpc.BranchSolutionMapper;

import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class GrpcReasoningBranchUpdateRepository implements ChangeRequestRepository {

  private final BranchSolutionMapper branchSolutionMapper;
  private final BranchGovernanceBlockingStub governanceBlockingStub;

  @Override
  @SuppressWarnings("unchecked") // https://github.com/vavr-io/vavr/issues/2411
  public Try<Void> save(UpdatedBranches updatedBranches) {
    log.info(REASONING_BRANCH, "Saving updated Branches using gRPC BranchGovernance");
    Try<Void> tryUpdate = Try.of(() -> createRequest(updatedBranches))
        .mapFailure(Case(
            $(instanceOf(IllegalArgumentException.class)), AiSolutionNotSupportedException::new))
        .flatMapTry(request -> Try.run(() -> governanceBlockingStub.changeBranches(request)));

    return mapStatusExceptionsToCommunicationException(tryUpdate)
        .mapFailure(Case($(codeIs(NOT_FOUND)), BranchIdsNotFoundException::new))
        .onSuccess(ignored -> log.info(REASONING_BRANCH, "Saved updated Branch"))
        .onFailure(reason -> log.error(REASONING_BRANCH, "Could not save updated Branch", reason));
  }

  private ChangeBranchesRequest createRequest(UpdatedBranches updatedBranches) {
    return ChangeBranchesRequest.newBuilder()
        .addAllBranchChange(createBranchChanges(updatedBranches))
        .setCorrelationId(fromJavaUuid(RequestCorrelation.id()))
        .build();
  }

  private Collection<BranchChange> createBranchChanges(UpdatedBranches updatedBranches) {
    return updatedBranches
        .getBranchIds()
        .stream()
        .map(branchId ->
            createBranchChange(
                updatedBranches.getTreeId(),
                branchId,
                updatedBranches.getNewAiSolution(),
                updatedBranches.getNewStatus()))
        .collect(toList());
  }

  @NotNull
  private BranchChange createBranchChange(
      long treeId, long branchId, Optional<String> newAiSolution, Optional<Boolean> newIsActive) {

    Builder branchChange = BranchChange.newBuilder();

    branchChange.setReasoningBranchId(buildGrpcBranchId(treeId, branchId));

    newAiSolution
        .map(this::buildSolutionChange)
        .ifPresent(branchChange::setSolutionChange);

    newIsActive
        .map(GrpcReasoningBranchUpdateRepository::buildEnablementChange)
        .ifPresent(branchChange::setEnablementChange);

    return branchChange.build();
  }

  private static ReasoningBranchId buildGrpcBranchId(long treeId, long branchId) {
    return ReasoningBranchId.newBuilder()
        .setDecisionTreeId(treeId)
        .setFeatureVectorId(branchId)
        .build();
  }

  private static EnablementChange buildEnablementChange(boolean newStatus) {
    return EnablementChange.newBuilder()
        .setEnabled(newStatus)
        .build();
  }

  private BranchSolutionChange buildSolutionChange(String newSolution) {
    return BranchSolutionChange.newBuilder()
        .setSolution(branchSolutionMapper.map(newSolution))
        .build();
  }
}
