package com.silenteight.simulator.processing.alert.index.amqp.listener;

import lombok.NonNull;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;

public interface RecommendationsGeneratedMessageHandler {

  void handle(@NonNull RecommendationsGenerated request);
}
