package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;

import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

import static com.google.common.base.Strings.nullToEmpty;

@Slf4j
@Builder
class AlertIdPublisher implements Consumer<AlertIdCollection> {

  private final AlertInFlightService alertInFlightService;

  @Override
  public void accept(@Nonnull AlertIdCollection collection) {
    if (log.isDebugEnabled())
      log.debug("Received alert IDs: {}", collection);

    var internalBatchId = generateInternalBatchId();
    //TODO: publish rabbit event

    var alertIdContext = toScbAlertIdContext(collection.getContext());

    alertInFlightService.saveUniqueAlerts(
        collection.getAlertIds(), internalBatchId, alertIdContext);
    log.info(
        "Collection of {} alerts has been saved with internalBatchId: {}", collection.getSize(),
        internalBatchId);
  }

  private String generateInternalBatchId() {
    return UUID.randomUUID().toString();
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
