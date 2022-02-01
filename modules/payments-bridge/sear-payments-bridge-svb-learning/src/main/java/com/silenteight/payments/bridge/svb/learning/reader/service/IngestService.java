package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.port.AddAlertLabelUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.svb.learning.event.AlreadySolvedAlertEvent;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;
import com.silenteight.payments.bridge.svb.learning.reader.domain.exception.NoCorrespondingAlertException;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;
import com.silenteight.payments.bridge.svb.migration.DecisionMapper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;

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
  private final CreateAlertDataRetentionUseCase createAlertDataRetentionUseCase;
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
    var registeredAlerts = buildRegisteredAlerts(alerts);
    eventPublisher.publishEvent(new AlreadySolvedAlertEvent(registeredAlerts.size()));

    var unregisteredLearningAlerts = getUnregisteredAlerts(alerts, registeredAlerts);
    processUnregistered(unregisteredLearningAlerts, batch.getErrors());

    var registeredLearningAlerts = getRegisteredAlerts(alerts, unregisteredLearningAlerts);
    processRegistered(registeredLearningAlerts, registeredAlerts);
  }

  private static List<LearningAlert> getLearningAlerts(LearningAlertBatch batch) {
    return batch.getLearningAlerts().stream()
        .filter(a -> a.getMatches().size() > 0)
        .collect(toList());
  }

  private void processRegistered(
      List<LearningAlert> registeredLearningAlerts,
      List<RegisteredAlert> registeredAlerts) {

    var indexAlertsRequest =
        registeredAlerts.stream()
            .map(ra -> fromLearningAlerts(ra, registeredLearningAlerts))
            .flatMap(List::stream)
            .collect(toList());

    if (!registeredLearningAlerts.isEmpty()) {
      addLearningLabels(registeredAlerts);
      indexLearningAlertPort.indexForLearning(indexAlertsRequest);
      createAlertDataRetentionUseCase.create(
          registeredAlerts
              .stream()
              .map(a -> getAlertDataRetention(registeredLearningAlerts, a))
              .collect(toList()));
    }
  }

  @Nonnull
  private static AlertDataRetention getAlertDataRetention(
      List<LearningAlert> registeredLearningAlerts, RegisteredAlert a) {
    return new AlertDataRetention(
        a.getAlertName(),
        registeredLearningAlerts
            .stream()
            .filter(r -> r.getSystemId().equals(a.getSystemId()))
            .findFirst()
            .orElseThrow(
                () -> new NoCorrespondingAlertException(
                    "There is no corresponding learning alert for registered alert"))
            .getAlertTime());
  }

  private void addLearningLabels(List<RegisteredAlert> registeredAlerts) {
    var alertNames =
        registeredAlerts.stream()
            .map(RegisteredAlert::getAlertName)
            .collect(toList());

    addAlertLabelUseCase.invoke(alertNames, List.of(getAlertLabelLearningCsv()));
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
      List<RegisteredAlert> registeredAlerts) {

    return alerts.stream()
        .filter(la -> !registeredAlerts
            .stream()
            .map(RegisteredAlert::getSystemId)
            .collect(toList())
            .contains(la.getSystemId()))
        .collect(toList());
  }

  private void fillUpAnalystDecision(List<LearningAlert> alerts) {
    alerts.forEach(alert -> {
      var analystDecision = alert.getAnalystDecision();
      alert.setDecision(decisionMapper.map(
          analystDecision.getPreviousStatuses(), analystDecision.getStatus()));
    });
  }

  private List<RegisteredAlert> buildRegisteredAlerts(List<LearningAlert> alerts) {
    var registeredAlertRequests = alerts.stream()
        .map(LearningAlert::getSystemId)
        .distinct().collect(toList());
    return findRegisteredAlertUseCase.find(registeredAlertRequests);
  }

  private void processUnregistered(
      List<LearningAlert> unregisteredLearningAlerts, List<ReadAlertError> errors) {

    if (!unregisteredLearningAlerts.isEmpty()) {
      register(unregisteredLearningAlerts);
      dataSourceIngestService.createValues(unregisteredLearningAlerts, errors);
      indexLearningAlertPort.index(unregisteredLearningAlerts);
      createAlertDataRetentionUseCase.create(unregisteredLearningAlerts
          .stream()
          .map(a -> new AlertDataRetention(a.getAlertName(), a.getAlertTime()))
          .collect(toList()));
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
      var alert = learningAlertsMap.get(alertResponse.getSystemId());
      alert.setAlertMatchNames(alertResponse);
    });
  }
}
