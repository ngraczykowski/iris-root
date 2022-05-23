package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.feeding.domain.FeedingFacade;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.domain.model.BatchSource;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import javax.annotation.PostConstruct;

import static com.silenteight.scb.ingest.domain.model.BatchSource.CBS;
import static com.silenteight.scb.ingest.domain.model.BatchSource.GNS_RT;
import static com.silenteight.scb.ingest.domain.model.BatchSource.LEARNING;

@Slf4j
@Service
@Profile("!dev")
public class UdsFeedingPublisherImpl implements UdsFeedingPublisher {

  private static final int ALERT_RECOMMENDATION_FLAGS =
      Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();

  private static final int ALERT_LEARNING_FLAGS =
      Flag.LEARN.getValue() | Flag.PROCESS.getValue();

  private int timeoutMs;

  private final int poolCbsThreads;

  private final int learningThreads;

  private final FeedingFacade feedingFacade;

  private Map<BatchSource, ExecutorService> pools;

  UdsFeedingPublisherImpl(
      @Value("${silenteight.scb-bridge.usd-feeder.timeoutMs:10000}") int timeoutMs,
      @Value("${silenteight.scb-bridge.usd-feeder.pool.cbs.threads:10}") int poolCbsThreads,
      @Value("${silenteight.scb-bridge.usd-feeder.pool.learning.threads:10}") int learningThreads,
      FeedingFacade feedingFacade) {
    this.timeoutMs = timeoutMs;
    this.poolCbsThreads = poolCbsThreads;
    this.learningThreads = learningThreads;
    this.feedingFacade = feedingFacade;
  }

  @PostConstruct
  public void init() {
    log.info("Creating thread pools for feeding Uds");
    pools = Map.of(
        CBS, Executors.newFixedThreadPool(poolCbsThreads, threadFactory("uds-feeder-cbs-%d")),
        GNS_RT, Executors.newCachedThreadPool(threadFactory("uds-feeder-gns-rt-%d")),
        LEARNING,
        Executors.newFixedThreadPool(learningThreads, threadFactory("uds-feeder-learning-%d")));
  }

  public IngestedAlertsStatus publishToUds(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext batchContext) {

    log.info("Feeding {} alerts for {} for internalBatchId: {} to Uds", alerts.size(), batchContext,
        internalBatchId);

    var updated = updateIngestInfoForAlert(alerts, flags(batchContext));

    return feedUds(updated, requirePool(batchContext));
  }

  private static int flags(RegistrationBatchContext batchContext) {
    return batchContext.batchSource() == BatchSource.LEARNING ?
           ALERT_LEARNING_FLAGS :
           ALERT_RECOMMENDATION_FLAGS;
  }

  private static List<Alert> updateIngestInfoForAlert(List<Alert> alerts, int flags) {
    return alerts.stream()
        .map(alert -> updateIngestInfoForAlert(alert, flags))
        .toList();
  }

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

  @NonNull
  private ExecutorService requirePool(RegistrationBatchContext batchContext) {
    return pools.get(batchContext.batchSource());
  }

  private IngestedAlertsStatus feedUds(List<Alert> alerts, ExecutorService pool) {
    var sw = StopWatch.createStarted();
    var i = -1;
    var success = new ArrayList<Alert>();
    var failed = new ArrayList<Alert>();

    log.debug("Feeding of {} alerts to Uds started...", alerts.size());
    for (var future : invokeAll(pool, tasks(alerts))) {
      i++;
      var alert = alerts.get(i);
      if (future.isCancelled()) {
        log.error("Didn't manage to feed: " + alert.logInfo() + " to Uds in specified time");
        failed.add(alert);
        continue;
      }
      try {
        boolean result = future.get();
        if (result) {
          success.add(alert);
        } else {
          failed.add(alert);
        }
      } catch (Exception e) {
        log.error("Failed to feed: " + alert.logInfo() + " to Uds", e);
        failed.add(alert);
      }
    }
    log.info(
        "Feeding of {} alerts to Uds finished in {}. Success: {}, failed: {}",
        alerts.size(),
        sw,
        success.size(),
        failed.size());
    return new IngestedAlertsStatus(success, failed);
  }

  private List<Future<Boolean>> invokeAll(ExecutorService pool, List<Callable<Boolean>> tasks) {
    try {
      return pool.invokeAll(tasks, timeoutMs, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Interrupted while feeding Uds with alerts", e);
    }
  }

  private List<Callable<Boolean>> tasks(List<Alert> alerts) {
    return alerts.stream()
        .map(alert -> (Callable<Boolean>) () -> feedingFacade.feedUds(alert))
        .toList();
  }

  private static ThreadFactory threadFactory(String nameFormat) {
    return new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
  }
}
