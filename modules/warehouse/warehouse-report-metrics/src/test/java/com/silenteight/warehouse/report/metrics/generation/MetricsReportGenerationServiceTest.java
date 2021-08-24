package com.silenteight.warehouse.report.metrics.generation;

import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.*;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.DATE;
import static com.silenteight.warehouse.report.metrics.generation.GenerationMetricsReportTestFixtures.PROPERTIES;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsReportGenerationServiceTest {

  @Mock
  private GroupingQueryService groupingQueryService;
  private MetricsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new MetricsReportGenerationService(groupingQueryService);
  }

  @Test
  void generateNoDataReport() {
    // given
    when(groupingQueryService.generate(any())).thenReturn(
        FetchGroupedDataResponse.builder().rows(emptyList()).build());

    // when
    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), INDEXES, PROPERTIES);

    // then
    assertThat(reportContent.getLines()).isEmpty();
  }

  @Test
  void generateReport() {
    // given
    List<Row> responseData = getRows();

    FetchGroupedDataResponse response = FetchGroupedDataResponse
        .builder().rows(responseData).build();
    when(groupingQueryService.generate(any())).thenReturn(response);

    // when
    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), INDEXES, PROPERTIES);

    // then
    assertThat(reportContent.getReport()).isEqualTo(
        "Date,Country,Risk Type,Hit Type,Efficiency,PTP Effectiveness,FP Effectiveness\n"
            + "2021-06,DE,Fraud,Fraud,0.882,0.583,0.333\n"
            + "2021-06,UK,Sanctions,San,1.000,1.000,0.000\n"
            + "2021-07,DE,Fraud,Fraud,1.000,0.000,0.714\n"
            + "2021-07,PL,AML,AML,1.000,1.000,0.000\n"
    );
  }

  private List<Row> getRows() {
    return of(
        buildRow(
            "PL",
            "AML",
            "AML",
            DATE,
            RECOMMENDATION_FIELD_POSITIVE_VALUE,
            ANALYST_FIELD_POSITIVE_VALUE,
            QA_FIELD_POSITIVE_VALUE,
            3),
        buildRow(
            "UK",
            "Sanctions",
            "San",
            DATE_2,
            RECOMMENDATION_FIELD_POSITIVE_VALUE,
            ANALYST_FIELD_POSITIVE_VALUE,
            QA_FIELD_NEGATIVE_VALUE,
            5),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE_2,
            RECOMMENDATION_FIELD_MEANINGLESS_VALUE,
            ANALYST_FIELD_NEGATIVE_VALUE,
            QA_FIELD_POSITIVE_VALUE,
            4),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE_2,
            RECOMMENDATION_FIELD_NEGATIVE_VALUE,
            ANALYST_FIELD_NEGATIVE_VALUE,
            QA_FIELD_POSITIVE_VALUE,
            2),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE_2,
            RECOMMENDATION_FIELD_NEGATIVE_VALUE,
            ANALYST_FIELD_NEGATIVE_VALUE,
            QA_FIELD_NEGATIVE_VALUE,
            4),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE_2,
            RECOMMENDATION_FIELD_POSITIVE_VALUE,
            ANALYST_FIELD_POSITIVE_VALUE,
            QA_FIELD_NEGATIVE_VALUE,
            14),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE_2,
            RECOMMENDATION_FIELD_POSITIVE_VALUE,
            ANALYST_FIELD_NEGATIVE_VALUE,
            QA_FIELD_POSITIVE_VALUE,
            10),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE,
            RECOMMENDATION_FIELD_POSITIVE_VALUE,
            ANALYST_FIELD_NEGATIVE_VALUE,
            QA_FIELD_POSITIVE_VALUE,
            2),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE,
            RECOMMENDATION_FIELD_NEGATIVE_VALUE,
            ANALYST_FIELD_POSITIVE_VALUE,
            QA_FIELD_POSITIVE_VALUE,
            5),
        buildRow(
            COUNTRY_DE,
            RISK_TYPE_FRAUD,
            RISK_TYPE_FRAUD,
            DATE,
            RECOMMENDATION_FIELD_NEGATIVE_VALUE,
            ANALYST_FIELD_POSITIVE_VALUE,
            QA_FIELD_NEGATIVE_VALUE,
            2));
  }

  private Row buildRow(
      String country,
      String riskType,
      String hitType,
      String date,
      String recommendation,
      String analystDecision,
      String qaDecision,
      long count) {

    Map<String, String> map = Map.of(
        DATE_FIELD_NAME, date,
        COUNTRY_FIELD_NAME, country,
        RISK_TYPE_FIELD_NAME, riskType,
        HIT_TYPE_FIELD_NAME, hitType,
        RECOMMENDATION_FIELD_NAME, recommendation,
        ANALYST_FIELD_NAME, analystDecision,
        QA_FIELD_NAME, qaDecision);

    return Row.builder().data(map).count(count).build();
  }
}
