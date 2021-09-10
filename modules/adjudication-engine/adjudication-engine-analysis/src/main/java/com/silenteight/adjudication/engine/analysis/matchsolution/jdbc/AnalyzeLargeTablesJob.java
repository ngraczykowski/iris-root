package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AnalyzeLargeTablesJob {

  private final AnalyzeLargeTablesQuery query;

  @Timed(value = "ae.analysis.jobs", extraTags = { "package", "matchsolution" })
  @SchedulerLock(lockAtLeastFor = "PT1M", lockAtMostFor = "PT3M", name = "AnalyzeLargeTablesJob")
  @Scheduled(initialDelayString = "60000", fixedDelayString =
      "${ae.analysis.match-solution.analyze-large-tables-job.delay:60000}")
  void analyzeLargeTables() {
    log.info("Performing ANALYZE of large tables...");
    query.execute();
  }
}
