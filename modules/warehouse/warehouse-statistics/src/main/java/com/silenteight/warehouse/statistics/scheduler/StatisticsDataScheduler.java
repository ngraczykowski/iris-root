package com.silenteight.warehouse.statistics.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.statistics.Annotations.Daily;
import com.silenteight.warehouse.statistics.StatisticsCollector;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticsDataScheduler {

  @Daily
  private final List<StatisticsCollector> statisticCollector;

  @Scheduled(cron = "${warehouse.statistics.daily.scheduled-cron}")
  public void dailyStatisticsScheduler() {
    log.info(
        "Scheduling to calculate daily statistic {}, for collectors {}", Instant.now(),
        statisticCollector);
    statisticCollector.forEach(StatisticsCollector::generateStatisticsData);
  }
}
