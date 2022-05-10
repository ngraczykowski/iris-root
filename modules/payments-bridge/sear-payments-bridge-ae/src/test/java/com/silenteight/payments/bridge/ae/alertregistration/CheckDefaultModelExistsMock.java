package com.silenteight.payments.bridge.ae.alertregistration;

import com.silenteight.payments.bridge.governance.solvingmodel.port.CheckDefaultModelExistsUseCase;

public class CheckDefaultModelExistsMock implements CheckDefaultModelExistsUseCase {

  @Override
  public boolean checkDefaultModelExists() {
    return true;
  }
}
