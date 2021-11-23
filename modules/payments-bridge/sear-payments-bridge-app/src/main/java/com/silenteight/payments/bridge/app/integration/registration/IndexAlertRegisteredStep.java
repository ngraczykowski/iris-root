package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertRegisteredUseCase;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class IndexAlertRegisteredStep {

  private final AlertMessageStatusUseCase alertMessageStatusUseCase;
  private final IndexAlertRegisteredUseCase indexAlertRegisteredUseCase;

  void invoke(Context ctx) {
    var status = alertMessageStatusUseCase.getStatus(ctx.getAlertId());
    indexAlertRegisteredUseCase.index(
        ctx.getAlertData(), ctx.getAlertMessageDto(), ctx.getAeAlert(), status.name());
  }
}
