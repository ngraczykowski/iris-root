package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CheckAlertRegisteredPort;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class IngestService {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final DataSourceIngestService dataSourceIngestService;
  private final WarehouseIngestService warehouseIngestService;
  private final CheckAlertRegisteredPort checkAlertRegisteredPort;

  void ingest(LearningAlert learningAlert) {
    if (learningAlert.getMatches().size() == 0)
      return;

    if (checkAlertRegisteredPort.isAlertRegistered(learningAlert.getAlertRegistration())) {
      // TODO: alertName != AE Alert name.
      warehouseIngestService.ingestAnalystSolution(learningAlert);
      return;
    }

    var response = registerAlertUseCase.register(List.of(learningAlert.toRegisterAlertRequest()));
    learningAlert.setAlertMatchNames(response.get(0));

    dataSourceIngestService.createValues(learningAlert);

    warehouseIngestService.ingestReportData(learningAlert);
    warehouseIngestService.ingestAnalystSolution(learningAlert);
  }
}
