package com.silenteight.warehouse.report.statistics.domain;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.statistics.domain.dto.EffectivenessDto;
import com.silenteight.warehouse.report.statistics.domain.dto.EfficiencyDto;
import com.silenteight.warehouse.report.statistics.domain.dto.StatisticsDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsQueryTest {

  private static final String AI_DECISION_FIELD = "alert_recommendation_recommended_action";
  private static final String ANALYST_DECISION_FIELD = "alert_DN_CASE.currentState";

  private static final String AI_DECISION_PTP = "ACTION_POTENTIAL_TRUE_POSITIVE";
  private static final String AI_DECISION_FP = "ACTION_FALSE_POSITIVE";
  private static final String AI_DECISION_MI = "ACTION_INVESTIGATE";

  private static final String ANALYST_DECISION_FP = "Level 1 Review";
  private static final String ANALYST_DECISION_PTP = "Level 2 Review";

  private static final String ANALYSIS_ID = "analysis/123";
  private static final String INDEXES = "sim_index_name";
  private static final String DATE_FIELD_NAME = "alert_timestamp";

  @Mock
  private GroupingQueryService groupingQueryService;
  @Mock
  private IndexesQuery indexerQuery;

  private StatisticsQuery underTest;

  @BeforeEach
  void setUp() {
    StatisticsProperties statisticsProperties =
        new StatisticsProperties(
            DATE_FIELD_NAME,
            new AiDecisionProperties(
                AI_DECISION_FIELD,
                of(AI_DECISION_FP, AI_DECISION_PTP),
                AI_DECISION_FP),
            new AnalystDecisionProperties(
                ANALYST_DECISION_FIELD,
                of(ANALYST_DECISION_FP, ANALYST_DECISION_PTP),
                ANALYST_DECISION_FP
            ),
            emptyList());

    underTest = new StatisticsConfiguration().statisticsQuery(
        groupingQueryService,
        indexerQuery,
        statisticsProperties);
  }

  @Test
  void shouldGetValidStatistics() {
    // given
    when(indexerQuery.getIndexesForAnalysis(ANALYSIS_ID)).thenReturn(of(INDEXES));
    when(groupingQueryService.generate(any())).thenReturn(getResponse());

    // when
    StatisticsDto statisticsDto = underTest.getStatistics(ANALYSIS_ID);

    // then
    EfficiencyDto efficiency = statisticsDto.getEfficiency();
    assertThat(efficiency.getAllAlerts()).isEqualTo(114987);
    assertThat(efficiency.getSolvedAlerts()).isEqualTo(36694);
    EffectivenessDto effectiveness = statisticsDto.getEffectiveness();
    assertThat(effectiveness.getAiSolvedAsFalsePositive()).isEqualTo(29408);
    assertThat(effectiveness.getAnalystSolvedAsFalsePositive()).isEqualTo(29405);
  }

  private FetchGroupedDataResponse getResponse() {
    return FetchGroupedDataResponse
        .builder()
        .rows(buildRows())
        .build();
  }

  private List<Row> buildRows() {
    return of(
        buildRow(AI_DECISION_FP, ANALYST_DECISION_FP, 29405),
        buildRow(AI_DECISION_FP, ANALYST_DECISION_PTP, 3),
        buildRow(AI_DECISION_FP, "", 233),
        buildRow(AI_DECISION_PTP, ANALYST_DECISION_FP, 6907),
        buildRow(AI_DECISION_PTP, ANALYST_DECISION_PTP, 145),
        buildRow(AI_DECISION_PTP, "", 1),
        buildRow(AI_DECISION_MI, ANALYST_DECISION_FP, 78260),
        buildRow(AI_DECISION_MI, ANALYST_DECISION_PTP, 21),
        buildRow(AI_DECISION_MI, "", 12)
    );
  }

  private Row buildRow(String aiDecision, String analystDecision, long count) {
    Map<String, String> dataMap = Map.of(
        AI_DECISION_FIELD, aiDecision,
        ANALYST_DECISION_FIELD, analystDecision
    );
    return Row
        .builder()
        .data(dataMap)
        .count(count)
        .build();
  }
}
