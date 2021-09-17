package com.silenteight.warehouse.report.metrics.domain;


import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition.DAY;
import static com.silenteight.warehouse.report.metrics.generation.GenerationMetricsReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MetricsReportServiceTest {

  private static final MetricsReportDefinition TYPE = DAY;

  private final InMemoryMetricsRepository repository = new InMemoryMetricsRepository();

  @Mock
  private AsyncMetricsReportGenerationService asyncReportGenerationService;
  @Mock
  private IndexesQuery indexQuery;
  private MetricsReportService underTest;
  private MetricsReportQuery query;

  @BeforeEach
  void setUp() {
    underTest = new MetricsReportService(
        repository,
        asyncReportGenerationService,
        indexQuery,
        indexQuery,
        PROPERTIES,
        PROPERTIES);

    query = new MetricsReportConfiguration().metricsReportQuery(repository);
  }

  @Test
  void shouldGenerateReport() {
    // when
    ReportInstanceReferenceDto reportInstance =
        underTest.createProductionReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME);

    // then
    ReportDto report = query.getReport(reportInstance.getGetInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());
    assertThat(repository.findById(reportInstance.getGetInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(MetricsReport::getState)
        .isEqualTo(ReportState.NEW);
  }

  @Test
  void removeReport() {
    // given
    ReportInstanceReferenceDto reportInstance =
        underTest.createProductionReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME);

    ReportDto report = query.getReport(reportInstance.getGetInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());

    // when
    underTest.removeReport(reportInstance.getGetInstanceReferenceId());

    // then
    assertThat(repository.findById(reportInstance.getGetInstanceReferenceId())).isEmpty();
  }
}
