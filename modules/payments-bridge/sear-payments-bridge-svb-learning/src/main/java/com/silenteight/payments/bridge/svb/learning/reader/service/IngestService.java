package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CheckAlertRegisteredPort;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory.Builder;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
class IngestService {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final DataSourceIngestService dataSourceIngestService;
  private final IndexAlertUseCase indexAlertUseCase;
  private final CheckAlertRegisteredPort checkAlertRegisteredPort;
  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final LearningWarehouseMapper warehouseMapper;

  void ingest(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .filter(a -> a.getMatches().size() > 0)
        .collect(toList());

    if (alerts.isEmpty()) {
      return;
    }

    var alertRegistrations = alerts.stream()
        .map(LearningAlert::getAlertRegistration)
        .distinct().collect(toList());
    var existing = checkAlertRegisteredPort.findAlertRegistered(alertRegistrations);

    var unregisteredAlerts = alerts.stream()
        .filter(learningAlert -> !existing.contains(learningAlert.getAlertRegistration()))
        .collect(toList());
    if (unregisteredAlerts.size() > 0) {
      processUnregistered(unregisteredAlerts);
    }

    var registeredAlerts = new ArrayList<>(alerts);
    registeredAlerts.removeAll(unregisteredAlerts);
    if (registeredAlerts.size() > 0) {
      processRegistered(registeredAlerts);
    }
  }

  private void processUnregistered(List<LearningAlert> learningAlerts) {
    register(learningAlerts);
    index(learningAlerts);
  }

  private void processRegistered(List<LearningAlert> learningAlerts) {
    indexRegistered(learningAlerts);
  }

  private void register(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(LearningAlert::toRegisterAlertRequest)
        .collect(toList());

    var responses = registerAlertUseCase.batchRegistration(alerts);

    var learningAlertsMap = learningAlerts.stream()
        .collect(toMap(LearningAlert::getAlertId, Function.identity()));
    responses.forEach(alertResponse -> {
      var alert = learningAlertsMap.get(alertResponse.getAlertId());
      alert.setAlertMatchNames(alertResponse);
    });

    dataSourceIngestService.createValues(learningAlerts);
  }

  private void index(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(learningAlert ->
          createIndexAlertBuilder(learningAlert)
            .setName(learningAlert.getAlertName())
            .addPayload(warehouseMapper.makeAnalystDecision(learningAlert.getAnalystDecision()))
            .addPayload(warehouseMapper.makeAlert(learningAlert)).build())
        .collect(toList());
    indexAlertUseCase.index(alerts, RequestOrigin.LEARNING);
  }

  private void indexRegistered(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(this::createIndexAlertBuilder)
        .map(Builder::build)
        .collect(toList());
    indexAlertUseCase.index(alerts, RequestOrigin.LEARNING);
  }

  private Builder createIndexAlertBuilder(LearningAlert learningAlert) {
    return alertBuilderFactory.newBuilder()
        .setDiscriminator(learningAlert.getDiscriminator())
        .addPayload(warehouseMapper.makeAnalystDecision(learningAlert.getAnalystDecision()));
  }

}
