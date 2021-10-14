package com.silenteight.warehouse.report.metrics.v1.domain;


import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.v1.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.metrics.v1.DeprecatedMetricsReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.DAY;
import static com.silenteight.warehouse.report.metrics.v1.generation.DeprecatedGenerationMetricsReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedMetricsReportServiceTest {

  private static final DeprecatedMetricsReportDefinition TYPE = DAY;

  private final DeprecatedInMemoryMetricsRepository
      repository = new DeprecatedInMemoryMetricsRepository();

  @Mock
  private DeprecatedAsyncMetricsReportGenerationService asyncReportGenerationService;
  @Mock
  private IndexesQuery indexQuery;
  private DeprecatedMetricsReportService underTest;
  private DeprecatedMetricsReportQuery query;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedMetricsReportService(
        repository,
        asyncReportGenerationService,
        indexQuery,
        indexQuery,
        PROPERTIES,
        PROPERTIES);

    query = new DeprecatedMetricsReportConfiguration().deprecatedMetricsReportQuery(repository);
  }

  @Test
  void shouldGenerateReport() {
    // when
    ReportInstanceReferenceDto reportInstance =
        underTest.createProductionReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME);

    // then
    ReportDto report = query.getReport(reportInstance.getInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());
    assertThat(repository.findById(reportInstance.getInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(DeprecatedMetricsReport::getState)
        .isEqualTo(DeprecatedReportState.NEW);
  }

  @Test
  void removeReport() {
    // given
    ReportInstanceReferenceDto reportInstance =
        underTest.createProductionReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME);

    ReportDto report = query.getReport(reportInstance.getInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());

    // when
    underTest.removeReport(reportInstance.getInstanceReferenceId());

    // then
    assertThat(repository.findById(reportInstance.getInstanceReferenceId())).isEmpty();
  }
}
