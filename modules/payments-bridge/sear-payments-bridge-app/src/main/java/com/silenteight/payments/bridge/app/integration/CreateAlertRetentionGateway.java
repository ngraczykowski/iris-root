package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class CreateAlertRetentionGateway implements CreateAlertRetentionPort {

  private final CreateAlertDataRetentionUseCase createAlertRetentionUseCase;

  @Override
  public void create(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(la -> new AlertDataRetention(la.getAlertName(), la.getAlertTime()))
        .collect(Collectors.toList());
    createAlertRetentionUseCase.create(alerts);
  }
}
