package com.silenteight.sens.webapp.grpc.bulkchange;

import com.silenteight.proto.protobuf.Uuid;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeGovernanceGrpc.BulkBranchChangeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.BranchSolutionChange;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.EnablementChange;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesResponse;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId.Builder;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIdsForReasoningBranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State.STATE_CREATED;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.NOT_FOUND_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcBulkChangeQueryTest {

  @Mock
  private BulkBranchChangeGovernanceBlockingStub bulkBranchChangeStub;

  private GrpcBulkChangeQuery bulkChangeQuery;

  @BeforeEach
  void setUp() {
    bulkChangeQuery = new GrpcBulkChangeQuery(bulkBranchChangeStub);
  }

  @Test
  void returnsBulkChanges() {
    UUID id1 = randomUUID();
    UUID id2 = randomUUID();
    long decisionTreeId1 = 1L;
    long featureVectorId1 = 12L;
    long featureVectorId2 = 15L;
    long decisionTreeId2 = 2L;
    long featureVectorId3 = 13L;

    when(bulkBranchChangeStub.listBulkBranchChanges(withState(STATE_CREATED)))
        .thenReturn(
            listBulkBranchChangesResponseWith(
                bulkBranchChangeViewWith(
                    id1, true, BRANCH_FALSE_POSITIVE,
                    reasoningBranchIdOf(decisionTreeId1, featureVectorId1),
                    reasoningBranchIdOf(decisionTreeId1, featureVectorId2)),
                bulkBranchChangeViewWith(
                    id2, false, BRANCH_POTENTIAL_TRUE_POSITIVE,
                    reasoningBranchIdOf(decisionTreeId2, featureVectorId3))));

    List<BulkChangeDto> bulkChanges = bulkChangeQuery.listPending();

    assertThat(bulkChanges).hasSize(2);

    assertThat(bulkChanges.get(0).getId()).isEqualTo(id1);
    assertThat(bulkChanges.get(0).getReasoningBranchIds())
        .containsExactlyInAnyOrderElementsOf(List.of(
            new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1),
            new ReasoningBranchIdDto(decisionTreeId1, featureVectorId2)));
    assertThat(bulkChanges.get(0).getAiSolution()).isEqualTo("FALSE_POSITIVE");
    assertThat(bulkChanges.get(0).getActive()).isEqualTo(TRUE);

    assertThat(bulkChanges.get(1).getId()).isEqualTo(id2);
    assertThat(bulkChanges.get(1).getReasoningBranchIds())
        .containsExactlyInAnyOrderElementsOf(
            List.of(new ReasoningBranchIdDto(decisionTreeId2, featureVectorId3)));
    assertThat(bulkChanges.get(1).getAiSolution()).isEqualTo("POTENTIAL_TRUE_POSITIVE");
    assertThat(bulkChanges.get(1).getActive()).isEqualTo(FALSE);
  }

  @Test
  void returnsNoChangeIfNoAiSolutionChange() {
    when(bulkBranchChangeStub.listBulkBranchChanges(withState(STATE_CREATED)))
        .thenReturn(
            ListBulkBranchChangesResponse
                .newBuilder()
                .addChanges(bulkBranchChangeViewWithDefaults())
                .build());

    List<BulkChangeDto> bulkChanges = bulkChangeQuery.listPending();

    assertThat(bulkChanges).hasSize(1);
    assertThat(bulkChanges.get(0).getAiSolution()).isNull();
    assertThat(bulkChanges.get(0).getActive()).isNull();
  }

  @Test
  void returnsEmptyList_whenGrpcThrowsNotFoundStatusException_requestingFeatureNames() {
    when(bulkBranchChangeStub.listBulkBranchChanges(any())).thenThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    assertThat(bulkChangeQuery.listPending()).isEmpty();
  }

  @Test
  void throwsGrpcException_whenGrpcThrowsNotFoundStatusException_requestingFeatureNames() {
    when(bulkBranchChangeStub.listBulkBranchChanges(any())).thenThrow(
        OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable featureNamesCall = () -> bulkChangeQuery.listPending();

    assertThatThrownBy(featureNamesCall).isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void returnsBulkChangeIds() {
    UUID bulkChangeId1 = randomUUID();
    UUID bulkChangeId2 = randomUUID();
    UUID bulkChangeId3 = randomUUID();
    long decisionTreeId1 = 1L;
    long decisionTreeId2 = 2L;
    long featureVectorId1 = 12L;
    long featureVectorId2 = 15L;
    long featureVectorId3 = 13L;
    long featureVectorId4 = 25L;
    ReasoningBranchId reasoningBranchId1 =
        reasoningBranchIdOf(decisionTreeId1, featureVectorId1).build();
    ReasoningBranchId reasoningBranchId2 =
        reasoningBranchIdOf(decisionTreeId2, featureVectorId2).build();

    when(bulkBranchChangeStub.listBulkBranchChanges(
        withStateAndReasoningBranchIds(
            STATE_CREATED,
            List.of(reasoningBranchId1, reasoningBranchId2))))
        .thenReturn(
            listBulkBranchChangesResponseWith(
                bulkBranchChangeViewWith(
                    bulkChangeId1, true, BRANCH_FALSE_POSITIVE,
                    reasoningBranchIdOf(decisionTreeId1, featureVectorId1),
                    reasoningBranchIdOf(decisionTreeId1, featureVectorId3)),
                bulkBranchChangeViewWith(
                    bulkChangeId2, true, BRANCH_FALSE_POSITIVE,
                    reasoningBranchIdOf(decisionTreeId1, featureVectorId1),
                    reasoningBranchIdOf(decisionTreeId2, featureVectorId2)),
                bulkBranchChangeViewWith(
                    bulkChangeId3, false, BRANCH_POTENTIAL_TRUE_POSITIVE,
                    reasoningBranchIdOf(decisionTreeId2, featureVectorId2),
                    reasoningBranchIdOf(decisionTreeId2, featureVectorId4))));

    List<BulkChangeIdsForReasoningBranchDto> bulkChangeIds =
        bulkChangeQuery.getIds(
            List.of(
                new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1),
                new ReasoningBranchIdDto(decisionTreeId2, featureVectorId2)));

    assertThat(bulkChangeIds).hasSize(2);
    assertThat(bulkChangeIds).containsExactly(
        new BulkChangeIdsForReasoningBranchDto(
            new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1),
            List.of(bulkChangeId1, bulkChangeId2)),
        new BulkChangeIdsForReasoningBranchDto(
            new ReasoningBranchIdDto(decisionTreeId2, featureVectorId2),
            List.of(bulkChangeId2, bulkChangeId3)));
  }

  @Test
  void throwsGrpcException_whenGrpcThrowsNotFoundStatusException_requestingBulkChangeIds() {
    List<ReasoningBranchIdDto> reasoningBranchIds = List.of(new ReasoningBranchIdDto(1L, 20L));
    when(bulkBranchChangeStub.listBulkBranchChanges(any())).thenThrow(
        OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable featureNamesCall = () -> bulkChangeQuery.getIds(reasoningBranchIds);

    assertThatThrownBy(featureNamesCall).isInstanceOf(GrpcCommunicationException.class);
  }

  private ListBulkBranchChangesResponse listBulkBranchChangesResponseWith(
      BulkBranchChangeView.Builder... bulkChanges) {
    ListBulkBranchChangesResponse.Builder responseBuilder = ListBulkBranchChangesResponse
        .newBuilder();
    for (BulkBranchChangeView.Builder bulkChange : bulkChanges) {
      responseBuilder.addChanges(bulkChange);
    }
    return responseBuilder.build();
  }

  private BulkBranchChangeView.Builder bulkBranchChangeViewWith(
      UUID id1, boolean enabled, BranchSolution solution, Builder... reasoningBranchIds) {
    return bulkBranchChangeViewWith(fromJavaUuid(id1), reasoningBranchIds)
        .setEnablementChange(EnablementChange.newBuilder().setEnabled(enabled))
        .setSolutionChange(BranchSolutionChange
            .newBuilder()
            .setSolution(solution));
  }

  private BulkBranchChangeView.Builder bulkBranchChangeViewWithDefaults() {
    return bulkBranchChangeViewWith(fromJavaUuid(randomUUID()), reasoningBranchIdOf(1L, 1L));
  }

  private BulkBranchChangeView.Builder bulkBranchChangeViewWith(
      Uuid id, Builder... reasoningBranchIds) {

    BulkBranchChangeView.Builder bulkBranchChangeView = BulkBranchChangeView.newBuilder();
    bulkBranchChangeView.setId(id);
    for (Builder reasoningBranchId : reasoningBranchIds) {
      bulkBranchChangeView.addReasoningBranchIds(reasoningBranchId);
    }
    return bulkBranchChangeView;
  }

  private Builder reasoningBranchIdOf(long decisionTreeId, long featureVectorId) {
    return ReasoningBranchId.newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureVectorId(featureVectorId);
  }

  private ListBulkBranchChangesRequest withState(State state) {
    return argThat(r ->
        r.getStateFilter().getStatesList().size() == 1 &&
            r.getStateFilter().getStatesList().contains(state));
  }

  private ListBulkBranchChangesRequest withStateAndReasoningBranchIds(
      State state, Collection<ReasoningBranchId> reasoningBranchIds) {

    return argThat(r ->
        r.getStateFilter().getStatesList().size() == 1
            && r.getStateFilter().getStatesList().contains(state)
            && r.getReasoningBranchFilter().getReasoningBranchIdsList().size() == 2
            && r.getReasoningBranchFilter().getReasoningBranchIdsList()
            .containsAll(reasoningBranchIds));
  }
}
