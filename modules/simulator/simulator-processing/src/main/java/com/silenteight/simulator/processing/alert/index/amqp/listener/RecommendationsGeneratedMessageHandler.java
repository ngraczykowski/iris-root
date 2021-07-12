package com.silenteight.simulator.processing.alert.index.amqp.listener;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;

public interface RecommendationsGeneratedMessageHandler {

  SimulationDataIndexRequest handle(RecommendationsGenerated request);
}
