package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlertFacade {

  private final AlertMapper alertMapper;

  public Alert map(com.silenteight.hsbc.bridge.rest.model.input.Alert alert) {
    return alertMapper.map(alert);
  }
}
