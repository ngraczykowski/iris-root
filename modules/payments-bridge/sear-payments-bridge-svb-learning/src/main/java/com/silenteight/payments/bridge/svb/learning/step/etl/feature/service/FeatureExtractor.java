package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;

public interface FeatureExtractor {

  FeatureInput createFeatureInputs(EtlHit etlHit);

  String name();
}
