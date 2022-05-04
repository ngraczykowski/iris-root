package com.silenteight.warehouse.statistics.get;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDashboardStatisticsUseCaseTest {

  @InjectMocks
  private GetDashboardStatisticsUseCase cut;

  @Mock
  private DailyRecommendationStatisticsRepository repository;

  @Test
  void testCalculation() {
    //given
    when(repository.findByDayBetween(any(), any())).thenReturn(provideDailyStatistic());

    //when
    StatisticsResponse response =
        cut.getTotalCount(StatisticsRequest.of(LocalDate.now(), LocalDate.now()));

    //then
    assertAll(
        () -> assertEquals(645, response.getTotalAlerts()),
        () -> assertEquals(430, response.getFalsePositive()),
        () -> assertEquals(110, response.getPotentialTruePositive()),
        () -> assertEquals(105, response.getManualInvestigation()),
        () -> assertEquals(66.67, response.getFalsePositivePercent()),
        () -> assertEquals(16.28, response.getManualInvestigationPercent()),
        () -> assertEquals(17.05, response.getPotentialTruePositivePercent()),
        () -> assertEquals(10.31, response.getAvgEffectivenessPercent()),
        () -> assertEquals(22.07, response.getAvgEfficiencyPercent())
    );
  }

  public List<DailyRecommendationStatistics> provideDailyStatistic() {
    return List.of(
        new DailyRecommendationStatistics(
            1L, LocalDate.now(), 300, 300, 0, 0, 100, 30.0, 0.0),
        new DailyRecommendationStatistics(
            2L, LocalDate.now(), 300, 100, 100, 100, 300, 100.0, 22.0),
        new DailyRecommendationStatistics(
            3L, LocalDate.now(), 45, 30, 10, 5, 1, 10.0, 50.0)
    );
  }
}
