package com.silenteight.scb.feeding.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.feeding.domain.model.FeedUdsCommand;
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

  public void feedUds(FeedUdsCommand feedUdsCommand) {
    Try.run(() -> registerAgentInputsForMatches(feedUdsCommand))
        .onFailure(e -> {
          log.error(
              "Failed to create feature inputs for batch id: {} and alert id: {}.",
              feedUdsCommand.alert().details().batchId(),
              feedUdsCommand.alert().id().sourceId(),
              e);
          //publish failure event
        })
        .onSuccess(e -> {
          log.info(
              "Feature inputs for batch id: {} and alert id: {} created successfully.",
              feedUdsCommand.alert().details().batchId(),
              feedUdsCommand.alert().id().sourceId());
          //publish success event
        });
  }

  private void registerAgentInputsForMatches(FeedUdsCommand feedUdsCommand) {
    Alert alert = feedUdsCommand.alert();
    feedUdsCommand.matches().forEach(match -> {
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
}
