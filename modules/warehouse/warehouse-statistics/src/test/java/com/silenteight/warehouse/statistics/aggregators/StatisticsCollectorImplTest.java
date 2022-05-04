package com.silenteight.warehouse.statistics.aggregators;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.domain.RecommendationMapper;
import com.silenteight.warehouse.common.properties.RecommendationProperties;
import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.statistics.AggregationPeriod;
import com.silenteight.warehouse.statistics.StatisticsCollectorImpl;
import com.silenteight.warehouse.statistics.computers.AlertRecommendationComputer;
import com.silenteight.warehouse.statistics.computers.AlertsRecommendationStatistics;
import com.silenteight.warehouse.statistics.extractors.AlertDataExtractor;
import com.silenteight.warehouse.statistics.get.DailyRecommendationStatistics;
import com.silenteight.warehouse.statistics.get.DailyRecommendationStatisticsRepository;
import com.silenteight.warehouse.statistics.persistance.DailyRecommendationPersistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsCollectorImplTest {

  private static final LocalDate NOW = LocalDate.ofInstant(Instant.now(), ZoneOffset.UTC);

  private static final LocalDate AGGREGATION_DATE = NOW.minusDays(1);

  private static final LocalDate RECALCULATION_DATE = AGGREGATION_DATE.minusDays(1);

  // 2 days before now
  private static final LocalDate OLDEST_DAILY_STATISTICS = AGGREGATION_DATE.minusDays(1);

  private static final String RECOMMENDATION_FIELD_NAME = "s8_recommendation";
  private final RecommendationProperties recommendationProperties = new RecommendationProperties(
      Map.of(
          "ACTION_FALSE_POSITIVE", List.of("ACTION_FALSE_POSITIVE"),
          "ACTION_MANUAL_INVESTIGATION", List.of("ACTION_MANUAL_INVESTIGATION"),
          "ACTION_POTENTIAL_TRUE_POSITIVE", List.of("ACTION_POTENTIAL_TRUE_POSITIVE")),
      RECOMMENDATION_FIELD_NAME);
  @Mock
  private AlertRepository alertRepository;
  @Mock
  private DailyRecommendationStatisticsRepository dailyRecommendationStatisticsRepository;
  @Mock
  private TimeSource timeSource;
  private StatisticsCollectorImpl<AlertDto, AlertsRecommendationStatistics>
      dailyAlertRecommendationCollector;

  private static DailyRecommendationStatistics createEmptyDailyStatistics(
      LocalDate localDate) {
    return DailyRecommendationStatistics.builder()
        .day(localDate)
        .alertsCount(0)
        .falsePositivesCount(0)
        .manualInvestigationsCount(0)
        .potentialTruePositivesCount(0)
        .effectivenessPercent(null)
        .efficiencyPercent(null)
        .analystDecisionCount(0)
        .build();
  }

  private static AlertDto buildAlert(LocalDate localDate, Map<String, String> map) {
    return AlertDto
        .builder()
        .id(1L)
        .name("alertName")
        .discriminator("test_desc")
        .createdAt(Timestamp.valueOf(localDate.atStartOfDay()))
        .recommendationDate(Timestamp.valueOf(localDate.atStartOfDay()))
        .payload(map)
        .build();
  }

  @BeforeEach
  void setUp() {
    dailyAlertRecommendationCollector = new StatisticsCollectorImpl<>(
        timeSource,
        new AlertDataExtractor(alertRepository),
        new AlertAggregator(AggregationPeriod.DAILY),
        new AlertRecommendationComputer(new RecommendationMapper(recommendationProperties)),
        new DailyRecommendationPersistence(dailyRecommendationStatisticsRepository),
        1);

    when(timeSource.localDateTime()).thenReturn(NOW.atStartOfDay());
  }

  @Test
  void noDailyStatisticsData_calculationOnAllAlerts() {
    // Given
    when(dailyRecommendationStatisticsRepository.findFirstByOrderByDayWithOffset(1)).thenReturn(
        Optional.empty());
    when(alertRepository.getEarliestAlertLocaDate()).thenReturn(OLDEST_DAILY_STATISTICS);
    when(alertRepository.fetchAlerts(any(), any())).thenReturn(
        List.of(buildAlert(
            OLDEST_DAILY_STATISTICS,
            Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE"))));
    var dps = DailyRecommendationStatistics.builder()
        .day(OLDEST_DAILY_STATISTICS)
        .alertsCount(1)
        .falsePositivesCount(0)
        .manualInvestigationsCount(0)
        .potentialTruePositivesCount(1)
        .effectivenessPercent(null)
        .efficiencyPercent(100.0)
        .analystDecisionCount(0)
        .build();
    var dps2 =
        createEmptyDailyStatistics(AGGREGATION_DATE);

    // When
    dailyAlertRecommendationCollector.generateStatisticsData();

    // Then
    ArgumentCaptor<DailyRecommendationStatistics> argument =
        ArgumentCaptor.forClass(DailyRecommendationStatistics.class);
    verify(dailyRecommendationStatisticsRepository, times(2)).save(argument.capture());
    assertThat(argument.getAllValues()).containsExactlyInAnyOrder(dps, dps2);
  }

  @Test
  void missingDailyStatisticsFromPreviousDay_calculationMadeOnCurrentMissingAndDateBefore() {
    // Given
    when(dailyRecommendationStatisticsRepository.findFirstByOrderByDayWithOffset(1)).thenReturn(
        Optional.of(DailyRecommendationStatistics
            .builder()
            .day(OLDEST_DAILY_STATISTICS.minusDays(1)).build()));
    when(alertRepository.fetchAlerts(any(), any())).thenReturn(
        List.of(
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_MANUAL_INVESTIGATION")),
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE")),
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE")),
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_FALSE_POSITIVE")),
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_FALSE_POSITIVE")),
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_FALSE_POSITIVE"))
        ));
    var dps = DailyRecommendationStatistics.builder()
        .day(AGGREGATION_DATE)
        .alertsCount(6)
        .falsePositivesCount(3)
        .manualInvestigationsCount(1)
        .potentialTruePositivesCount(2)
        .effectivenessPercent(null)
        .analystDecisionCount(0)
        .efficiencyPercent(((double) 5 / 6) * 100)
        .build();

    var dps2 =
        createEmptyDailyStatistics(OLDEST_DAILY_STATISTICS);

    var dps3 = createEmptyDailyStatistics(OLDEST_DAILY_STATISTICS.minusDays(1));

    // When
    dailyAlertRecommendationCollector.generateStatisticsData();

    // Then
    ArgumentCaptor<DailyRecommendationStatistics> argument =
        ArgumentCaptor.forClass(DailyRecommendationStatistics.class);
    verify(dailyRecommendationStatisticsRepository, times(3)).save(argument.capture());
    assertThat(argument.getAllValues()).containsExactlyInAnyOrder(dps, dps2, dps3);
  }

  @Test
  void noDailyStatisticsForCurrentExecution_calculationMadeOnCurrentDateAndDayBefore() {
    // Given
    when(dailyRecommendationStatisticsRepository.findFirstByOrderByDayWithOffset(1)).thenReturn(
        Optional.of(DailyRecommendationStatistics.builder().day(RECALCULATION_DATE)
            .build()));
    when(timeSource.localDateTime()).thenReturn(LocalDateTime.now());
    when(alertRepository.fetchAlerts(any(), any())).thenReturn(
        List.of(
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_MANUAL_INVESTIGATION")),
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE")),
            buildAlert(
                OLDEST_DAILY_STATISTICS,
                Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE")),
            buildAlert(
                AGGREGATION_DATE,
                Map.of("s8_recommendation", "ACTION_FALSE_POSITIVE")),
            buildAlert(
                OLDEST_DAILY_STATISTICS,
                Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE")),
            buildAlert(
                OLDEST_DAILY_STATISTICS,
                Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE"))
        ));
    var dps = DailyRecommendationStatistics.builder()
        .day(AGGREGATION_DATE)
        .alertsCount(3)
        .falsePositivesCount(1)
        .manualInvestigationsCount(1)
        .potentialTruePositivesCount(1)
        .effectivenessPercent(null)
        .analystDecisionCount(0)
        .efficiencyPercent(((double) 2 / 3) * 100)
        .build();

    var dps2 = DailyRecommendationStatistics.builder()
        .day(OLDEST_DAILY_STATISTICS)
        .alertsCount(3)
        .falsePositivesCount(0)
        .manualInvestigationsCount(0)
        .potentialTruePositivesCount(3)
        .effectivenessPercent(null)
        .analystDecisionCount(0)
        .efficiencyPercent(100.0)
        .build();

    // When
    dailyAlertRecommendationCollector.generateStatisticsData();

    // Then
    ArgumentCaptor<DailyRecommendationStatistics> argument =
        ArgumentCaptor.forClass(DailyRecommendationStatistics.class);
    verify(dailyRecommendationStatisticsRepository, times(2)).save(argument.capture());
    assertThat(argument.getAllValues()).containsExactlyInAnyOrder(dps, dps2);
  }
}