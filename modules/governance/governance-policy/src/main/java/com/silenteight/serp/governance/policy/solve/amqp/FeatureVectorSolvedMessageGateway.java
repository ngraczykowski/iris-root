package com.silenteight.serp.governance.policy.solve.amqp;

import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

public interface FeatureVectorSolvedMessageGateway {

  void send(FeatureVectorSolvedEvent event);
}
