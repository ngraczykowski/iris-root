/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

import static com.google.common.base.Strings.nullToEmpty;

@Slf4j
@RequiredArgsConstructor
class AlertIdPublisher implements Consumer<AlertIdCollection> {

  private final AlertInFlightService alertInFlightService;

  @Override
  public void accept(@Nonnull AlertIdCollection collection) {
    if (log.isDebugEnabled())
      log.debug("Received alert IDs: {}", collection);

    var alertIdContext = toScbAlertIdContext(collection.getContext());

    alertInFlightService.saveUniqueAlerts(collection.getAlertIds(), alertIdContext);
  }

  private static ScbAlertIdContext toScbAlertIdContext(AlertIdContext context) {
    return ScbAlertIdContext.newBuilder()
        .setAckRecords(context.isAckRecords())
        .setHitDetailsView(nullToEmpty(context.getHitDetailsView()))
        .setPriority(context.getPriority())
        .setSourceView(context.getRecordsView())
        .setWatchlistLevel(context.isWatchlistLevel())
        .build();
  }
}
