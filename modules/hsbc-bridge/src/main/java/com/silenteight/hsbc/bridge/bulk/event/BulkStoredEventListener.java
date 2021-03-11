package com.silenteight.hsbc.bridge.bulk.event;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;

@Slf4j
public class BulkStoredEventListener {

  @EventListener
  public void onBulkStoredEvent(BulkStoredEvent bulkStoredEvent) {
    log.debug("Receive bulkStoredEvent, bulk_id: " + bulkStoredEvent.getMessage());
  }
}
