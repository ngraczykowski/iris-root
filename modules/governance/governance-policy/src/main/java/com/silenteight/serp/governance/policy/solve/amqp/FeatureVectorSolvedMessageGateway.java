package com.silenteight.serp.governance.policy.solve.amqp;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolvedEvent;

public interface FeatureVectorSolvedMessageGateway {
  void send(FeatureVectorSolvedEvent event);
}
