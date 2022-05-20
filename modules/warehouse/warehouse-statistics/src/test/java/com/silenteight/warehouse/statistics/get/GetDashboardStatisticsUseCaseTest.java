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
  void testCalculation_allDataExists() {
    //given
    when(repository.findByDayBetweenOrderByDayDesc(any(), any())).thenReturn(
        provideDailyStatistic());

    //when
    StatisticsResponse response =
        cut.getTotalCount(StatisticsRequest.of(LocalDate.now(), LocalDate.now()));

    //then
    assertAll(
        () -> assertEquals(645, response.getTotalAlerts()),
        () -> assertEquals(430, response.getFalsePositive()),
        () -> assertEquals(110, response.getPotentialTruePositive()),
        () -> assertEquals(105, response.getManualInvestigation()),
        () -> assertEquals(66.66666666666666, response.getFalsePositivePercent()),
        () -> assertEquals(16.27906976744186, response.getManualInvestigationPercent()),
        () -> assertEquals(17.05426356589147, response.getPotentialTruePositivePercent()),
        () -> assertEquals(16.58354114713217, response.getAvgEffectivenessPercent()),
        () -> assertEquals(61.16279069767442, response.getAvgEfficiencyPercent())
    );
  }

  @Test
  void testCalculation_dataContainsNaN() {
    //given
    when(repository.findByDayBetweenOrderByDayDesc(any(), any())).thenReturn(
        provideDailyStatisticWithNaN());

    //when
    StatisticsResponse response =
        cut.getTotalCount(StatisticsRequest.of(LocalDate.now(), LocalDate.now()));

    //then
    assertAll(
        () -> assertEquals(645, response.getTotalAlerts()),
        () -> assertEquals(430, response.getFalsePositive()),
        () -> assertEquals(110, response.getPotentialTruePositive()),
        () -> assertEquals(105, response.getManualInvestigation()),
        () -> assertEquals(66.66666666666666, response.getFalsePositivePercent()),
        () -> assertEquals(16.27906976744186, response.getManualInvestigationPercent()),
        () -> assertEquals(17.05426356589147, response.getPotentialTruePositivePercent()),
        () -> assertEquals(22.093023255813954, response.getAvgEffectivenessPercent()),
        () -> assertEquals(61.16279069767442, response.getAvgEfficiencyPercent())
    );
  }

  @Test
  void testCalculation_emptyTable() {
    //given
    when(repository.findByDayBetweenOrderByDayDesc(any(), any())).thenReturn(List.of());

    //when
    StatisticsResponse response =
        cut.getTotalCount(StatisticsRequest.of(LocalDate.now(), LocalDate.now()));

    //then
    assertAll(
        () -> assertEquals(0, response.getTotalAlerts()),
        () -> assertEquals(0, response.getFalsePositive()),
        () -> assertEquals(0, response.getPotentialTruePositive()),
        () -> assertEquals(0, response.getManualInvestigation()),
        () -> assertEquals(Double.NaN, response.getFalsePositivePercent()),
        () -> assertEquals(Double.NaN, response.getManualInvestigationPercent()),
        () -> assertEquals(Double.NaN, response.getPotentialTruePositivePercent()),
        () -> assertEquals(Double.NaN, response.getAvgEffectivenessPercent()),
        () -> assertEquals(Double.NaN, response.getAvgEfficiencyPercent())
    );
  }

  public List<DailyRecommendationStatistics> provideDailyStatisticWithNaN() {
    return List.of(
        new DailyRecommendationStatistics(
            1L, LocalDate.now(), 300, 300, 0, 0, 0, 30.0, Double.NaN),
        new DailyRecommendationStatistics(
            2L, LocalDate.now(), 300, 100, 100, 100, 300, 100.0, 22.0),
        new DailyRecommendationStatistics(
            3L, LocalDate.now(), 45, 30, 10, 5, 1, 10.0, 50.0)
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
