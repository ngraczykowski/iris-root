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

  public void etlAndFeedUds(ExtractedAlert extractedAlert) {
    if (extractedAlert.getStatus() == AlertStatus.FAILURE) {
      feedingEventPublisher.publish(
          createUdsFedEvent(extractedAlert, Status.FAILURE, extractedAlert.getErrorDescription()));
      return;
    }

    Try.run(() -> feedingService.createFeatureInputs(createFeatureInputsCommand(extractedAlert)))
        .onFailure(e -> {
          log.error(
              "Failed to create feature inputs for batch id: {} and alert id: {}.",
              extractedAlert.getBatchId(), extractedAlert.getAlertId(), e);
          feedingEventPublisher.publish(
              createUdsFedEvent(
                  extractedAlert, Status.FAILURE, AlertErrorDescription.CREATE_FEATURE_INPUT));
        })
        .onSuccess(e -> {
          log.info(
              "Feature inputs for batch id: {} and alert id: {} created successfully.",
              extractedAlert.getBatchId(), extractedAlert.getAlertId());
          feedingEventPublisher.publish(
              createUdsFedEvent(extractedAlert, Status.SUCCESS, AlertErrorDescription.NONE));
        });
  }

  private static FeatureInputsCommand createFeatureInputsCommand(ExtractedAlert extractedAlert) {
    return FeatureInputsCommand.builder()
        .batchId(extractedAlert.getBatchId())
        .extractedAlert(extractedAlert)
        .build();
  }

  private static UdsFedEvent createUdsFedEvent(
      ExtractedAlert extractedAlert, Status status, AlertErrorDescription errorDescription) {
    return UdsFedEvent.builder()
        .batchId(extractedAlert.getBatchId())
        .alertId(extractedAlert.getAlertId())
        .errorDescription(errorDescription)
        .feedingStatus(status)
        .fedMatches(createFedMatches(extractedAlert))
        .build();
  }

  private static List<FedMatch> createFedMatches(ExtractedAlert extractedAlert) {
    return extractedAlert.getMatches().stream()
        .map(match -> new FedMatch(match.getMatchId()))
        .collect(toList());
  }
}
