package com.silenteight.serp.governance.policy.solve.amqp;

import lombok.Getter;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolvedEvent;

public class FeatureVectorSolvedMessageGatewayMock implements FeatureVectorSolvedMessageGateway {

  @Getter
  private FeatureVectorSolvedEvent lastEvent;

  @Override
  public void send(FeatureVectorSolvedEvent event) {
    lastEvent = event;
  }

}