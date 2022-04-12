package com.silenteight.adjudication.engine.solving.application.publisher;

import org.springframework.stereotype.Service;

@Service
public class GovernancePublisher {

  public void send(final Object object) {
    // Send for solving alert solution to governance via queue (internal)
    // Create Governance internal queue listener and send to Gov
  }
}
