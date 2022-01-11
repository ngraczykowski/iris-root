package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.metrics.generation.GenerationMetricsReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MetricsReportServiceTest {

  private final InMemoryMetricsRepository repository = new InMemoryMetricsRepository();

  @Mock
  private AsyncMetricsReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private MetricsReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new MetricsReportService(repository, asyncReportGenerationService, reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    ReportInstanceReferenceDto reportInstance =
        underTest.createReportInstance(REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    assertThat(repository.findById(reportInstance.getInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(MetricsReport::getState)
        .isEqualTo(ReportState.NEW);
  }

  @Test
  void removeReport() {
    // given
    ReportInstanceReferenceDto reportInstance =
        underTest.createReportInstance(REPORT_RANGE, INDEXES, PROPERTIES);

    // when
    underTest.removeReport(reportInstance.getInstanceReferenceId());

    // then
    assertThat(repository.findById(reportInstance.getInstanceReferenceId())).isEmpty();
  }
}
