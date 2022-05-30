package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.publisher.AgentsMatchPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.ReadyMatchFeatureVectorPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;
import com.silenteight.adjudication.engine.solving.data.MatchFeaturesFacade;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;
import com.silenteight.adjudication.engine.solving.domain.MatchFeature;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.sep.base.aspects.metrics.Timed;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Slf4j
public class AlertAgentDispatchProcess {

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
    var alerts = fetchAlertMatchesFeatures(message);

    var alertMatchFeatures =
        alerts.stream()
            .map(MatchFeature::from)
            .collect(Collectors.groupingBy(MatchFeature::getAlertId));

    var alertMatchCategories =
        alerts.stream()
            .map(MatchCategory::from)
            .collect(Collectors.groupingBy(MatchCategory::getAlertId));

    var pendingAlerts =
        message.getAnalysisAlertsList().stream()
            .map(s -> ResourceName.create(s).getLong("alerts"))
            .map(
                alertId -> {
                  var firstAlert = alerts.stream().findFirst().get();
                  return new AlertSolving(
                          alertId,
                          firstAlert.getPolicy(),
                          firstAlert.getStrategy(),
                          firstAlert.getAnalysisId())
                      .addMatchesFeatures(alertMatchFeatures.get(alertId))
                      .addMatchesCategories(alertMatchCategories.get(alertId));
                })
            .map(this.alertSolvingRepository::save)
            .collect(Collectors.toList());

    pendingAlerts.forEach(
        alertSolving -> {
          // it sucks because is still blocking
          this.agentExchangeRequestMapper.from(alertSolving).forEach(matchesPublisher::publish);
          this.commentInputResolveProcess.resolve(alertSolving.getAlertName());
          this.categoryResolveProcess.resolve(alertSolving.getAlertId());
        });

    solveReadyMatches(pendingAlerts);

    if (log.isDebugEnabled()) {
      log.debug("AnalysisAlertsAdded mapped to AlertsSolving done");
    }
  }

  private void solveReadyMatches(List<AlertSolving> pendingAlerts) {
    pendingAlerts.forEach(
        alertSolving ->
            Arrays.stream(alertSolving.getMatchIds())
                .forEach(
                    matchId -> {
                      if (alertSolving.isMatchReadyForSolving(matchId)) {
                        var matchSolutionRequest =
                            new MatchSolutionRequest(
                                alertSolving.getAlertId(),
                                matchId,
                                alertSolving.getPolicy(),
                                alertSolving.getMatchFeatureNames(matchId),
                                alertSolving.getMatchFeatureVectors(matchId));
                        readyMatchFeatureVectorPublisher.send(matchSolutionRequest);
                      }
                    }));
  }

  @Nonnull
  private List<MatchFeatureDao> fetchAlertMatchesFeatures(AnalysisAlertsAdded message) {
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
    var features = matchFeaturesFacade.findAnalysisFeatures(analysis, alerts);

    if (log.isTraceEnabled()) {
      log.trace("Found features: {}", features);
    }
    return features;
  }
}
