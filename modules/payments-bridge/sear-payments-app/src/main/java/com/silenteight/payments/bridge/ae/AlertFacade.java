package com.silenteight.payments.bridge.ae;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertFacade {

  private final SolveAlertUseCase solveAlertUseCase;

  public void solveAlert(long alertId) {
    solveAlertUseCase.solveAlert(alertId);
  }
}
