package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class AdjudicationFacade {

  private final AlertService alertService;
  private final AnalysisFacade analysisFacade;
  private final DatasetServiceClient datasetServiceClient;

  public long registerAlertWithMatchesAndAnalysis(
      Map<String, AlertMatchIdComposite> alertMatchIds) {
    var alerts = alertService.registerAlertsWithMatches(alertMatchIds);
    return adjudicateAlerts(alerts);
  }

  public void registerAlertWithMatches(Map<String, AlertMatchIdComposite> alertMatchIds) {
    alertService.registerAlertsWithMatches(alertMatchIds);
  }

  private long adjudicateAlerts(Collection<String> alerts) {
    var datasetName = datasetServiceClient.createDataset(alerts);
    var analysis = analysisFacade.createAnalysisWithDataset(datasetName);
    return analysis.getId();
  }
}
