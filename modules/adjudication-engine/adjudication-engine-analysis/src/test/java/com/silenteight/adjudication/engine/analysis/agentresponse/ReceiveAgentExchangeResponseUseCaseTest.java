package com.silenteight.adjudication.engine.analysis.agentresponse;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.MatchFeatureValueFacade;
import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse.Builder;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;
import com.silenteight.agents.v1.api.exchange.AgentOutput.FeatureSolution;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiveAgentExchangeResponseUseCaseTest {

  protected static final String FEATURES_TEST_1 = "features/test-1";
  protected static final String FEATURES_TEST_2 = "features/test-2";
  protected static final String TEST_SOLUTION = "SOLUTION";
  protected static final int TEST_MATCH_ID = 456;
  private final FakeFeatureIdsProvider featureIdsProvider = new FakeFeatureIdsProvider();
  private final Builder responseBuilder = AgentExchangeResponse.newBuilder();

  @Mock
  private MatchFeatureValueFacade facade;

  private ReceiveAgentExchangeResponseUseCase useCase;

  @Captor
  private ArgumentCaptor<Collection<MatchFeatureValue>> featureValuesCaptor;

  @Mock
  private DeleteAgentExchangeGateway gateway;

  @BeforeEach
  void setUp() {
    useCase =
        new ReceiveAgentExchangeResponseUseCase(facade, featureIdsProvider, gateway);
  }

  @Test
  void givenEmptyInput_shouldDoNothing() {
    whenUseCaseActivated();
    verifyNoMoreInteractions(facade);
  }

  @Test
  void givenNonExistentRequest_shouldDoNothing() {
    givenOutput("features/test", "SOLUTION", Struct.getDefaultInstance());

    whenUseCaseActivated();

    verifyNoMoreInteractions(facade);
  }

  @Test
  void shouldFailForNotRequestedFeatures() {
    featureIdsProvider.put(FEATURES_TEST_1, 1);
    givenOutput(FEATURES_TEST_1, TEST_SOLUTION, Struct.getDefaultInstance());
    givenOutput(FEATURES_TEST_2, TEST_SOLUTION, Struct.getDefaultInstance());

    assertThatThrownBy(this::whenUseCaseActivated).isInstanceOf(
        UnexpectedFeaturesReceivedException.class);
  }

  @Test
  void shouldCreateMatchFeatureValues() {
    featureIdsProvider.put(FEATURES_TEST_1, 1);
    featureIdsProvider.put(FEATURES_TEST_2, 2);

    var reason = Struct
        .newBuilder()
        .putFields("reason", Value.newBuilder().setStringValue("because").build())
        .build();

    givenOutput(FEATURES_TEST_1, TEST_SOLUTION, reason);
    givenOutput(FEATURES_TEST_2, TEST_SOLUTION, reason);

    whenUseCaseActivated();

    verify(facade, times(1)).createMatchFeatureValues(featureValuesCaptor.capture());

    var featureValueDtos = featureValuesCaptor.getValue();

    assertThat(featureValueDtos)
        .hasSize(2)
        .allSatisfy(r -> {
          assertThat(r.getMatchId()).isEqualTo(TEST_MATCH_ID);
          assertThat(r.getReason()).isEqualTo(reason);
          assertThat(r.getValue()).isEqualTo(TEST_SOLUTION);
        })
        .anySatisfy(r -> assertThat(r.getAgentConfigFeatureId()).isEqualTo(1))
        .anySatisfy(r -> assertThat(r.getAgentConfigFeatureId()).isEqualTo(2));
  }

  private void whenUseCaseActivated() {
    useCase.receiveAgentExchangeResponse(UUID.randomUUID(), responseBuilder.build());
  }

  private AgentOutput givenOutput(String featureName, String solution, Struct reason) {
    var agentOutput = AgentOutput.newBuilder()
        .setMatch("alerts/123/matches/" + TEST_MATCH_ID)
        .addFeatures(Feature.newBuilder()
            .setFeature(featureName)
            .setFeatureSolution(FeatureSolution.newBuilder()
                .setSolution(solution)
                .setReason(reason)
                .build())
            .build())
        .build();

    responseBuilder.addAgentOutputs(agentOutput);

    return agentOutput;
  }
}
