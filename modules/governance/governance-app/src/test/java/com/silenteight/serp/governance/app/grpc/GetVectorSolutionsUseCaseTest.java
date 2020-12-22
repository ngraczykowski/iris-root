package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.GetVectorSolutionsRequest;
import com.silenteight.serp.governance.branchquery.VectorSolutionFinder;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetVectorSolutionsUseCaseTest {

  public static final String DECISION_GROUP = "decision group";

  @Mock
  private VectorSolutionFinder vectorSolutionFinder;

  @Mock
  private DecisionGroupService decisionGroupService;

  private GetVectorSolutionsUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new GetVectorSolutionsUseCase(vectorSolutionFinder, decisionGroupService);
  }

  @Test
  void storeDecisionGroup() {
    // given
    GetVectorSolutionsRequest request = GetVectorSolutionsRequest.newBuilder()
        .setDecisionGroup(DECISION_GROUP)
        .build();

    // when
    useCase.activate(request);

    // then
    verify(decisionGroupService).store(DECISION_GROUP);
  }
}