package com.silenteight.warehouse.statistics.get;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRecommendationStatisticsRepository
    extends Repository<DailyRecommendationStatistics, Long> {

  void save(DailyRecommendationStatistics entity);

  List<DailyRecommendationStatistics> findByDayBetweenOrderByDayDesc(
      LocalDate startDate, LocalDate endDate);

  Optional<DailyRecommendationStatistics> findFirstByOrderByDayDesc();

  @Query(value =
      "SELECT * FROM warehouse_daily_recommendation_statistics ORDER BY DAY DESC LIMIT 1 OFFSET"
          + " ?1", nativeQuery = true)
  Optional<DailyRecommendationStatistics> findFirstByOrderByDayWithOffset(Number offset);

}
