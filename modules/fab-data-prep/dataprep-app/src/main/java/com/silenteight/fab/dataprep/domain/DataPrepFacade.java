package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.adapter.incoming.AlertDetailsFacade;
import com.silenteight.fab.dataprep.domain.ex.DataPrepException;
import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription;
import com.silenteight.fab.dataprep.domain.model.LearningData;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.proto.fab.api.v1.AlertMessageDetails;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.validation.Valid;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataPrepFacade {

  private final FeedingFacade feedingFacade;
  private final AlertDetailsFacade alertDetailsFacade;
  private final AlertParser alertParser;
  private final RegistrationService registrationService;
  private final AlertService alertService;
  private final LearningService learningService;
  @Valid
  private final AlertStateProperties alertStateProperties;

  public void processAlert(AlertMessageStored message) {
    if (message.getState() == State.NEW) {
      Try.of(() -> registerAlert(message))
          .getOrElseGet(e -> registerFailedAlert(message))
          .forEach(feedingFacade::etlAndFeedUds);
    } else {
      var analystDecision = alertStateProperties.getAnalystDecision(message.getState());
      registerLearningAlert(message, analystDecision)
          .forEach(learningService::feedWarehouse);
    }
  }

  private List<LearningData> registerLearningAlert(
      AlertMessageStored message, String analystDecision) {
    AlertMessagesDetailsResponse response = getAlertDetails(message);
    Map<String, ParsedAlertMessage> extractedAlerts = getExtractedAlerts(message, response);

    registerNewLearningAlert(extractedAlerts);

    return extractedAlerts.values()
        .stream()
        .map(parsedAlertMessage -> {
          var discriminator = getDiscriminator(parsedAlertMessage);
          String alertName = alertService.getAlertName(discriminator)
              .orElseThrow(() -> new DataPrepException("Alert not found"));
          return getLearningData(alertName, discriminator, analystDecision, parsedAlertMessage);
        })
        .collect(toList());
  }

  private void registerNewLearningAlert(Map<String, ParsedAlertMessage> extractedAlerts) {
    Map<String, ParsedAlertMessage> alertsToRegister = extractedAlerts.entrySet()
        .stream()
        .filter(entry -> {
          var discriminator = getDiscriminator(entry.getValue());
          return alertService.getAlertName(discriminator).isEmpty();
        })
        .collect(toMap(Entry::getKey, Entry::getValue));

    if (!alertsToRegister.isEmpty()) {
      log.debug("Registering learning data, count: {}", alertsToRegister.size());
      List<RegisteredAlert> registeredAlerts =
          registrationService.registerAlertsAndMatches(
              alertsToRegister); //TODO use method without batchId

      registeredAlerts.forEach(registeredAlert -> {
        var discriminator = getDiscriminator(registeredAlert);
        alertService.save(discriminator, registeredAlert.getAlertName());
      });

      registeredAlerts.forEach(feedingFacade::etlAndFeedUdsLearningData);
    }
  }

  private List<RegisteredAlert> registerAlert(AlertMessageStored message) {
    AlertMessagesDetailsResponse response = getAlertDetails(message);
    Map<String, ParsedAlertMessage> extractedAlerts = getExtractedAlerts(message, response);
    var registeredAlerts = registrationService.registerAlertsAndMatches(extractedAlerts);
    registeredAlerts.forEach(registeredAlert -> {
      var discriminator = getDiscriminator(registeredAlert);
      alertService.save(discriminator, registeredAlert.getAlertName());
    });
    return registeredAlerts;
  }

  private List<RegisteredAlert> registerFailedAlert(AlertMessageStored message) {
    return registrationService.registerFailedAlerts(of(message.getMessageName()),
        message.getBatchName(), AlertErrorDescription.EXTRACTION);
  }

  private Map<String, ParsedAlertMessage> getExtractedAlerts(
      AlertMessageStored message,
      AlertMessagesDetailsResponse alertsDetailsResponse) {
    return alertsDetailsResponse.getAlertsList()
        .stream()
        .collect(toMap(AlertMessageDetails::getMessageName, ad -> alertParser.parse(message, ad)));
  }

  private AlertMessagesDetailsResponse getAlertDetails(AlertMessageStored message) {
    return alertDetailsFacade.getAlertDetails(message);
  }

  private static String getDiscriminator(ParsedAlertMessage parsedAlertMessage) {
    return getDiscriminator(parsedAlertMessage.getSystemId(), parsedAlertMessage.getMessageId());
  }

  private static String getDiscriminator(RegisteredAlert registeredAlert) {
    return getDiscriminator(registeredAlert.getSystemId(), registeredAlert.getMessageId());
  }

  private static String getDiscriminator(String systemId, String messageId) {
    return systemId + "|" + messageId;
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
        .accessPermissionTag("AE")
        .build();
  }
}
