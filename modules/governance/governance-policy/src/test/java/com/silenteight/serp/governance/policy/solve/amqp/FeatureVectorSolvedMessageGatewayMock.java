package com.silenteight.serp.governance.policy.solve.amqp;

import lombok.Getter;

import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

public class FeatureVectorSolvedMessageGatewayMock implements FeatureVectorSolvedMessageGateway {

  @Getter
  private FeatureVectorSolvedEvent lastEvent;

  @Override
  public void send(FeatureVectorSolvedEvent event) {
    lastEvent = event;
  }

}