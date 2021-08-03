package com.silenteight.warehouse.report.statistics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedTimeRangedDataRequest;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.statistics.domain.dto.EffectivenessDto;
import com.silenteight.warehouse.report.statistics.domain.dto.EfficiencyDto;
import com.silenteight.warehouse.report.statistics.domain.dto.StatisticsDto;
import com.silenteight.warehouse.report.statistics.simulation.SimulationStatisticsQuery;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class StatisticsQuery implements SimulationStatisticsQuery {

  private static final DefaultTimeSource INSTANCE = DefaultTimeSource.INSTANCE;
  private static final TimeConverter TIME_CONVERTER = DefaultTimeSource.TIME_CONVERTER;
  private static final OffsetDateTime EPOCH = TIME_CONVERTER.toOffset(Instant.EPOCH);
  private static final OffsetDateTime NOW = TIME_CONVERTER.toOffset(INSTANCE.now());

  @NonNull
  private final GroupingQueryService groupingQueryService;
  @NonNull
  private final IndexesQuery indexerQuery;
  @NonNull
  private final StatisticsProperties properties;

  @Override
  public StatisticsDto getStatistics(String analysis) {
    log.info("Getting statistics for analysis={}", analysis);
    FetchGroupedDataResponse response = fetchRawData(analysis);
    List<Row> rows = response.getRows();
    EfficiencyDto efficiency = toEfficiencyDto(rows);
    log.debug(
        "Analysis={}, aiSolvedAsFalsePositive={}, analystSolvedAsFalsePositive={}",
        analysis,
        efficiency.getAiSolvedAsFalsePositive(),
        efficiency.getAnalystSolvedAsFalsePositive());

    EffectivenessDto effectiveness = toEffectivenessDto(rows);
    log.debug(
        "Analysis={}, allAlerts={}, solvedAlerts={}",
        analysis,
        effectiveness.getAllAlerts(),
        effectiveness.getSolvedAlerts());

    return StatisticsDto
        .builder()
        .effectiveness(effectiveness)
        .efficiency(efficiency)
        .build();
  }

  private EffectivenessDto toEffectivenessDto(List<Row> rows) {
    long allAlertsCount = sumAlerts(rows);
    long solvedAlertsCount = sumSolvedAlerts(rows);
    return EffectivenessDto
        .builder()
        .allAlerts(allAlertsCount)
        .solvedAlerts(solvedAlertsCount)
        .build();
  }

  private EfficiencyDto toEfficiencyDto(List<Row> rows) {
    List<Row> solvedAsFalsePositiveByAi = getRowsSolvedAsFalsePositiveByAi(rows);
    long solvedAsFalsePositiveByAiCount = sumAlerts(solvedAsFalsePositiveByAi);
    long solvedAsFalsePositiveByAnalystCount =
        sumSolvedAsFalsePositiveByAnalyst(solvedAsFalsePositiveByAi);

    return EfficiencyDto
        .builder()
        .aiSolvedAsFalsePositive(solvedAsFalsePositiveByAiCount)
        .analystSolvedAsFalsePositive(solvedAsFalsePositiveByAnalystCount)
        .build();
  }

  private FetchGroupedDataResponse fetchRawData(String analysis) {
    List<String> indexes = indexerQuery.getIndexesForAnalysis(analysis);
    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest
        .builder()
        .from(EPOCH)
        .to(NOW)
        .dateField(properties.getDateFieldName())
        .fields(properties.getFields())
        .indexes(indexes)
        .build();

    return groupingQueryService.generate(request);
  }

  private long sumSolvedAlerts(List<Row> rows) {
    AiDecisionProperties aiDecisionProperties = properties.getAiDecision();
    List<String> significantValues = aiDecisionProperties.getSignificantValues();
    String aiDecisionField = aiDecisionProperties.getField();
    return rows
        .stream()
        .filter(row -> significantValues.contains(row.getValue(aiDecisionField)))
        .mapToLong(Row::getCount)
        .sum();
  }

  private long sumSolvedAsFalsePositiveByAnalyst(List<Row> rows) {
    AnalystDecisionProperties analystDecisionProperties = properties.getAnalystDecision();
    String analystDecisionField = analystDecisionProperties.getField();
    String analystFalsePositiveValue = analystDecisionProperties.getFalsePositiveValue();
    return rows
        .stream()
        .filter(row -> analystFalsePositiveValue.equals(row.getValue(analystDecisionField)))
        .mapToLong(Row::getCount)
        .sum();
  }

  private List<Row> getRowsSolvedAsFalsePositiveByAi(List<Row> rows) {
    AiDecisionProperties aiDecisionProperties = properties.getAiDecision();
    String aiFalsePositiveValue = aiDecisionProperties.getFalsePositiveValue();
    String aiDecisionField = aiDecisionProperties.getField();
    return rows
        .stream()
        .filter(row -> aiFalsePositiveValue.equals(row.getValue(aiDecisionField)))
        .collect(toList());
  }

  private static long sumAlerts(List<Row> rows) {
    return rows.stream().mapToLong(Row::getCount).sum();
  }
}
