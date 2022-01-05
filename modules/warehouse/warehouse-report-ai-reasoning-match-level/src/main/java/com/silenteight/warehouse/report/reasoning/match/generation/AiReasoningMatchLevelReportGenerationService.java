package com.silenteight.warehouse.report.reasoning.match.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;
import com.silenteight.warehouse.report.reporting.AiReasoningMatchLevelReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportGenerationService;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

@RequiredArgsConstructor
public class AiReasoningMatchLevelReportGenerationService {

  @NonNull
  private final ReportGenerationService generationStreamingService;

  public void generateReport(
      @NonNull OffsetDateTime from,
      @NonNull OffsetDateTime to,
      @NonNull List<String> indexes,
      @NonNull @Valid AiReasoningMatchLevelReportDefinitionProperties properties,
      @NonNull String name) {

    FetchDataRequest request = FetchDataRequest
        .builder()
        .from(from)
        .to(to)
        .dateField(properties.getDateFieldName())
        .fieldsDefinitions(properties.getReportFieldsDefinition())
        .queryFilters(properties.getQueryFilters())
        .indexes(indexes)
        .name(name)
        .build();

    generationStreamingService.generate(request);
  }
}
