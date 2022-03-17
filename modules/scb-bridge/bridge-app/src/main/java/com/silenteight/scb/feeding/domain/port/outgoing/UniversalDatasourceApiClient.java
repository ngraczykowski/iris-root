package com.silenteight.scb.feeding.domain.port.outgoing;

import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;

public interface UniversalDatasourceApiClient {

  <T extends Feature> void registerAgentInputs(BatchCreateAgentInputsIn<T> agentInputs);
}
