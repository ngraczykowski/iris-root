package com.silenteight.scb.feeding.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.feeding.domain.agent.input.AgentInputFactory;
import com.silenteight.scb.feeding.domain.model.AlertErrorDescription;
import com.silenteight.scb.feeding.domain.model.UdsFedEvent;
import com.silenteight.scb.feeding.domain.model.UdsFedEvent.FedMatch;
import com.silenteight.scb.feeding.domain.model.UdsFedEvent.Status;
import com.silenteight.scb.feeding.domain.port.outgoing.FeedingEventPublisher;
import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoryValuesIn;
import com.silenteight.universaldatasource.api.library.category.v2.CreateCategoryValuesIn;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedingFacade {

  private final AgentInputFactory agentInputFactory;
  private final UniversalDatasourceApiClient universalDatasourceApiClient;
  private final FeedingEventPublisher feedingEventPublisher;

  public boolean feedUds(Alert alert) {
    log.info("Feeding {} to Uds", alert.logInfo());
    var sw = StopWatch.createStarted();
    try {
      registerCategoriesValuesForMatches(alert);
      log.info(
          "Categories values have been registered for {}, executed in: {}", alert.logInfo(), sw);
    } catch (Exception e) {
      log.error("Failed to register categories values for {} after {}", alert.logInfo(), sw, e);
    }

    sw = StopWatch.createStarted();
    try {
      registerAgentInputsForMatches(alert);
      log.info("Feature inputs for {} created successfully, executed in: {}", alert.logInfo(), sw);
      alertFeedingSucceed(alert);
    } catch (Exception e) {
      log.error("Failed to create feature inputs for {} after: {}", alert.logInfo(), sw, e);
      alertFeedingFailed(alert);
      return false;
    }
    return true;
  }

  private void alertFeedingSucceed(Alert alert) {
    if (!alert.isLearnFlag()) {
      feedingEventPublisher.publish(
          createUdsFedEvent(alert, Status.SUCCESS, AlertErrorDescription.NONE));
    }
  }

  private void alertFeedingFailed(Alert alert) {
    if (!alert.isLearnFlag()) {
      feedingEventPublisher.publish(
          createUdsFedEvent(alert, Status.FAILURE, AlertErrorDescription.CREATE_FEATURE_INPUT));
    }
  }

  private void registerCategoriesValuesForMatches(Alert alert) {
    alert.matches()
        .forEach(match -> registerCategoriesValues(
            agentInputFactory.createCategoryValuesIns(alert, match)));
  }

  private void registerCategoriesValues(List<CreateCategoryValuesIn> createCategoryValuesIns) {
    universalDatasourceApiClient.registerCategoryValues(BatchCreateCategoryValuesIn.builder()
        .requests(createCategoryValuesIns)
        .build());
  }

  private void registerAgentInputsForMatches(Alert alert) {
    alert.matches()
        .forEach(match -> registerAgentInputs(agentInputFactory.createAgentInputIns(alert, match)));
  }

  private void registerAgentInputs(List<AgentInputIn<Feature>> agentInputIns) {
    universalDatasourceApiClient.registerAgentInputs(BatchCreateAgentInputsIn.builder()
        .agentInputs(agentInputIns)
        .build());
  }

  private UdsFedEvent createUdsFedEvent(
      Alert alert, Status status, AlertErrorDescription errorDescription) {
    return UdsFedEvent.builder()
        .internalBatchId(alert.details().getInternalBatchId())
        .alertName(alert.details().getAlertName())
        .errorDescription(errorDescription)
        .feedingStatus(status)
        .fedMatches(createFedMatches(alert))
        .build();
  }

  private List<FedMatch> createFedMatches(Alert alert) {
    return alert.matches().stream()
        .map(match -> new FedMatch(match.details().getMatchName()))
        .toList();
  }
}
