package com.silenteight.scb.feeding.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.feeding.domain.model.AlertErrorDescription;
import com.silenteight.scb.feeding.domain.model.FeedUdsCommand;
import com.silenteight.scb.feeding.domain.model.UdsFedEvent;
import com.silenteight.scb.feeding.domain.model.UdsFedEvent.FedMatch;
import com.silenteight.scb.feeding.domain.model.UdsFedEvent.Status;
import com.silenteight.scb.feeding.domain.port.outgoing.FeedingEventPublisher;
import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedingFacade {

  private final FeedingService feedingService;
  private final UniversalDatasourceApiClient universalDatasourceApiClient;
  private final FeedingEventPublisher feedingEventPublisher;

  public void feedUds(FeedUdsCommand feedUdsCommand) {
    Try.run(() -> registerAgentInputsForMatches(feedUdsCommand))
        .onFailure(e -> {
          Alert alert = feedUdsCommand.alert();
          log.error(
              "Failed to create feature inputs for batch id: {} and alert id: {}.",
              alert.details().getBatchId(),
              alert.id().sourceId(),
              e);
          if (!alert.isLearnFlag()) {
            feedingEventPublisher.publish(
                createUdsFedEvent(
                    alert, Status.FAILURE, AlertErrorDescription.CREATE_FEATURE_INPUT));
          }
        })
        .onSuccess(e -> {
          Alert alert = feedUdsCommand.alert();
          log.info(
              "Feature inputs for batch id: {} and alert id: {} created successfully.",
              alert.details().getBatchId(),
              alert.id().sourceId());
          if (!alert.isLearnFlag()) {
            feedingEventPublisher.publish(
                createUdsFedEvent(alert, Status.SUCCESS, AlertErrorDescription.NONE));
          }
        });
  }

  private void registerAgentInputsForMatches(FeedUdsCommand feedUdsCommand) {
    Alert alert = feedUdsCommand.alert();
    alert.matches().forEach(match -> {
      var agentInputIns =
          feedingService.createAgentInputIns(alert, match);
      registerAgentInputs(agentInputIns);
    });
  }

  private void registerAgentInputs(List<AgentInputIn<Feature>> agentInputIns) {
    BatchCreateAgentInputsIn<Feature> batchCreateAgentInputsIn =
        BatchCreateAgentInputsIn.builder()
            .agentInputs(agentInputIns)
            .build();
    universalDatasourceApiClient.registerAgentInputs(batchCreateAgentInputsIn);
  }

  private UdsFedEvent createUdsFedEvent(
      Alert alert, Status status, AlertErrorDescription errorDescription) {
    return UdsFedEvent.builder()
        .batchId(alert.details().getBatchId())
        .alertId(alert.id().sourceId())
        .errorDescription(errorDescription)
        .feedingStatus(status)
        .fedMatches(createFedMatches(alert))
        .build();
  }

  private List<FedMatch> createFedMatches(Alert alert) {
    return alert.matches().stream()
        .map(match -> new FedMatch(match.id().sourceId()))
        .toList();
  }
}
