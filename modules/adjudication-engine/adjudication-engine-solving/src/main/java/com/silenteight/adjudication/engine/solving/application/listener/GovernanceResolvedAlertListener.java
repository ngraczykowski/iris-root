package com.silenteight.adjudication.engine.solving.application.listener;

import com.silenteight.adjudication.engine.solving.application.process.ResolvedAlertProcess;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

import org.springframework.stereotype.Service;

@Service
class GovernanceResolvedAlertListener {

  private final ResolvedAlertProcess resolvedAlertProcess;

  GovernanceResolvedAlertListener(ResolvedAlertProcess resolvedAlertProcess) {
    this.resolvedAlertProcess = resolvedAlertProcess;
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceiveMessage(long alertId) {
    this.resolvedAlertProcess.generateRecommendation(
        alertId, BatchSolveAlertsResponse.newBuilder().build());
  }

}
