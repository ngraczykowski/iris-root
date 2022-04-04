package com.silenteight.warehouse.statistics.model;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.warehouse.statistics.StatisticsConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@ActiveProfiles("jpa-test")
@ContextConfiguration(classes = { StatisticsConfiguration.class })
class DailyPolicyStatisticsRepositoryTest extends BaseDataJpaTest {

  @Autowired
  private DailyPolicyStatisticsRepository repository;

  @Test
  void getStatisticFromRange_oneStatisticInRange_statisticReturned() {
    // Given
    DailyPolicyStatistics dailyPolicyStatistic =
        createDailyPolicyStatistic(LocalDate.of(2022, 3, 13));
    repository.save(dailyPolicyStatistic);

    // When
    List<DailyPolicyStatistics>
        dailyPolicyStatisticList =
        repository.findByDayBetween(LocalDate.of(2022, 3, 13), LocalDate.of(2022, 3, 13));

    // Then
    assertThat(dailyPolicyStatisticList).containsExactlyInAnyOrder(dailyPolicyStatistic);
  }

  @Test
  void getStatisticFromRange_multipleStatistic_returnedOnlyStatisticInRange() {
    // Given
    DailyPolicyStatistics dailyPolicyStatistic =
        createDailyPolicyStatistic(LocalDate.of(2022, 3, 13));
    DailyPolicyStatistics dailyPolicyStatistic2 =
        createDailyPolicyStatistic(LocalDate.of(2022, 3, 15));
    repository.save(dailyPolicyStatistic);
    repository.save(dailyPolicyStatistic2);

    // When
    List<DailyPolicyStatistics>
        dailyPolicyStatisticList =
        repository.findByDayBetween(LocalDate.of(2022, 3, 13), LocalDate.of(2022, 3, 14));

    // Then
    assertThat(dailyPolicyStatisticList).containsExactlyInAnyOrder(dailyPolicyStatistic);
  }

  private static DailyPolicyStatistics createDailyPolicyStatistic(LocalDate localDate) {
    return DailyPolicyStatistics
        .builder()
        .day(localDate)
        .alertsCount(100)
        .falsePositivesCount(90)
        .potentialTruePositivesCount(8)
        .manualInvestigationsCount(2)
        .efficiencyPercent(20)
        .effectivenessPercent(80)
        .build();
  }
}