package com.silenteight.warehouse.statistics.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyPolicyStatisticsRepository
    extends Repository<DailyPolicyStatistics, Long> {

  void save(DailyPolicyStatistics entity);

  List<DailyPolicyStatistics> findByDayBetween(LocalDate startDate, LocalDate endDate);

  Optional<DailyPolicyStatistics> findFirstByOrderByDayDesc();

  @Query(value = "SELECT * FROM warehouse_daily_policy_statistics ORDER BY DAY DESC LIMIT 1 OFFSET"
      + " ?1", nativeQuery = true)
  Optional<DailyPolicyStatistics> findFirstByOrderByDayWithOffset(Number offset);
}
