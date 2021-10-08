package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
class AlertDtoSupplier implements Supplier<AlertMessageDto> {

  private final UUID identifier;

  private @Autowired AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private AlertMessageDto result;

  @Override
  public AlertMessageDto get() {
    if (result == null) {
      result = alertMessagePayloadUseCase.findByAlertMessageId(identifier);
    }
    return result;
  }

}
