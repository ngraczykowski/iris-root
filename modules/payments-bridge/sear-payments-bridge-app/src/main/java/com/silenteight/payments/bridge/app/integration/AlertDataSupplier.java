package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

@RequiredArgsConstructor
class AlertDataSupplier implements Supplier<AlertData> {

  private final String identifier;

  private @Autowired AlertMessageUseCase alertMessageUseCase;
  private AlertData result;

  @Override
  public AlertData get() {
    if (result == null) {
      result = alertMessageUseCase.findByAlertMessageId(identifier);
    }
    return result;
  }
}
