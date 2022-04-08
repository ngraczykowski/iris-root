package com.silenteight.scb.feeding.adapter.incomming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.feeding.domain.FeedingFacade;
import com.silenteight.scb.feeding.domain.model.FeedUdsCommand;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.AlertIngested;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class FeedingListener {

  private final FeedingFacade feedingFacade;

  @Async
  @EventListener
  public void subscribe(AlertIngested alertIngested) {
    Alert alert = alertIngested.alert();
    log.info("Alert received: {}", alert.logInfo());
    FeedUdsCommand feedUdsCommand = new FeedUdsCommand(alert);
    feedingFacade.feedUds(feedUdsCommand);
  }
}
