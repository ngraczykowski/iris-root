package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

public interface UnstructuredFeatureExtractor {

  FeatureInput createFeatureInputs(HitComposite hitComposite);
}
