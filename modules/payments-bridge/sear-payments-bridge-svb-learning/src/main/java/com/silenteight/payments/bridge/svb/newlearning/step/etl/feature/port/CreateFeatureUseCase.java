package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.port;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import java.util.List;

public interface CreateFeatureUseCase {

  List<AgentInput> createFeatureInputs(
      List<EtlHit> etlHits, RegisterAlertResponse registerAlert);
}
