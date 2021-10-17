package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class IngestService {

  private final DataSourceIngestService dataSourceIngestService;
  private final RegisterAlertUseCase registerAlertUseCase;

  void ingest(LearningAlert learningAlert) {

    if (learningAlert.getMatches().size() == 0)
      return;

    var response = registerAlertUseCase.register(List.of(learningAlert.toRegisterAlertRequest()));
    learningAlert.setAlertMatchNames(response.get(0));

    dataSourceIngestService.createValues(learningAlert);
  }
}
