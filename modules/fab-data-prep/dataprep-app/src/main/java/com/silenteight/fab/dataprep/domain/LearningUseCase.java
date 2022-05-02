package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.ex.DataPrepException;
import com.silenteight.fab.dataprep.domain.model.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
class LearningUseCase extends BaseUseCase {

  private static final String ACCESS_PERMISSION_TAG = "AE";

  private final LearningService learningService;
  private final FeedingFacade feedingFacade;

  public LearningUseCase(
      AlertService alertService,
      RegistrationService registrationService,
      FeedingFacade feedingFacade,
      LearningService learningService) {
    super(alertService, registrationService);
    this.feedingFacade = feedingFacade;
    this.learningService = learningService;
  }

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
          AlertItem alertItem = alertService.getAlertItem(discriminator)
              .orElseThrow(() -> new DataPrepException("Alert not found"));
          return getLearningData(
              alertItem.getAlertName(),
              alertItem.getMessageName(),
              analystDecision,
              parsedAlertMessage);
        })
        .collect(toList());
  }

  private void registerNewLearningAlerts(Map<String, ParsedAlertMessage> extractedAlerts) {
    List<RegisteredAlert> registeredNotInUdsAlerts = registerNewAlerts(extractedAlerts);

    registeredNotInUdsAlerts.forEach(registeredAlert -> {
      var discriminator = registeredAlert.getDiscriminator();
      feedingFacade.etlAndFeedUds(registeredAlert);
      alertService.setAlertState(discriminator, AlertState.IN_UDS);
    });
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
