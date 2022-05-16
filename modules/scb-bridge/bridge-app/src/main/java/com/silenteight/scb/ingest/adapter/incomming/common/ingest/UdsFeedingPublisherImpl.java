package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.domain.model.BatchSource;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logAlert;

@Slf4j
@RequiredArgsConstructor
@Service
@Profile("!dev")
public class UdsFeedingPublisherImpl implements UdsFeedingPublisher {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();

  private static final int ALERT_LEARNING_FLAGS =
      Flag.LEARN.getValue() | Flag.PROCESS.getValue();

  private final IngestEventPublisher ingestEventPublisher;

  public void publishToUds(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext batchContext) {

    log.info("Publishing {} alerts for {} for internalBatchId: {}",
        alerts.size(), batchContext, internalBatchId);

    var flags = flags(batchContext);

    alerts.forEach(alert -> publish(alert, flags));
  }

  private static int flags(RegistrationBatchContext batchContext) {
    return batchContext.batchSource() == BatchSource.LEARNING ?
           ALERT_LEARNING_FLAGS :
           ALERT_RECOMMENDATION_FLAGS;
  }

  @LogContext
  private void publish(Alert alert, int flags) {
    logAlert(alert.id().sourceId(), alert.id().discriminator());

    var ingestedAlert = updateIngestInfoForAlert(alert, flags);

    ingestEventPublisher.publish(ingestedAlert);
  }

  @NotNull
  private static Alert updateIngestInfoForAlert(Alert alert, int flags) {
    var ingestedAt = Instant.now();

    if (log.isTraceEnabled()) {
      log.trace("Updating {} with: ingestedAt: {}, flags: {}", alert.logInfo(), ingestedAt, flags);
    }

    return alert
        .toBuilder()
        .flags(flags)
        .ingestedAt(ingestedAt)
        .build();
  }
}
