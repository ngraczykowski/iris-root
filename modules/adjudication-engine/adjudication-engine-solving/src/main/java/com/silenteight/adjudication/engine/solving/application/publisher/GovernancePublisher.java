package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GovernancePublisher {

  public void send(final Object object) {
    // Send for solving alert solution to governance via queue (internal)
    // Create Governance internal queue listener and send to Gov
    log.info("Sending object to Governance");
  }
}
