package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;

import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Set;

@Component
@RequiredArgsConstructor
class AlertRetentionStep {

  private final CreateAlertDataRetentionUseCase createAlertRetentionUseCase;

  void invoke(Context ctx) {
    var alertRetention = new AlertDataRetention(ctx.getAlertName(),
        ctx.getAlertMessageDto().getFilteredAt(ZoneOffset.UTC));
    createAlertRetentionUseCase.create(Set.of(alertRetention));
  }
}
