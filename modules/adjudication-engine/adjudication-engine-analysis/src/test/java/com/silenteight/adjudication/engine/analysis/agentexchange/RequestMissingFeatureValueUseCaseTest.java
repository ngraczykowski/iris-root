package com.silenteight.adjudication.engine.analysis.agentexchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.createFeatureRequestingStrategySupplier;
import static org.assertj.core.api.Assertions.*;

class RequestMissingFeatureValueUseCaseTest {

  private final AgentExchangeRequestMessageRepository repository =
      new InMemoryAgentRequestMessageRepository();
  private final AgentRequestHandler agentRequestHandler = new AgentRequestHandler(
      createFeatureRequestingStrategySupplier(10), repository);
  private AgentExchangeRequestGateway gateway;
  private RequestMissingFeatureValuesUseCase requestMissingFeatureValuesUseCase;

  @BeforeEach
  void setUp() {
    gateway = new InMemoryAgentExchangeRequestGateway();
    MissingMatchFeatureReader missingMatchFutureReader = new InMemoryMissingMatchFutureReader();
    requestMissingFeatureValuesUseCase =
        new RequestMissingFeatureValuesUseCase(
            missingMatchFutureReader,
            agentRequestHandler, gateway);
  }

  @Test
  void shouldSendMessages() {
    requestMissingFeatureValuesUseCase.requestMissingFeatureValues("analysis/123");
    assertThat(((InMemoryAgentExchangeRequestGateway)gateway).count()).isEqualTo(100);
  }
}
