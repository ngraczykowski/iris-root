package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

public interface FeatureExtractor {

  FeatureInput createFeatureInputs(EtlHit etlHit);
}
