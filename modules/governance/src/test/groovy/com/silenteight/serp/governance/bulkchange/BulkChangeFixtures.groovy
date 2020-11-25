package com.silenteight.serp.governance.bulkchange

import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State
import com.silenteight.proto.serp.v1.api.FeatureSignatures
import com.silenteight.proto.serp.v1.api.FeatureVectorIds
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.StateFilter
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeRequest
import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.BranchSolutionChange
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.EnablementChange
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId
import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand
import com.silenteight.proto.serp.v1.recommendation.BranchSolution
import com.silenteight.protocol.utils.Uuids
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder

import com.google.protobuf.ByteString
import org.springframework.context.ApplicationEventPublisher

trait BulkChangeFixtures {

  CreateBulkBranchChangeCommand createBulkChange = createBulkBranchChangeCommand(
      Boolean.TRUE, BranchSolution.BRANCH_FALSE_POSITIVE, [1: 1])
  ApplyBulkBranchChangeCommand applyBulkChange = applyBulkBranchChangeCommand(
      createBulkChange)
  RejectBulkBranchChangeCommand rejectBulkChange = rejectBulkBranchChangeCommand(
      createBulkChange)

  ValidateBulkChangeRequest validateFeatureVectorIds = validateFeatureVectorIds([1L, 2L, 3L, 4L])
  ValidateBulkChangeRequest validateFeatureSignatures = validateFeatureSignatures(
      ['a', 'b', 'c', 'd'])

  List<ByteString> existingFeatureSignatures = ['a', 'b'].collect {ByteString.copyFromUtf8(it)}
  List<ByteString> nonexistingFeatureSignatures = ['c', 'd'].collect {ByteString.copyFromUtf8(it)}

  static BulkChangeFacade bulkChangeFacade(
      ApplicationEventPublisher applicationEventPublisher,
      DecisionTreeFacade decisionTreeFacade, FeatureVectorFinder featureVectorFinder) {

    def bulkBranchChangeRepository = new InMemoryBulkBranchChangeRepository()

    def mapper = new BulkBranchChangeMapper()
    def validateUseCase = ValidateBulkChangeUseCase.builder()
        .decisionTreeFacade(decisionTreeFacade)
        .featureVectorFinder(featureVectorFinder)
        .build()
    def listUseCase = ListBulkBranchChangesUseCase.builder()
        .bulkBranchChangeFinder(new BulkBranchChangeFinder(bulkBranchChangeRepository))
        .build()

    return new BulkChangeFacade(
        new ApplyBulkBranchChangeHandler(bulkBranchChangeRepository, applicationEventPublisher),
        new CreateBulkBranchChangeHandler(mapper, bulkBranchChangeRepository),
        new RejectBulkBranchChangeHandler(bulkBranchChangeRepository, applicationEventPublisher),
        validateUseCase,
        listUseCase
    )
  }

  private static CreateBulkBranchChangeCommand createBulkBranchChangeCommand(
      Boolean enabled,
      BranchSolution solution,
      Map<Integer, Integer> reasoningBranchIds) {
    List<ReasoningBranchId> branchIds = reasoningBranchIds.collect {
      ReasoningBranchId.
          newBuilder().
          setDecisionTreeId(it.key).
          setFeatureVectorId(it.value).
          build()
    }
    CreateBulkBranchChangeCommand
        .newBuilder()
        .setId(Uuids.random())
        .setCorrelationId(Uuids.random())
        .addAllReasoningBranchIds(branchIds)
        .setEnablementChange(EnablementChange.newBuilder().setEnabled(enabled).build())
        .setSolutionChange(BranchSolutionChange.newBuilder().setSolution(solution).build())
        .build()
  }

  private static ApplyBulkBranchChangeCommand applyBulkBranchChangeCommand(
      CreateBulkBranchChangeCommand createCommand) {
    ApplyBulkBranchChangeCommand.newBuilder()
        .setId(createCommand.id)
        .setCorrelationId(Uuids.random())
        .build()
  }

  private static RejectBulkBranchChangeCommand rejectBulkBranchChangeCommand(
      CreateBulkBranchChangeCommand createCommand) {
    RejectBulkBranchChangeCommand.newBuilder()
        .setId(createCommand.id)
        .setCorrelationId(Uuids.random())
        .build()
  }

  private static ValidateBulkChangeRequest validateFeatureVectorIds(List<Long> featureVectorIds) {
    ValidateBulkChangeRequest.newBuilder()
        .setDecisionTreeId(1L)
        .setFeatureVectorIds(
            FeatureVectorIds.
                newBuilder().
                addAllFeatureVectorId(featureVectorIds).
                build())
        .build()
  }

  private static ValidateBulkChangeRequest validateFeatureSignatures(List<String> signatures) {
    List<ByteString> byteSignatures = signatures.collect {ByteString.copyFromUtf8(it)}
    ValidateBulkChangeRequest.newBuilder()
        .setDecisionTreeId(1L)
        .setFeatureSignatures(
            FeatureSignatures.newBuilder().addAllFeaturesSignature(
                byteSignatures).build())
        .build()
  }

  static ListBulkBranchChangesRequest withStatuses(List<State> states = []) {
    ListBulkBranchChangesRequest.newBuilder()
     .setStateFilter(buildStateFilter(states))
     .build()
  }

  private static StateFilter buildStateFilter(List<State> states = []) {
    def fetchedStates = states ?: [State.STATE_APPLIED, State.STATE_CREATED, State.STATE_REJECTED]
    StateFilter.newBuilder()
        .addAllStates(fetchedStates)
        .build()
  }
}
