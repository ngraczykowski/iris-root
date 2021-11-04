package com.silenteight.warehouse.report.statistics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
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
    EffectivenessDto effectiveness = toEffectivenessDto(rows);
    log.debug(
        "Analysis={}, aiSolvedAsFalsePositive={}, analystSolvedAsFalsePositive={}",
        analysis,
        effectiveness.getAiSolvedAsFalsePositive(),
        effectiveness.getAnalystSolvedAsFalsePositive());

    EfficiencyDto efficiency = toEfficiencyDto(rows);
    log.debug(
        "Analysis={}, allAlerts={}, solvedAlerts={}",
        analysis,
        efficiency.getAllAlerts(),
        efficiency.getSolvedAlerts());

    return StatisticsDto
        .builder()
        .efficiency(efficiency)
        .effectiveness(effectiveness)
        .build();
  }

  private EfficiencyDto toEfficiencyDto(List<Row> rows) {
    long allAlertsCount = sumAlerts(rows);
    long solvedAlertsCount = sumSolvedAlerts(rows);
    return EfficiencyDto
        .builder()
        .allAlerts(allAlertsCount)
        .solvedAlerts(solvedAlertsCount)
        .build();
  }

  private EffectivenessDto toEffectivenessDto(List<Row> rows) {
    List<Row> rowsWithAnalystDecision = getRowsWithAnalystDecision(
        rows, properties.getAnalystDecision());

    List<Row> solvedAsFalsePositiveByAi = getRowsSolvedAsFalsePositiveByAI(rowsWithAnalystDecision);
    long solvedAsFalsePositiveByAiCount = sumAlerts(solvedAsFalsePositiveByAi);

    List<Row> solvedAsFalsePositiveByAnalyst = getRowsSolvedAsFalsePositiveByAnalyst(
        solvedAsFalsePositiveByAi);
    long solvedAsFalsePositiveByAnalystCount = sumAlerts(solvedAsFalsePositiveByAnalyst);

    return getEffectivenessDto(solvedAsFalsePositiveByAiCount, solvedAsFalsePositiveByAnalystCount);
  }

  private EffectivenessDto getEffectivenessDto(
      long solvedAsFalsePositiveByAiCount, long solvedAsFalsePositiveByAnalystCount) {

    if (solvedAsFalsePositiveByAiCount < solvedAsFalsePositiveByAnalystCount)
      return buildDto(0, 0);

    return buildDto(solvedAsFalsePositiveByAiCount, solvedAsFalsePositiveByAnalystCount);
  }

  private EffectivenessDto buildDto(
      long solvedAsFalsePositiveByAiCount, long solvedAsFalsePositiveByAnalystCount) {

    return EffectivenessDto
        .builder()
        .aiSolvedAsFalsePositive(solvedAsFalsePositiveByAiCount)
        .analystSolvedAsFalsePositive(solvedAsFalsePositiveByAnalystCount)
        .build();
  }

  private List<Row> getRowsWithAnalystDecision(
      List<Row> rows, AnalystDecisionProperties analystSignificantValues) {

    List<String> significantValues = analystSignificantValues.getAnalystSignificantValues();
    String analystDecisionField = analystSignificantValues.getField();

    return rows
        .stream()
        .filter(row -> significantValues.contains(row.getValue(analystDecisionField)))
        .collect(toList());
  }

  private List<Row> getRowsSolvedAsFalsePositiveByAI(List<Row> rows) {
    AiDecisionProperties aiDecisionProperties = properties.getAiDecision();
    String aiDecisionField = aiDecisionProperties.getField();
    String aiFalsePositiveValue = aiDecisionProperties.getFalsePositiveValue();
    return rows
        .stream()
        .filter(row -> aiFalsePositiveValue.equals(row.getValue(aiDecisionField)))
        .collect(toList());
  }

  private FetchGroupedDataResponse fetchRawData(String analysis) {
    List<String> indexes = indexerQuery.getIndexesForAnalysis(analysis);
    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest
        .builder()
        .from(EPOCH)
        .to(TIME_CONVERTER.toOffset(INSTANCE.now()))
        .dateField(properties.getDateFieldName())
        .fields(properties.getFields())
        .queryFilters(properties.getQueryFilters())
        .indexes(indexes)
        .build();

    log.debug("Analysis={}, FetchGroupedTimeRangedDataRequest={}", analysis, request);
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

  private List<Row> getRowsSolvedAsFalsePositiveByAnalyst(List<Row> rows) {
    AnalystDecisionProperties analystDecisionProperties = properties.getAnalystDecision();
    String analystDecisionField = analystDecisionProperties.getField();
    String analystFalsePositiveValue = analystDecisionProperties.getFalsePositiveValue();
    return rows
        .stream()
        .filter(row -> analystFalsePositiveValue.equals(row.getValue(analystDecisionField)))
        .collect(toList());
  }

  private static long sumAlerts(List<Row> rows) {
    return rows.stream().mapToLong(Row::getCount).sum();
  }
}
