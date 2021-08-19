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

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.METRICS_COUNTRY_FIELD;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.METRICS_RISK_TYPE_FIELD;
import static com.silenteight.warehouse.report.metrics.generation.GenerationMetricsReportTestFixtures.PROPERTIES;
import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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
  void generateThreeRowsReport() {
    // given
    List<Row> responseData = asList(
        buildRow("PL", "AML", 1),
        buildRow("UK", "Sanctions", 1),
        buildRow("DE", "Fraud", 2));

    FetchGroupedDataResponse response = FetchGroupedDataResponse
        .builder().rows(responseData).build();
    when(groupingQueryService.generate(any())).thenReturn(response);

    // when
    CsvReportContentDto reportContent = underTest.generateReport(
        now(), now(), INDEXES, PROPERTIES);

    // then
    assertThat(reportContent.getReport()).isEqualTo(
        "Country,Risk Type,Efficiency\n"
            + "DE,Fraud,0.000\n"
            + "PL,AML,0.000\n"
            + "UK,Sanctions,0.000\n"
    );
  }

  private Row buildRow(String country, String riskType, long count) {
    Map<String, String> map = Map.of(
        METRICS_COUNTRY_FIELD, country,
        METRICS_RISK_TYPE_FIELD, riskType);

    return Row.builder().data(map).count(count).build();
  }
}
