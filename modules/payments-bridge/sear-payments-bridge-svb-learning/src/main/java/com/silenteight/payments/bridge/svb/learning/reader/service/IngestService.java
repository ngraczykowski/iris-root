package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;
import com.silenteight.payments.bridge.svb.learning.reader.domain.RegisteredAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;
import com.silenteight.payments.bridge.svb.learning.reader.port.FindRegisteredAlertPort;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest.fromLearningAlerts;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
class IngestService {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final DataSourceIngestService dataSourceIngestService;
  private final FindRegisteredAlertPort findRegisteredAlertPort;
  private final CreateAlertRetentionPort createAlertRetentionPort;
  private final DecisionMapper decisionMapper;
  private final IndexLearningAlertPort indexLearningAlertPort;

  void ingest(LearningAlertBatch batch) {
    var alerts = batch.getLearningAlerts().stream()
        .filter(a -> a.getMatches().size() > 0)
        .collect(toList());

    if (alerts.isEmpty()) {
      return;
    }

    alerts.forEach(alert ->
        alert.setDecision(decisionMapper.map(alert.getAnalystDecision().getStatus())));

    var registeredAlertMap = buildRegisteredAlertMap(alerts);
    var unregisteredAlerts = alerts.stream()
        .filter(la -> !registeredAlertMap.containsKey(la.getDiscriminator()))
        .collect(toList());
    if (unregisteredAlerts.size() > 0) {
      processUnregistered(unregisteredAlerts, batch.getErrors());
    }

    var registeredAlerts = new ArrayList<>(alerts);
    registeredAlerts.removeAll(unregisteredAlerts);
    var indexAlertsRequest =
        registeredAlertMap.values().stream()
            .map(ra -> fromLearningAlerts(ra, registeredAlerts)).collect(toList());
    if (registeredAlerts.size() > 0) {
      indexLearningAlertPort.indexForLearning(indexAlertsRequest);
      createAlertRetentionPort.create(registeredAlerts);
    }
  }

  private Map<String, RegisteredAlert> buildRegisteredAlertMap(List<LearningAlert> alerts) {
    var registeredAlertRequests = alerts.stream()
        .map(LearningAlert::toFindRegisterAlertRequest)
        .distinct().collect(toList());
    return findRegisteredAlertPort.find(registeredAlertRequests).stream()
        .collect(toMap(RegisteredAlert::getDiscriminator, Function.identity()));
  }

  private void processUnregistered(
      List<LearningAlert> learningAlerts, List<ReadAlertError> errors) {
    register(learningAlerts);
    dataSourceIngestService.createValues(learningAlerts, errors);
    indexLearningAlertPort.index(learningAlerts);
    createAlertRetentionPort.create(learningAlerts);
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

}
