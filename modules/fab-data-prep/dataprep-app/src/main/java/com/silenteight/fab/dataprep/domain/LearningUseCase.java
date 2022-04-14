package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.ex.DataPrepException;
import com.silenteight.fab.dataprep.domain.model.*;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.silenteight.fab.dataprep.domain.RegistrationConverter.convert;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
class LearningUseCase {

  private static final String ACCESS_PERMISSION_TAG = "AE";

  private final AlertService alertService;
  private final LearningService learningService;
  private final FeedingFacade feedingFacade;
  private final RegistrationService registrationService;

  void handle(LearningRequest learningRequest) {
    registerLearningAlert(
        learningRequest.getExtractedAlerts(), learningRequest.getAnalystDecision())
        .forEach(learningService::feedWarehouse);
  }

  private List<LearningData> registerLearningAlert(
      Map<String, ParsedAlertMessage> extractedAlerts,
      String analystDecision) {
    registerNewLearningAlerts(extractedAlerts);

    return extractedAlerts.values()
        .stream()
        .map(parsedAlertMessage -> {
          var discriminator = parsedAlertMessage.getDiscriminator();
          String alertName = alertService.getAlertItem(discriminator)
              .map(AlertItem::getAlertName)
              .orElseThrow(() -> new DataPrepException("Alert not found"));
          return getLearningData(alertName, discriminator, analystDecision, parsedAlertMessage);
        })
        .collect(toList());
  }

  private void registerNewLearningAlerts(Map<String, ParsedAlertMessage> extractedAlerts) {
    List<RegisteredAlert> registeredNotInUdsAlerts = new LinkedList<>();
    Map<String, ParsedAlertMessage> alertsToRegister = new HashMap<>();

    extractedAlerts.forEach((messageName, parsedAlertMessage) -> alertService
        .getAlertItem(parsedAlertMessage.getDiscriminator())
        .ifPresentOrElse(
            alertItem -> {
              if (alertItem.getState() == AlertState.REGISTERED) {
                registeredNotInUdsAlerts.add(convert(alertItem, parsedAlertMessage));
              }
            },
            () -> alertsToRegister.put(messageName, parsedAlertMessage)));

    if (!alertsToRegister.isEmpty()) {
      log.debug("Registering learning data, count: {}", alertsToRegister.size());
      registrationService.registerAlertsAndMatches(alertsToRegister)
          .forEach(registeredAlert -> {
            saveAlertItem(registeredAlert);
            registeredNotInUdsAlerts.add(registeredAlert);
          });
    }

    registeredNotInUdsAlerts.forEach(registeredAlert -> {
      var discriminator = registeredAlert.getDiscriminator();
      feedingFacade.etlAndFeedUdsLearningData(registeredAlert);
      alertService.setAlertState(discriminator, AlertState.IN_UDS);
    });
  }

  private void saveAlertItem(RegisteredAlert registeredAlert) {
    List<String> matchNames = registeredAlert.getMatches()
        .stream()
        .map(Match::getMatchName)
        .collect(toList());
    var discriminator = registeredAlert.getDiscriminator();
    alertService.save(discriminator, registeredAlert.getAlertName(), matchNames);
  }

  private static LearningData getLearningData(
      String alertName,
      String discriminator,
      String analystDecision,
      ParsedAlertMessage parsedAlertMessage) {
    return LearningData.builder()
        .alertName(alertName)
        .analystDecision(analystDecision)
        .originalAnalystDecision(parsedAlertMessage.getCurrentStatusName())
        .analystDecisionModifiedDateTime(parsedAlertMessage.getCurrentActionDateTime())
        .analystReason(parsedAlertMessage.getCurrentActionComment())
        .discriminator(discriminator)
        .accessPermissionTag(ACCESS_PERMISSION_TAG)
        .build();
  }
}
