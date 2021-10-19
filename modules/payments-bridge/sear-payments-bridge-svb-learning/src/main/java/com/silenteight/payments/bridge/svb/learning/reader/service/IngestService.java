package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CheckAlertRegisteredPort;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IngestService {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final DataSourceIngestService dataSourceIngestService;
  private final IndexAlertUseCase indexAlertUseCase;
  private final CheckAlertRegisteredPort checkAlertRegisteredPort;
  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final LearningWarehouseMapper warehouseMapper;

  void ingest(LearningAlert learningAlert) {
    if (learningAlert.getMatches().size() == 0)
      return;

    var alertBuilder = alertBuilderFactory.newBuilder()
        .setDiscriminator(learningAlert.getDiscriminator())
        .addPayload(warehouseMapper.makeAnalystDecision(learningAlert.getAnalystDecision()));

    if (!checkAlertRegisteredPort.isAlertRegistered(learningAlert.getAlertRegistration())) {
      var response = registerAlertUseCase.register(learningAlert.toRegisterAlertRequest());
      learningAlert.setAlertMatchNames(response);
      alertBuilder.setName(response.getAlertName());

      dataSourceIngestService.createValues(learningAlert);
      alertBuilder.addPayload(warehouseMapper.makeAlert(learningAlert));
    }

    indexAlertUseCase.index(alertBuilder.build());
  }
}
