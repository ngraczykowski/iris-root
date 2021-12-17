package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModel;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService;

import org.springframework.stereotype.Component;

@Component
class DefaultModelServiceAdapter implements DefaultModelService {

  @Override
  public DefaultModel getForSolving() {
    // TODO ALL-495
    return new DefaultModel();
  }
}
