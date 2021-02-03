package com.silenteight.serp.governance.policy.solve.amqp;

import com.silenteight.governance.api.v1.FeatureVectorSolvedEvent;

public interface FeatureVectorSolvedMessageGateway {
  void send(FeatureVectorSolvedEvent event);
}
