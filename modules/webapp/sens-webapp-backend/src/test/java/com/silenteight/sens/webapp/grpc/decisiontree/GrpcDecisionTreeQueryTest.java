package com.silenteight.sens.webapp.grpc.decisiontree;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.DecisionTreeResponse;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sens.webapp.backend.decisiontree.details.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.details.DecisionTreeNotFound;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.grpc.GrpcFixtures.NOT_FOUND_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GrpcDecisionTreeQueryTest {

  private static final long DECISION_TREE_ID = 123;
  private static final long OTHER_DECISION_TREE_ID = 456;
  @Mock
  private DecisionTreeGovernanceBlockingStub decisionTreeStub;

  @InjectMocks
  private GrpcDecisionTreeQuery underTest;

  @Test
  void returnsDecisionTree() {
    given(decisionTreeStub.getDecisionTree(
        argThat(r -> r.getDecisionTreeId() == DECISION_TREE_ID)))
        .willReturn(getResponse());

    assertThat(underTest.getDecisionTree(DECISION_TREE_ID))
        .isEqualTo(new DecisionTreeDto(DECISION_TREE_ID));
  }

  @NotNull
  private DecisionTreeResponse getResponse() {
    return DecisionTreeResponse.newBuilder().setDecisionTree(
        DecisionTreeSummary.newBuilder().setId(GrpcDecisionTreeQueryTest.DECISION_TREE_ID).build()
    ).build();
  }

  @Test
  void throwsGrpcException_whenCommuniationException() {
    given(decisionTreeStub.getDecisionTree(
        argThat(r -> r.getDecisionTreeId() == OTHER_DECISION_TREE_ID)))
        .willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable getDecisionTree = () -> underTest.getDecisionTree(OTHER_DECISION_TREE_ID);

    assertThatThrownBy(getDecisionTree).isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void throwsGrpcNotFoundException_whenNotFound() {
    given(decisionTreeStub.getDecisionTree(
        argThat(r -> r.getDecisionTreeId() == OTHER_DECISION_TREE_ID)))
        .willThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    ThrowingCallable getDecisionTree = () -> underTest.getDecisionTree(OTHER_DECISION_TREE_ID);

    assertThatThrownBy(getDecisionTree).isInstanceOf(DecisionTreeNotFound.class);
  }
}
