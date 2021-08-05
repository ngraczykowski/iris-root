package com.silenteight.payments.bridge.firco.ingress;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.input.AlertDataCenterDto;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IngressFacade {

  private final HandleIncomingAlertsUseCase handleIncomingAlertsUseCase;

  public void handleIncomingAlerts(List<AlertDataCenterDto> alertDataCenterDtos) {
    handleIncomingAlertsUseCase.handleIncomingAlerts(alertDataCenterDtos);
  }
}
