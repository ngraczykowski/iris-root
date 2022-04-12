package com.silenteight.adjudication.engine.solving.application.listener;

import com.silenteight.adjudication.engine.solving.application.process.Gamma;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
class ReceiveAgentResponseListener {

  private final Gamma gamma;

  ReceiveAgentResponseListener(final Gamma gamma) {
    this.gamma = gamma;
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceive(Object o) {
    // Update matches received features.
    // Verify state of matches - if completed
    // Send to governance via queue (internal)
    // Create Governance internal queue listener and send to Gov

    gamma.updateMatches(o);
  }


}
