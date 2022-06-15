package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.process.port.SolvingAlertReceivedPort;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.AgentsMatchPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CategoryResolvePublisherPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputResolvePublisherPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.sep.base.aspects.metrics.Timed;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Slf4j
class SolvingAlertReceivedProcess implements SolvingAlertReceivedPort {

  private final AgentExchangeAlertSolvingMapper agentExchangeRequestMapper;
  private final AgentsMatchPort matchesPublisherPort;
  private final MatchFeatureDataAccess jdbcMatchFeaturesDataAccess;
  private final AlertSolvingRepository alertSolvingRepository;
  private final ReadyMatchFeatureVectorPort readyMatchFeatureVectorPublisherPort;
  private final CommentInputResolvePublisherPort commentInputResolveProcessPort;
  private final CategoryResolvePublisherPort categoryResolvePublisherPort;

  @Timed(
      percentiles = {0.5, 0.95, 0.99},
      histogram = true)
  public void handle(final AnalysisAlertsAdded message) {
    log.debug("Received alerts for solving = {}", message);
    aggregateAndPublish(message);
    if (log.isDebugEnabled()) {
      log.debug("AnalysisAlertsAdded mapped to AlertsSolving done");
    }
  }

  private void solveReadyMatches(AlertSolving alertSolving) {

    for (var matchId : alertSolving.getMatchIds()) {
      if (!alertSolving.isMatchReadyForSolving(matchId)) {
        continue;
      }
      var matchSolutionRequest =
          new MatchSolutionRequest(
              alertSolving.getAlertId(),
              matchId,
              alertSolving.getPolicy(),
              alertSolving.getMatchFeatureNames(matchId),
              alertSolving.getMatchFeatureVectors(matchId));
      readyMatchFeatureVectorPublisherPort.send(matchSolutionRequest);
    }
  }

  @Nonnull
  private void aggregateAndPublish(AnalysisAlertsAdded message) {
    message
        .getAnalysisAlertsList()
        .forEach(
            s -> {
              var analysisId = ResourceName.create(s).getLong("analysis");
              var alertId = ResourceName.create(s).getLong("alerts");
              log.info("Getting data from analysisId: {} alertId:{} ", analysisId, alertId);

              var alertAggregate =
                  jdbcMatchFeaturesDataAccess.findAnalysisAlertAndAggregate(analysisId, alertId);
              log.debug(
                  "AlertAggregate analysisId: {} alertId:{} featuresCount:{} matchesCount:{}",
                  analysisId,
                  alertId,
                  alertAggregate.agentFeatures().keySet().size(),
                  alertAggregate.matches().size());

              var alertSolving = this.alertSolvingRepository.save(new AlertSolving(alertAggregate));
              log.debug("Publish AlertSolving analysisId: {} alertId:{} ", analysisId, alertId);
              publishAlertSolving(alertSolving);
              log.debug("SolveMatchesIfReady analysisId: {} alertId:{} ", analysisId, alertId);
              solveReadyMatches(alertSolving);
            });
  }

  private void publishAlertSolving(AlertSolving alertSolving) {
    if (alertSolving.hasFeatures()) {
      this.agentExchangeRequestMapper.from(alertSolving).forEach(matchesPublisherPort::publish);
    }

    if (alertSolving.hasCategories()) {
      this.categoryResolvePublisherPort.resolve(alertSolving.getAlertId());
    }
    this.commentInputResolveProcessPort.resolve(alertSolving.getAlertName());
  }
}
