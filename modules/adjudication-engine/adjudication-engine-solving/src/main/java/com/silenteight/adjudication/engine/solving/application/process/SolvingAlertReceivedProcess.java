package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.publisher.AgentsMatchPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.ReadyMatchFeatureVectorPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.jdbc.MatchFeaturesFacade;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.sep.base.aspects.metrics.Timed;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Slf4j
public class SolvingAlertReceivedProcess {

  private final AgentExchangeAlertSolvingMapper agentExchangeRequestMapper;
  private final AgentsMatchPublisher matchesPublisher;
  private final MatchFeaturesFacade matchFeaturesFacade;
  private final AlertSolvingRepository alertSolvingRepository;
  private final ReadyMatchFeatureVectorPublisher readyMatchFeatureVectorPublisher;
  private final CommentInputResolveProcess commentInputResolveProcess;
  private final CategoryResolveProcess categoryResolveProcess;

  @Timed(
      percentiles = {0.5, 0.95, 0.99},
      histogram = true)
  public void handle(final AnalysisAlertsAdded message) {
    log.debug("Received alerts for solving = {}", message);

    var alerts = fetchAlertMatchesFeatures(message);

    var pendingAlerts =
        message.getAnalysisAlertsList().stream()
            .map(s -> ResourceName.create(s).getLong("alerts"))
            .map(alertId -> new AlertSolving(alerts.get(alertId)))
            .map(this.alertSolvingRepository::save)
            .collect(Collectors.toList());

    pendingAlerts.forEach(
        alertSolving -> {

          if (alertSolving.hasFeatures()) {
            this.agentExchangeRequestMapper.from(alertSolving).forEach(matchesPublisher::publish);
          }

          if (alertSolving.hasCategories()) {
            this.categoryResolveProcess.resolve(alertSolving.getAlertId());
          }

          this.commentInputResolveProcess.resolve(alertSolving.getAlertName());
        });

    solveReadyMatches(pendingAlerts);

    if (log.isDebugEnabled()) {
      log.debug("AnalysisAlertsAdded mapped to AlertsSolving done");
    }
  }

  private void solveReadyMatches(List<AlertSolving> pendingAlerts) {
    for (var alertSolving : pendingAlerts) {
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
        readyMatchFeatureVectorPublisher.send(matchSolutionRequest);
      }
    }
  }

  @Nonnull
  private Map<Long, AlertAggregate> fetchAlertMatchesFeatures(AnalysisAlertsAdded message) {
    Set<Long> analysis = new HashSet<>();
    Set<Long> alerts = new HashSet<>();

    message
        .getAnalysisAlertsList()
        .forEach(
            s -> {
              var analysisId = ResourceName.create(s).getLong("analysis");
              var alertId = ResourceName.create(s).getLong("alerts");
              analysis.add(analysisId);
              alerts.add(alertId);
            });
    log.info("Getting data from analysis {} for {} alerts", analysis, alerts.size());
    var alertAggregates = matchFeaturesFacade.findAnalysisFeatures(analysis, alerts);

    if (log.isTraceEnabled()) {
      log.trace("Found features: {}", alertAggregates);
    }
    return alertAggregates;
  }
}
