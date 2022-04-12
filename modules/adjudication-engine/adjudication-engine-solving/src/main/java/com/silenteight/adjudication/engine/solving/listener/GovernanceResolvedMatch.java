package com.silenteight.adjudication.engine.solving.listener;

import com.silenteight.sep.base.aspects.metrics.Timed;

class GovernanceResolvedMatch {

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceiveMessage(Object o){
    // Update matches received solution.
    // Verify state of all matches solution for alert - if completed
    // Send for solving alert solution to governance via queue (internal)
    // Create Governance internal queue listener and send to Gov
  }
}
