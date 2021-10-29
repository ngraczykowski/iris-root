package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;
import com.silenteight.payments.bridge.svb.learning.reader.port.CheckAlertRegisteredPort;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory.AlertBuilder;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest.fromLearningAlerts;
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

  void ingest(LearningAlertBatch batch) {
    var alerts = batch.getLearningAlerts().stream()
        .filter(a -> a.getMatches().size() > 0)
        .collect(toList());

    if (alerts.isEmpty()) {
      return;
    }

    var alertRegistrations = alerts.stream()
        .map(LearningAlert::toFindRegisterAlertRequest)
        .distinct().collect(toList());
    var existing = checkAlertRegisteredPort.findAlertRegistered(alertRegistrations);

    var unregisteredAlerts = alerts.stream()
        .filter(learningAlert -> existing
            .stream()
            .filter(e -> e.getDiscriminator().equals(learningAlert.getDiscriminator()))
            .findAny()
            .isEmpty())
        .collect(toList());
    if (unregisteredAlerts.size() > 0) {
      processUnregistered(unregisteredAlerts, batch.getErrors());
    }

    var registeredAlerts = new ArrayList<>(alerts);
    registeredAlerts.removeAll(unregisteredAlerts);
    var indexAlertsRequest =
        existing.stream().map(ra -> fromLearningAlerts(ra, registeredAlerts)).collect(toList());
    if (registeredAlerts.size() > 0) {
      processRegistered(indexAlertsRequest);
    }
  }

  private void processUnregistered(
      List<LearningAlert> learningAlerts, List<ReadAlertError> errors) {
    register(learningAlerts);
    dataSourceIngestService.createValues(learningAlerts, errors);
    index(learningAlerts);
  }

  private void processRegistered(List<IndexRegisterAlertRequest> indexRegisterAlertRequest) {
    indexForLearning(indexRegisterAlertRequest);
  }

  private List<LearningAlert> register(List<LearningAlert> learningAlerts) {
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

    return new ArrayList<>(learningAlertsMap.values());
  }

  private void index(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(learningAlert -> {
          var alertBuilder = createIndexAlertBuilder(learningAlert)
              .addPayload(warehouseMapper.makeAlert(learningAlert));
          learningAlert.getMatches().forEach(m -> alertBuilder
              .newMatch()
              .setName(m.getMatchName())
              .setDiscriminator(m.getMatchName())
              .addPayload(warehouseMapper.makeMatch(m))
              .finish());
          return alertBuilder.build();
        })
        .collect(toList());
    indexAlertUseCase.index(alerts, RequestOrigin.LEARNING);
  }

  private void indexForLearning(List<IndexRegisterAlertRequest> indexRegisterAlertRequest) {
    var alerts = indexRegisterAlertRequest.stream()
        .map(indexAlert -> {
          var alertBuilder = createIndexAlertBuilder(indexAlert.getLearningAlert());
          indexAlert.getMatchNames().forEach(matchName -> alertBuilder
              .newMatch()
              .setName(matchName)
              .setDiscriminator(matchName)
              .finish());
          return alertBuilder.build();
        })
        .collect(toList());
    indexAlertUseCase.index(alerts, RequestOrigin.LEARNING);
  }

  private AlertBuilder createIndexAlertBuilder(LearningAlert learningAlert) {
    return alertBuilderFactory.newBuilder()
        .setDiscriminator(learningAlert.getDiscriminator())
        .setName(learningAlert.getAlertName())
        .addPayload(warehouseMapper.makeAnalystDecision(learningAlert.getAnalystDecision()));
  }
}
