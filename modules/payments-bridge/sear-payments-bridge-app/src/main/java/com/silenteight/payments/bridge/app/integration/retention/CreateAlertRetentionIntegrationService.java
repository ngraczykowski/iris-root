package com.silenteight.payments.bridge.app.integration.retention;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CreateAlertRetentionIntegrationService implements CreateAlertRetentionPort {

  private final CreateAlertDataRetentionUseCase createAlertRetentionUseCase;

  @Override
  public void create(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(la -> new AlertDataRetention(la.getAlertName(), la.getAlertTime()))
        .collect(Collectors.toList());
    createAlertRetentionUseCase.create(alerts);
  }
}
