package com.silenteight.payments.bridge.governance.core.solvingmodel.service;

import com.silenteight.payments.bridge.governance.core.solvingmodel.model.Model;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.GetCurrentModelPort;

import org.springframework.stereotype.Service;

@Service
class GetCurrentModelService implements GetCurrentModelPort {

  @Override
  public Model getCurrentModel() {
    return null;
  }
}
