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

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedingFacade {

  private final FeedingService feedingService;
  private final FeedingEventPublisher feedingEventPublisher;

  public void etlAndFeedUds(RegisteredAlert registeredAlert) {
    if (registeredAlert.getStatus() == AlertStatus.FAILURE) {
      feedingEventPublisher.publish(createUdsFedEvent(registeredAlert, Status.FAILURE,
          registeredAlert.getErrorDescription()));
      return;
    }

    Try.run(() -> feedingService.createFeatureInputs(createFeatureInputsCommand(registeredAlert)))
        .onFailure(e -> {
          log.error(
              "Failed to create feature inputs for batch id: {} and alert id: {}.",
              registeredAlert.getBatchId(), registeredAlert.getAlertId(), e);
          feedingEventPublisher.publish(
              createUdsFedEvent(
                  registeredAlert, Status.FAILURE, AlertErrorDescription.CREATE_FEATURE_INPUT));
        })
        .onSuccess(e -> {
          log.info(
              "Feature inputs for batch id: {} and alert id: {} created successfully.",
              registeredAlert.getBatchId(), registeredAlert.getAlertId());
          feedingEventPublisher.publish(
              createUdsFedEvent(registeredAlert, Status.SUCCESS, AlertErrorDescription.NONE));
        });
  }

  private static FeatureInputsCommand createFeatureInputsCommand(RegisteredAlert registeredAlert) {
    return FeatureInputsCommand.builder()
        .batchId(registeredAlert.getBatchId())
        .registeredAlert(registeredAlert)
        .build();
  }

  private static UdsFedEvent createUdsFedEvent(
      RegisteredAlert registeredAlert, Status status, AlertErrorDescription errorDescription) {
    return UdsFedEvent.builder()
        .batchId(registeredAlert.getBatchId())
        .alertId(registeredAlert.getAlertId())
        .errorDescription(errorDescription)
        .feedingStatus(status)
        .fedMatches(createFedMatches(registeredAlert))
        .build();
  }

  private static List<FedMatch> createFedMatches(RegisteredAlert registeredAlert) {
    return registeredAlert.getMatches().stream()
        .map(match -> new FedMatch(match.getMatchId()))
        .collect(toList());
  }
}
