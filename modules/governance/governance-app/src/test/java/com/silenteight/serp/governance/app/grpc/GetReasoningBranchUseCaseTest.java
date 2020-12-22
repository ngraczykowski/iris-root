package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.serp.governance.branchquery.ReasoningBranchFinder;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

class GetReasoningBranchUseCaseTest {

  private GetReasoningBranchRequest getReasoningBranchRequest;

  @BeforeEach
  void setUp() {
    ReasoningBranchId reasoningBranchId =
        ReasoningBranchId.newBuilder().setDecisionTreeId(1).setFeatureVectorId(1).build();
    getReasoningBranchRequest =
        GetReasoningBranchRequest.newBuilder().setReasoningBranchId(reasoningBranchId).build();
  }

  @Test
  void shouldReturnCorrectResponse() {
    //given
    ReasoningBranchFinder reasoningBranchFinder = Mockito.mock(ReasoningBranchFinder.class);
    ReasoningBranchSummary reasoningBranchSummary = ReasoningBranchSummary
        .newBuilder()
        .setEnabled(true)
        .setSolution(BranchSolution.BRANCH_NO_DECISION)
        .build();

    Mockito
        .when(reasoningBranchFinder.getByDecisionTreeIdAndFeatureVectorId(
            Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(reasoningBranchSummary);

    GetReasoningBranchUseCase getReasoningBranchUseCase =
        new GetReasoningBranchUseCase(reasoningBranchFinder);

    //when
    ReasoningBranchResponse reasoningBranchResponse =
        getReasoningBranchUseCase.activate(getReasoningBranchRequest);

    //then
    assertThat(reasoningBranchResponse.getReasoningBranch().getSolution())
        .isEqualTo(BranchSolution.BRANCH_NO_DECISION);
    assertThat(reasoningBranchResponse.getReasoningBranch().getEnabled())
        .isEqualTo(true);
  }

  @Test
  void shouldReturnException() {
    //given
    ReasoningBranchFinder reasoningBranchFinder = Mockito.mock(ReasoningBranchFinder.class);

    Mockito
        .when(reasoningBranchFinder.getByDecisionTreeIdAndFeatureVectorId(
            Mockito.anyLong(), Mockito.anyLong()))
        .thenThrow(new StatusRuntimeException(Status.NOT_FOUND));

    GetReasoningBranchUseCase getReasoningBranchUseCase =
        new GetReasoningBranchUseCase(reasoningBranchFinder);

    //when
    Assertions.assertThrows(StatusRuntimeException.class, () -> getReasoningBranchUseCase.activate(
        getReasoningBranchRequest));
  }
}
