package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsEventPublisher;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.RecomCalledEvent;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
public class CbsRecommendationGateway extends CbsEventPublisher {

  private static final String RECOMMENDATION_FAILED = "FAILED";
  private final RecomFunctionExecutorService recomFunctionExecutorService;
  private final ScbRecommendationService recommendationService;

  public CbsRecommendationGateway(
      @NonNull RecomFunctionExecutorService recomFunctionExecutorService,
      ScbRecommendationService recommendationService) {
    this.recomFunctionExecutorService = recomFunctionExecutorService;
    this.recommendationService = recommendationService;
  }

  @Transactional(value = "externalTransactionManager", readOnly = true)
  void recommendAlerts(List<CbsAlertRecommendation> alertsToBeRecommended) {
    alertsToBeRecommended.forEach(this::recommendAlert);
  }

  @Timed(
      value = "serp.scb.bridge.oracle.procedure.recommend-alert.time",
      description = "Time taken to call recommend function")
  void recommendAlert(@NonNull CbsAlertRecommendation alertRecommendation) {
    try {
      String statusCode = recomFunctionExecutorService.execute(alertRecommendation);

      log.info(
          "CBS: Recommendation function executed: status={}, systemId={}, hitWatchlistId={},"
              + " batchId={}",
          statusCode,
          alertRecommendation.getAlertExternalId(),
          alertRecommendation.getHitWatchlistId(),
          alertRecommendation.getBatchId());

      notifyRecomCalled(statusCode);
      recommendationService.updateRecomStatus(
          alertRecommendation.getAlertExternalId(),
          alertRecommendation.getHitWatchlistId(),
          statusCode);
    } catch (Exception e) {
      log.error(
          "CBS: Cannot recommend given: systemId={}, batchId={}",
          alertRecommendation.getAlertExternalId(),
          alertRecommendation.getBatchId(),
          e);
      notifyCbsCallFailed("RECOM");
      recommendationService.updateRecomStatus(
          alertRecommendation.getAlertExternalId(),
          alertRecommendation.getHitWatchlistId(),
          RECOMMENDATION_FAILED);
    }
  }

  private void notifyRecomCalled(String statusCode) {
    publishEvent(() -> RecomCalledEvent
        .builder()
        .statusCode(statusCode)
        .build());
  }
}
