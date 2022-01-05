package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.port;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import java.util.List;
import java.util.Map;

public interface CreateFeatureUseCase {

  Map<String, List<FeatureInput>> createFeatureInputs(EtlHit etlHit);
}
