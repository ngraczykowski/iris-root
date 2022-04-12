package com.silenteight.adjudication.engine.solving.listener;

import com.silenteight.sep.base.aspects.metrics.Timed;

class ReceiveAgentResponse {

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceive(Object o) {
    // Update matches received features.
    // Verify state of matches - if completed
    // Send to governance via queue (internal)
    // Create Governance internal queue listener and send to Gov
  }
}
