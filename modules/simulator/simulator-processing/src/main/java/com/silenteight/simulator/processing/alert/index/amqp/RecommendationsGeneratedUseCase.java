package com.silenteight.simulator.processing.alert.index.amqp;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.simulator.processing.alert.index.amqp.listener.RecommendationsGeneratedMessageHandler;

public class RecommendationsGeneratedUseCase implements RecommendationsGeneratedMessageHandler {

  @Override
  public SimulationDataIndexRequest handle(
      RecommendationsGenerated request) {

    throw new UnsupportedOperationException("Not implemented yet");
  }
}
