package com.silenteight.serp.governance.qa.check;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.qa.domain.DecisionService;

import java.time.OffsetDateTime;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MILLIS;

@RequiredArgsConstructor
@Slf4j
class ViewingDecisionHandlerJob implements Runnable {

  private static final Integer BATCH_SIZE = 50;
  @NonNull
  private final Integer maxMs;
  @NonNull
  private final DecisionService decisionService;
  @NonNull
  private final TimeSource timeSource;

  @Override
  public void run() {
    log.info("ViewingDecisionHandlerJob started at {}", now());
    decisionService.restartViewingDecisions(getDateTimeOlderThan(maxMs), BATCH_SIZE);
    log.info("ViewingDecisionHandlerJob finished at {}", now());
  }

  private OffsetDateTime getDateTimeOlderThan(Integer maxMs) {
    return timeSource.offsetDateTime().minus(maxMs, MILLIS);
  }
}
