package com.silenteight.warehouse.statistics.model;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.warehouse.common.domain.DomainConfiguration;
import com.silenteight.warehouse.common.time.TimeConfiguration;
import com.silenteight.warehouse.indexer.query.single.SingleAlertQueryConfiguration;
import com.silenteight.warehouse.statistics.StatisticsConfiguration;
import com.silenteight.warehouse.statistics.get.DailyRecommendationStatistics;
import com.silenteight.warehouse.statistics.get.DailyRecommendationStatisticsRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@ActiveProfiles("jpa-test")
@ContextConfiguration(classes = {
    StatisticsConfiguration.class, DomainConfiguration.class, TimeConfiguration.class,
    SingleAlertQueryConfiguration.class })
class DailyRecommendationStatisticsRepositoryTest extends BaseDataJpaTest {

  @Autowired
  private DailyRecommendationStatisticsRepository repository;

  private static DailyRecommendationStatistics createDailyRecommendationStatistic(
      LocalDate localDate) {
    return DailyRecommendationStatistics
        .builder()
        .day(localDate)
        .alertsCount(100)
        .falsePositivesCount(90)
        .potentialTruePositivesCount(8)
        .manualInvestigationsCount(2)
        .analystDecisionCount(3)
        .efficiencyPercent(20.0)
        .effectivenessPercent(80.0)
        .build();
  }

  @Test
  void getStatisticFromRange_oneStatisticInRange_statisticReturned() {
    // Given
    DailyRecommendationStatistics dailyRecommendationStatistic =
        createDailyRecommendationStatistic(LocalDate.of(2022, 3, 13));
    repository.save(dailyRecommendationStatistic);

    // When
    List<DailyRecommendationStatistics>
        dailyRecommendationStatisticList =
        repository.findByDayBetweenOrderByDayDesc(
            LocalDate.of(2022, 3, 13), LocalDate.of(2022, 3, 13));

    // Then
    assertThat(dailyRecommendationStatisticList).containsExactlyInAnyOrder(
        dailyRecommendationStatistic);
  }

  @Test
  void getStatisticFromRange_multipleStatistic_returnedOnlyStatisticInRange() {
    // Given
    DailyRecommendationStatistics dailyRecommendationStatistic =
        createDailyRecommendationStatistic(LocalDate.of(2022, 3, 13));
    DailyRecommendationStatistics dailyRecommendationStatistic2 =
        createDailyRecommendationStatistic(LocalDate.of(2022, 3, 15));
    repository.save(dailyRecommendationStatistic);
    repository.save(dailyRecommendationStatistic2);

    // When
    List<DailyRecommendationStatistics>
        dailyRecommendationStatisticList =
        repository.findByDayBetweenOrderByDayDesc(
            LocalDate.of(2022, 3, 13), LocalDate.of(2022, 3, 14));

    // Then
    assertThat(dailyRecommendationStatisticList).containsExactlyInAnyOrder(
        dailyRecommendationStatistic);
  }

  @Test
  void getLastStatistic_multipleStatistic_returnedLastStatistic() {
    // Given
    DailyRecommendationStatistics dailyRecommendationStatistic =
        createDailyRecommendationStatistic(LocalDate.of(2022, 3, 13));
    DailyRecommendationStatistics dailyRecommendationStatistic2 =
        createDailyRecommendationStatistic(LocalDate.of(2022, 3, 15));
    repository.save(dailyRecommendationStatistic);
    repository.save(dailyRecommendationStatistic2);

    // When
    Optional<DailyRecommendationStatistics> dailyRecommendationStatisticOptional =
        repository.findFirstByOrderByDayDesc();

    // Then
    assertThat(dailyRecommendationStatisticOptional).contains(dailyRecommendationStatistic2);
  }

  @Test
  void getLastStatistic_noStatistics_returnedOptionalEmpty() {

    // When
    Optional<DailyRecommendationStatistics> dailyRecommendationStatisticOptional =
        repository.findFirstByOrderByDayDesc();

    // Then
    assertThat(dailyRecommendationStatisticOptional).isEmpty();

  }
}