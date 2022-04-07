package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.AlertIngested;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class IngestEventPublisherAdapter implements IngestEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void publish(Alert alert) {
    AlertIngested alertIngested = new AlertIngested(alert);
    applicationEventPublisher.publishEvent(alertIngested);
  }
}
