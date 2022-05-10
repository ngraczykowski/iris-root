package com.silenteight.payments.bridge.governance.solvingmodel.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.governance.solvingmodel.model.GovernanceClientException;
import com.silenteight.payments.bridge.governance.solvingmodel.port.CheckDefaultModelExistsUseCase;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class CheckDefaultModelExistsService implements CheckDefaultModelExistsUseCase {

  private final SolvingModelServiceClient solvingModelServiceClient;

  public boolean checkDefaultModelExists() {
    try {
      solvingModelServiceClient.getCurrentModel();
      return true;
    } catch (GovernanceClientException e) {
      return false;
    }
  }
}
