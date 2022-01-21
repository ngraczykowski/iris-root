package com.silenteight.warehouse.report.accuracy.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;
import com.silenteight.warehouse.report.reporting.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportGenerationService;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

@RequiredArgsConstructor
public class AccuracyReportGenerationService {

  @NonNull
  private final ReportGenerationService generationStreamingService;

  public void generateReport(
      @NonNull OffsetDateTime from,
      @NonNull OffsetDateTime to,
      @NonNull List<String> indexes,
      @NonNull @Valid AccuracyReportDefinitionProperties properties,
      @NonNull String name,
      String analysisId) {

    FetchDataRequest request = FetchDataRequest
        .builder()
        .from(from)
        .to(to)
        .dateField(properties.getDateFieldName())
        .fieldsDefinitions(properties.getReportFieldsDefinition())
        .queryFilters(properties.getQueryFilters())
        .indexes(indexes)
        .name(name)
        .useSqlReports(properties.isUseSqlReports())
        .selectSqlQuery(properties.getSelectSqlQuery())
        .sqlTemplates(properties.getSqlTemplates())
        .analysisId(analysisId)
        .build();

    generationStreamingService.generate(request);
  }
}
