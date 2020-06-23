package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.sens.webapp.backend.reasoningbranch.feature.value.exception.ReasoningBranchNotFoundException;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.NOT_FOUND_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GrpcFeatureValuesQueryTest {

  @Mock
  private BranchGovernanceBlockingStub branchStub;

  private GrpcFeatureValuesQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new GrpcFeatureValuesQuery(branchStub);
  }

  @Test
  void returnsEmptyList_whenGrpcThrowsNotFoundStatusException() {
    given(branchStub.getReasoningBranch(any())).willThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    ThrowingCallable when = () -> underTest.findFeatureValues(1L, 5L);

    assertThatThrownBy(when).isInstanceOf(ReasoningBranchNotFoundException.class);
  }

  @Test
  void throwsGrpcCommunicationException_whenGrpcThrowsOtherThanNotFoundStatusException() {
    given(branchStub.getReasoningBranch(any())).willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable when = () -> underTest.findFeatureValues(1L, 5L);

    assertThatThrownBy(when).isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void returnsCorrectBranchFeatures_whenRequestingBranch() {
    long decisionTreeId = 1L;
    long featureVectorId = 5L;
    Instant updatedAt = Instant.parse("2019-10-28T15:35:24.00Z");
    String featureValue1 = "feature-value-1";
    String featureValue2 = "feature-value-2";
    given(branchStub.getReasoningBranch(
        argThat(
            request -> request.getReasoningBranchId().getDecisionTreeId() == decisionTreeId
                && request.getReasoningBranchId().getFeatureVectorId() == featureVectorId)))
        .willReturn(
            reasoningBranchResponseWith(
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchIdOf(decisionTreeId, featureVectorId))
                    .setUpdatedAt(toTimestamp(updatedAt))
                    .setSolution(BRANCH_FALSE_POSITIVE)
                    .setEnabled(true)
                    .addFeatureValue(featureValue1)
                    .addFeatureValue(featureValue2)
                    .build()
            ));

    List<String> actual = underTest.findFeatureValues(decisionTreeId, featureVectorId);

    assertThat(actual).hasSize(2);
    assertThat(actual).containsExactly(featureValue1, featureValue2);
  }

  private static ReasoningBranchId reasoningBranchIdOf(long decisionTreeId, long featureVector) {
    return ReasoningBranchId
        .newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureVectorId(featureVector)
        .build();
  }

  private static ReasoningBranchResponse reasoningBranchResponseWith(
      ReasoningBranchSummary reasoningBranch) {

    return ReasoningBranchResponse
        .newBuilder()
        .setReasoningBranch(reasoningBranch)
        .build();
  }
}
