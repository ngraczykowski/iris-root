package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;

@RequiredArgsConstructor
public class AlertFacade {

  private final AlertMapper alertMapper;

  public RawAlert map(Alert alert) {
    return alertMapper.map(alert);
  }
}
