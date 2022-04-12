package com.silenteight.adjudication.engine.solving.listener;

import com.silenteight.sep.base.aspects.metrics.Timed;

class GovernanceResolvedAlert {

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceiveMessage(Object o) {
    // Update alert received solution.
    // Generate Comment for alert
    // Verify if timout reached and update status ??
    // Send recommendation notification to topic (two listeners storing, sending cmapi)
  }
}
