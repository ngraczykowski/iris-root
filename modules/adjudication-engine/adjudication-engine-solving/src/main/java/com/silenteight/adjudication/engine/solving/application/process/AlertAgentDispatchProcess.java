package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.publisher.AgentsMatchPublisher;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;
import com.silenteight.adjudication.engine.solving.data.MatchFeaturesFacade;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.MatchFeature;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.sep.base.aspects.metrics.Timed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Slf4j
public class AlertAgentDispatchProcess {

  private final AgentExchangeAlertSolvingMapper agentExchnageRequestMapper;
  private final AgentsMatchPublisher matchesPublisher;
  private final MatchFeaturesFacade matchFeaturesFacade;
  private final AlertSolvingRepository alertSolvingRepository;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void handle(final AnalysisAlertsAdded message) {
    var alerts = fetchAlertMatchesFeatures(message);

    var alertMatchFeatures = alerts
        .stream()
        .map(MatchFeature::from)
        .collect(Collectors.groupingBy(MatchFeature::getAlertId));

    var pendingAlerts = message.getAnalysisAlertsList()
        .stream()
        .map(s -> ResourceName.create(s).getLong("alerts"))
        .map(alertId -> {
          // TODO: get specific strategy and policy for alert
          var firstAlert = alerts.stream().findFirst().get();
          return new AlertSolving(alertId, firstAlert.getPolicy(),
              firstAlert.getStrategy(), firstAlert.getAnalysisId()).addMatchesFeatures(
              alertMatchFeatures.get(alertId));
        })
        .map(this.alertSolvingRepository::save)
        .collect(Collectors.toList());

    pendingAlerts.forEach(alertSolving -> {
      this.agentExchnageRequestMapper.from(alertSolving)
          .forEach(matchesPublisher::publish);
    });
    if (log.isDebugEnabled()) {
      log.debug("AnalysisAlertsAdded mapped to AlertsSolving done");
    }
  }

  @Nonnull
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  private List<MatchFeatureDao> fetchAlertMatchesFeatures(AnalysisAlertsAdded message) {
    Set<Long> analysis = new HashSet<>();
    Set<Long> alerts = new HashSet<>();

    message.getAnalysisAlertsList().forEach(s -> {
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
