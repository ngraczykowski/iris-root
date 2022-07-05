package com.silenteight.agent.facade;

import com.silenteight.agent.monitoring.Monitoring;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;
import com.silenteight.agents.v1.api.exchange.AgentOutput.FeatureSolution;

import com.google.protobuf.Struct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentErrorHandlerTest {

  @Mock private Monitoring monitoring;

  private AgentErrorHandler underTest;

  @BeforeEach
  void init() {
    underTest = new AgentErrorHandler(monitoring);
  }

  @Test
  void createErrorResponse() {
    var features = List.of("feature1", "feature2");
    var req = AgentExchangeRequest
        .newBuilder()
        .addAllFeatures(features)
        .addMatches("match1")
        .addMatches("match2")
        .build();
    var ex = new AgentRequestTimeoutException(10);
    var res = underTest.createErrorResponse(req, ex);
    var allSolutions = res
        .getAgentOutputsList()
        .stream()
        .flatMap(x -> x.getFeaturesList().stream())
        .map(Feature::getFeatureSolution)
        .map(FeatureSolution::getSolution)
        .collect(Collectors.toList());

    assertEquals("match1", res.getAgentOutputs(0).getMatch());
    assertEquals("match2", res.getAgentOutputs(1).getMatch());
    assertEquals(4, allSolutions.size());
    assertEquals(Set.of("DEADLINE_EXCEEDED"), new HashSet<>(allSolutions));
    verify(monitoring).captureException(ex);
  }

  @Test
  void shouldBuildErrorSolution() {
    var ex = new AgentRequestTimeoutException(10);
    var message = ex.getMessage();
    var res = underTest.buildErrorFeatureSolution("solution", ex);
    assertEquals(message, res.getReason().getFieldsMap().get("errorMessage").getStringValue());
    verify(monitoring).captureException(ex);
  }

  @ParameterizedTest
  @ValueSource(strings = { "test", "\"test\"", "\"}" })
  void shouldBuildErrorSolutionForVariousMessages(String message) {
    var ex = new RuntimeException(message);
    var res = underTest.buildErrorFeatureSolution("solution", ex);
    assertEquals(message, res.getReason().getFieldsMap().get("errorMessage").getStringValue());
    verify(monitoring).captureException(ex);
  }

  @Test
  void shouldBuildErrorSolutionForNullMessage() {
    var ex = new RuntimeException();
    var res = underTest.buildErrorFeatureSolution("solution", ex);
    assertEquals("", res.getReason().getFieldsMap().get("errorMessage").getStringValue());
    verify(monitoring).captureException(ex);
  }

  @Test
  void shouldBuildNoDataSolution() {
    var data = underTest.buildNoDataSolution();

    assertEquals("NO_DATA", data.getSolution());
    assertEquals(Struct.newBuilder().build(), data.getReason());
  }
}
