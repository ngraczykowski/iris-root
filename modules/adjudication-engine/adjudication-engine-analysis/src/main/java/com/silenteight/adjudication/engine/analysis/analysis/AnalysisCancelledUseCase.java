package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeDataAccess;
import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationDataAccess;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnalysisCancelledUseCase {

  private final PendingRecommendationDataAccess pendingRecommendationDataAccess;
  private final AgentExchangeDataAccess agentExchangeDataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" }, percentiles = {
      0.5, 0.95, 0.99 }, histogram = true)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void cancelAnalysis(Long analysisId) {

    log.info("Cancelling analysis has been started, analysis id={}", analysisId);
    List<Long> pendingRecommendationAlertIds =
        pendingRecommendationDataAccess.removePendingRecommendationByAnalysisIds(
            List.of(analysisId));
    log.debug("Pending recommendations have been deleted by analysis id={}", analysisId);
    agentExchangeDataAccess.removeAgentExchangeMatchFeatureByAlertIds(
        pendingRecommendationAlertIds);
    log.debug(
        "AgentExchangeMatchFeatures connected with pending recommendations alert ids ={}",
        pendingRecommendationAlertIds);
    log.info("Cancelling analysis has been finished, analysis id={}", analysisId);
  }
}
