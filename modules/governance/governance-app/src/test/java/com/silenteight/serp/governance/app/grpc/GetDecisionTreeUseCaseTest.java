package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.DecisionTreeResponse;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.serp.governance.app.grpc.GetDecisionTreeUseCase.GetDecisionTreeUseCaseListener;
import com.silenteight.serp.governance.decisiontreesummaryquery.DecisionTreeSummaryFinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDecisionTreeUseCaseTest {

  private final Fixtures fixtures = new Fixtures();

  @Mock
  private DecisionTreeSummaryFinder finder;
  @Spy
  private GetDecisionTreeUseCaseListener listener;

  private GetDecisionTreeUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new GetDecisionTreeUseCase(finder);
  }

  @Test
  void missingDecisionTreeSpecTest() {
    useCase.activate(fixtures.emptyRequest, listener);

    verify(listener).onMissingDecisionTreeSpec();
  }

  @Test
  void decisionTreeFound_findByIdReturnsTree() {
    when(finder.findById(fixtures.idRequest.getDecisionTreeId())).thenReturn(of(fixtures.summary));

    useCase.activate(fixtures.idRequest, listener);

    verify(listener).onDecisionTreeFound(fixtures.response);
  }

  @Test
  void decisionTreeFound_findByDecisionGroupNameReturnsTree() {
    when(finder.findByDecisionGroupName(fixtures.decisionGroupRequest.getDecisionGroup()))
        .thenReturn(of(fixtures.summary));

    useCase.activate(fixtures.decisionGroupRequest, listener);

    verify(listener).onDecisionTreeFound(fixtures.response);
  }

  @Test
  void decisionTreeNotFound_findByIdReturnsEmpty() {
    when(finder.findById(fixtures.idRequest.getDecisionTreeId())).thenReturn(empty());

    useCase.activate(fixtures.idRequest, listener);

    verify(listener).onDecisionTreeNotFound();
  }

  @Test
  void decisionTreeNotFound_findByDecisionGroupNameReturnsEmpty() {
    when(finder.findByDecisionGroupName(fixtures.decisionGroupRequest.getDecisionGroup()))
        .thenReturn(empty());

    useCase.activate(fixtures.decisionGroupRequest, listener);

    verify(listener).onDecisionTreeNotFound();
  }

  private static class Fixtures {

    GetDecisionTreeRequest emptyRequest = GetDecisionTreeRequest.newBuilder().build();

    GetDecisionTreeRequest idRequest = GetDecisionTreeRequest.newBuilder()
        .setDecisionTreeId(1L)
        .build();

    GetDecisionTreeRequest decisionGroupRequest = GetDecisionTreeRequest.newBuilder()
        .setDecisionGroup("dg")
        .build();

    DecisionTreeSummary summary = DecisionTreeSummary.newBuilder()
        .setName("name")
        .build();

    DecisionTreeResponse response = DecisionTreeResponse.newBuilder()
        .setDecisionTree(summary)
        .build();
  }
}
