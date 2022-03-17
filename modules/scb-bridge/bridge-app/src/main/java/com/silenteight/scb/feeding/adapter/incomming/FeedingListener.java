package com.silenteight.scb.feeding.adapter.incomming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.feeding.domain.FeedingFacade;
import com.silenteight.scb.feeding.domain.model.FeedUdsCommand;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.AlertAndMatchesIngested;

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
  public void subscribe(AlertAndMatchesIngested alertAndMatchesIngested) {
    Alert alert = alertAndMatchesIngested.alert();
    log.info(
        "Alert and matches received for batch id: {} and alert id: {}.",
        alert.details().getBatchId(),
        alert.id().sourceId());
    FeedUdsCommand feedUdsCommand = new FeedUdsCommand(alert);
    feedingFacade.feedUds(feedUdsCommand);
  }
}
