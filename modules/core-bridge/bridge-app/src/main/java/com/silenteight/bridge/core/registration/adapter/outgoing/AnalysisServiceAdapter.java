package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.domain.port.outgoing.Analysis;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModel;

import org.springframework.stereotype.Component;

@Component
class AnalysisServiceAdapter implements AnalysisService {

  @Override
  public Analysis create(DefaultModel defaultModel) {
    // TODO ALL-496
    return new Analysis("analysis_name");
  }
}
