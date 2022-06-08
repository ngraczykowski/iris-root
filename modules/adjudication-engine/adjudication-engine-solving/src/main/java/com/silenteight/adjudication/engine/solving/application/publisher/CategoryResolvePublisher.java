package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.process.port.CategoryResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CategoryResolvePublisherPort;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CategoryResolvePublisher implements CategoryResolvePublisherPort {

  private final Queue<Long> alertCategoryValuesInputQueue;
  private final CategoryResolveProcessPort categoryResolveProcess;

  CategoryResolvePublisher(
      final ScheduledExecutorService scheduledExecutorService,
      final Queue<Long> alertCategoryValuesInputQueue,
      final CategoryResolveProcessPort categoryResolveProcess) {
    this.alertCategoryValuesInputQueue = alertCategoryValuesInputQueue;
    this.categoryResolveProcess = categoryResolveProcess;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 10, TimeUnit.MILLISECONDS);
  }

  public void resolve(final Long alertId) {
    log.debug("Resolve category value for alert: {}", alertId);
    this.alertCategoryValuesInputQueue.add(alertId);
  }

  private void process() {
    try {
      execute();
    } catch (Exception e) {
      log.error("Processing of category value failed: ", e);
    }
  }

  private void execute() {
    while (true) {
      final Long alertId = this.alertCategoryValuesInputQueue.poll();
      if (alertId == null) {
        break;
      }
      categoryResolveProcess.resolveCategoryValues(alertId);
    }
  }
}
