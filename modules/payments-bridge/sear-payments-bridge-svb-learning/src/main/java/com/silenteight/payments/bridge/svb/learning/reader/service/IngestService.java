package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.port.AddAlertLabelUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.event.AlreadySolvedAlertEvent;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;
import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest.fromLearningAlerts;
import static com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert.getAlertLabelLearningCsv;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
class IngestService {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final AddAlertLabelUseCase addAlertLabelUseCase;
  private final DataSourceIngestService dataSourceIngestService;
  private final FindRegisteredAlertUseCase findRegisteredAlertUseCase;
  private final CreateAlertRetentionPort createAlertRetentionPort;
  private final DecisionMapper decisionMapper;
  private final IndexLearningAlertPort indexLearningAlertPort;
  private final ApplicationEventPublisher eventPublisher;

  void ingest(LearningAlertBatch batch) {
    var alerts = getLearningAlerts(batch);
    if (alerts.isEmpty()) {
      return;
    }

    fillUpAnalystDecision(alerts);

    // Only solving alerts are registered.
    var registeredAlertMap = buildRegisteredAlertMap(alerts);
    eventPublisher.publishEvent(new AlreadySolvedAlertEvent(registeredAlertMap.size()));

    var unregisteredLearningAlerts = getUnregisteredAlerts(alerts, registeredAlertMap);
    processUnregistered(unregisteredLearningAlerts, batch.getErrors());

    var registeredLearningAlerts = getRegisteredAlerts(alerts, unregisteredLearningAlerts);
    processRegistered(registeredLearningAlerts, registeredAlertMap);
  }

  private static List<LearningAlert> getLearningAlerts(LearningAlertBatch batch) {
    return batch.getLearningAlerts().stream()
        .filter(a -> a.getMatches().size() > 0)
        .collect(toList());
  }

  private void processRegistered(
      List<LearningAlert> registeredLearningAlerts,
      Map<String, RegisteredAlert> registeredAlertMap) {

    var indexAlertsRequest =
        registeredAlertMap.values().stream()
            .map(ra -> fromLearningAlerts(ra, registeredLearningAlerts))
            .collect(toList());

    if (!registeredLearningAlerts.isEmpty()) {
      addLearningLabels(registeredLearningAlerts);
      indexLearningAlertPort.indexForLearning(indexAlertsRequest);
      createAlertRetentionPort.create(registeredLearningAlerts);
    }
  }

  private void addLearningLabels(List<LearningAlert> registeredLearningAlerts) {
    var alertIds =
        registeredLearningAlerts.stream()
            .map(LearningAlert::getAlertId)
            .collect(toList());

    addAlertLabelUseCase.invoke(alertIds, List.of(getAlertLabelLearningCsv()));
  }

  private static List<LearningAlert> getRegisteredAlerts(
      List<LearningAlert> alerts,
      List<LearningAlert> unregisteredAlerts) {

    return alerts.stream()
        .filter(alert -> !unregisteredAlerts.contains(alert))
        .collect(toList());
  }

  private static List<LearningAlert> getUnregisteredAlerts(
      List<LearningAlert> alerts,
      Map<String, RegisteredAlert> registeredAlertMap) {

    return alerts.stream()
        .filter(la -> !registeredAlertMap.containsKey(la.getDiscriminator()))
        .collect(toList());
  }

  private void fillUpAnalystDecision(List<LearningAlert> alerts) {
    alerts.forEach(alert -> {
      var analystDecision = alert.getAnalystDecision();
      alert.setDecision(decisionMapper.map(
          analystDecision.getPreviousStatuses(), analystDecision.getStatus()));
    });
  }

  private Map<String, RegisteredAlert> buildRegisteredAlertMap(List<LearningAlert> alerts) {
    var registeredAlertRequests = alerts.stream()
        .map(LearningAlert::toFindRegisterAlertRequest)
        .distinct().collect(toList());
    return findRegisteredAlertUseCase.find(registeredAlertRequests).stream()
        .collect(toMap(RegisteredAlert::getDiscriminator, Function.identity()));
  }

  private void processUnregistered(
      List<LearningAlert> unregisteredLearningAlerts, List<ReadAlertError> errors) {

    if (!unregisteredLearningAlerts.isEmpty()) {
      register(unregisteredLearningAlerts);
      dataSourceIngestService.createValues(unregisteredLearningAlerts, errors);
      indexLearningAlertPort.index(unregisteredLearningAlerts);
      createAlertRetentionPort.create(unregisteredLearningAlerts);
    }
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
  }
}
