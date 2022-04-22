package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.adapter.incoming.AlertDetailsFacade;
import com.silenteight.fab.dataprep.domain.model.*;
import com.silenteight.proto.fab.api.v1.AlertMessageDetails;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
  @Valid
  private final AlertStateProperties alertStateProperties;

  private final LearningUseCase learningUseCase;
  private final SolvingUseCase solvingUseCase;

  public void processAlert(AlertMessageStored message) {
    Map<String, ParsedAlertMessage> extractedAlerts = getExtractedAlerts(message);
    processAlert(extractedAlerts, message.getState());
  }

  private void processAlert(Map<String, ParsedAlertMessage> extractedAlerts, State state) {
    if (state == State.NEW) {
      solvingUseCase.handle(SolvingRequest.builder()
          .extractedAlerts(extractedAlerts)
          .build());
    } else {
      learningUseCase.handle(LearningRequest.builder()
          .analystDecision(alertStateProperties.getAnalystDecision(state))
          .extractedAlerts(extractedAlerts)
          .build());
    }
  }

  private void failedToParseMessage(AlertMessageStored message) {
    String batchName = message.getBatchName();
    registerFailedAlert(message.getMessageName(), batchName, "")
        .forEach(alertName -> feedingFacade.notifyAboutError(batchName, alertName));
  }

  public void processAlertFailed(AlertMessageStored message) {
    log.debug("Process alert failed: {}", message);
    Try.of(() -> getExtractedAlerts(message))
        .onSuccess(extractedAlerts -> extractedAlerts.forEach((messageName, value) -> {
          var discriminator = value.getDiscriminator();
          var batchName = value.getBatchName();
          alertService.getAlertItem(discriminator)
              .map(AlertItem::getAlertName)
              .map(List::of)
              .orElseGet(() -> registerFailedAlert(messageName, batchName, discriminator))
              .forEach(alertName -> feedingFacade.notifyAboutError(batchName, alertName));
        }))
        .onFailure(e -> {
          log.warn("Unable to parse message", e);
          failedToParseMessage(message);
        });
  }

  private List<String> registerFailedAlert(
      String messageName,
      String batchName,
      String discriminator) {
    return registrationService.registerFailedAlerts(of(messageName),
        batchName, discriminator, AlertErrorDescription.EXTRACTION)
        .stream()
        .map(RegisteredAlert::getAlertName)
        .collect(toList());
  }

  private Map<String, ParsedAlertMessage> getExtractedAlerts(
      AlertMessageStored message) {
    AlertMessagesDetailsResponse alertsDetailsResponse = getAlertDetails(message);
    return alertsDetailsResponse.getAlertsList()
        .stream()
        .collect(toMap(AlertMessageDetails::getMessageName, ad -> alertParser.parse(message, ad)));
  }

  private AlertMessagesDetailsResponse getAlertDetails(AlertMessageStored message) {
    return alertDetailsFacade.getAlertDetails(message);
  }
}
