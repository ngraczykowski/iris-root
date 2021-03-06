package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand;
import com.silenteight.fab.dataprep.domain.model.*;
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.FedMatch;
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.Status;
import com.silenteight.fab.dataprep.domain.outgoing.FeedingEventPublisher;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.fab.dataprep.domain.model.AlertErrorDescription.*;
import static com.silenteight.fab.dataprep.domain.model.UdsFedEvent.Status.FAILURE;
import static com.silenteight.fab.dataprep.domain.model.UdsFedEvent.Status.SUCCESS;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedingFacade {

  private static final String SUCCESS_MESSAGE =
      "Feature inputs for batch: {} and alert: {} "
          + "(Origin alert messageName: {}) created successfully.";
  private final FeedingService feedingService;
  private final FeedingEventPublisher feedingEventPublisher;

  public void etlAndFeedUds(RegisteredAlert registeredAlert) {
    if (registeredAlert.getStatus() == AlertStatus.FAILURE) {
      feedingEventPublisher.publish(createUdsFedEvent(registeredAlert, FAILURE,
          registeredAlert.getErrorDescription()));
      return;
    }

    Try.run(() -> feedingService.createFeatureInputs(createFeatureInputsCommand(registeredAlert)))
        .onSuccess(e -> {
          log.info(
              SUCCESS_MESSAGE, registeredAlert.getBatchName(), registeredAlert.getAlertName(),
              registeredAlert.getMessageName());
          feedingEventPublisher.publish(createUdsFedEvent(registeredAlert, SUCCESS, NONE));
        })
        .get();
  }

  public void notifyAboutError(
      String batchName,
      String alertName,
      AlertErrorDescription errorDescription) {
    feedingEventPublisher.publish(
        createUdsFedEventWithoutMatches(batchName, alertName, errorDescription));
  }

  private static FeatureInputsCommand createFeatureInputsCommand(RegisteredAlert registeredAlert) {
    return FeatureInputsCommand.builder()
        .registeredAlert(registeredAlert)
        .build();
  }

  private static UdsFedEvent createUdsFedEvent(
      RegisteredAlert registeredAlert, Status status, AlertErrorDescription errorDescription) {
    return UdsFedEvent.builder()
        .batchName(registeredAlert.getBatchName())
        .alertName(registeredAlert.getAlertName())
        .errorDescription(errorDescription)
        .feedingStatus(status)
        .fedMatches(createFedMatches(registeredAlert))
        .build();
  }

  private static List<FedMatch> createFedMatches(RegisteredAlert registeredAlert) {
    return registeredAlert.getMatches().stream()
        .map(match -> new FedMatch(match.getMatchName()))
        .collect(toList());
  }

  private static UdsFedEvent createUdsFedEventWithoutMatches(
      String batchName, String alertName, AlertErrorDescription errorDescription) {
    return UdsFedEvent.builder()
        .batchName(batchName)
        .alertName(alertName)
        .errorDescription(errorDescription)
        .feedingStatus(Status.FAILURE)
        .build();
  }
}
