package com.silenteight.scb.ingest.adapter.incomming.common.alertmodel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.AlertModel;
import com.silenteight.sep.base.common.messaging.MessageSender;

import com.google.protobuf.TextFormat;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.ROUTE_ALERT_MODEL;

@RequiredArgsConstructor
@Slf4j
class AlertModelService {

  @NonNull
  private final MessageSender messageSender;
  @NonNull
  private final AlertModelFactory alertModelFactory;

  @EventListener(ApplicationStartedEvent.class)
  public void applicationStarted() {
    AlertModel alertModel = alertModelFactory.get();

    log.info("Sending alert model: message={}", TextFormat.shortDebugString(alertModel));
    messageSender.send(ROUTE_ALERT_MODEL, alertModel);
  }
}
