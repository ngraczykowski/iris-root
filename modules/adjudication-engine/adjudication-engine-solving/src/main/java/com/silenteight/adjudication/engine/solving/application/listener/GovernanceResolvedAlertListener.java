package com.silenteight.adjudication.engine.solving.application.listener;

import com.silenteight.adjudication.engine.solving.application.process.Dexter;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
class GovernanceResolvedAlertListener {

  private final Dexter dexter;

  GovernanceResolvedAlertListener(Dexter dexter) {
    this.dexter = dexter;
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceiveMessage(Object o) {
    // Update alert received solution.
    // Generate Comment for alert
    // Verify if timout reached and update status ??
    // Send recommendation notification to topic (two listeners storing, sending cmapi)

    this.dexter.updateAlertSolutions(o);
  }

}
