package com.silenteight.adjudication.engine.solving.infrastructure.publisher;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.TracePublisher;
import com.silenteight.adjudication.engine.solving.domain.TraceEvent;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("!trace-solving")
class TracePublisherDummyImpl implements TracePublisher {

  @Override
  public void publish(TraceEvent event) {
    log.debug("Model {}", event);
  }
}
