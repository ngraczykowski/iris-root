package com.silenteight.serp.governance.policy.solve.amqp;

import lombok.Getter;

import com.silenteight.solving.api.v1.FeatureVectorSolvedEventBatch;

public class FeatureVectorSolvedMessageGatewayMock implements FeatureVectorSolvedMessageGateway {

  @Getter
  private FeatureVectorSolvedEventBatch lastEvent;

  @Override
  public void send(FeatureVectorSolvedEventBatch event) {
    lastEvent = event;
  }
}
