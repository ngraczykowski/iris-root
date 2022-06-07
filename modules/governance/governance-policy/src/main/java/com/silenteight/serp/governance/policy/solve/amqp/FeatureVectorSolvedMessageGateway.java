package com.silenteight.serp.governance.policy.solve.amqp;


import com.silenteight.solving.api.v1.FeatureVectorSolvedEventBatch;

public interface FeatureVectorSolvedMessageGateway {

  String ID = "featureVectorSolvedMessageGateway";

  void send(FeatureVectorSolvedEventBatch event);
}
