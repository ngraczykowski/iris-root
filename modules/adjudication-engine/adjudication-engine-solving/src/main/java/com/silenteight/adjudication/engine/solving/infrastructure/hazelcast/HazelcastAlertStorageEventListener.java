/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.hazelcast;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryExpiredListener;
import com.hazelcast.map.listener.EntryRemovedListener;

@Slf4j
class HazelcastAlertStorageEventListener
    implements EntryEvictedListener<Long, AlertSolving>,
    EntryRemovedListener<Long, AlertSolving>,
    EntryExpiredListener<Long, AlertSolving> {


  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void entryEvicted(EntryEvent<Long, AlertSolving> event) {
    log.info("Entry eviction event:{} key:{}", event.getEventType(), event.getKey());
    // TODO implement logic what to do after eviction ?
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void entryRemoved(EntryEvent<Long, AlertSolving> event) {
    log.info("Entry removed event:{} key:{}", event.getEventType(), event.getKey());
    // TODO implement logic what to do after eviction ?
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void entryExpired(EntryEvent<Long, AlertSolving> event) {
    log.info("Entry expired event:{} key:{}", event.getEventType(), event.getKey());
    // TODO implement logic what to do after eviction ?
  }
}
