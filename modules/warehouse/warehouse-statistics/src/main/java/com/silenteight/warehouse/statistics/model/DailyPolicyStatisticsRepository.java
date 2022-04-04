package com.silenteight.warehouse.statistics.model;

import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public interface DailyPolicyStatisticsRepository
    extends Repository<DailyPolicyStatistics, Long> {

  void save(DailyPolicyStatistics entity);

  List<DailyPolicyStatistics> findByDayBetween(LocalDate startDate, LocalDate endDate);
}
