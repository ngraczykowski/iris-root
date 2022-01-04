package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.warehouse.report.metrics.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.metrics.generation.GenerationMetricsReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncMetricsReportGenerationServiceTest {

  private static final CsvReportContentDto REPORT_CONTENT = CsvReportContentDto
      .of("test", of("lines"));

  private final InMemoryMetricsRepository repository = new InMemoryMetricsRepository();

  @Mock
  private MetricsReportGenerationService reportGenerationService;
  @Mock
  private ReportStorage reportStorage;

  private AsyncMetricsReportGenerationService underTest;


  @BeforeEach
  void setUp() {
    underTest =
        new AsyncMetricsReportGenerationService(repository, reportGenerationService, reportStorage);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    when(reportGenerationService.generateReport(
        REPORT_RANGE.getFrom(),
        REPORT_RANGE.getTo(),
        INDEXES,
        PROPERTIES))
        .thenReturn(REPORT_CONTENT);

    MetricsReport metricsReport = repository.save(MetricsReport.of(REPORT_RANGE));
    assertThat(metricsReport.getState()).isEqualTo(NEW);

    // when
    underTest.generateReport(metricsReport.getId(), REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    metricsReport = repository.getById(metricsReport.getId());
    assertThat(metricsReport.getState()).isEqualTo(DONE);
  }

  @Test
  void shouldFailReport() {
    // given
    MetricsReport metricsReport = repository.save(MetricsReport.of(REPORT_RANGE));
    when(reportGenerationService.generateReport(
        REPORT_RANGE.getFrom(),
        REPORT_RANGE.getTo(),
        INDEXES,
        PROPERTIES))
        .thenThrow(RuntimeException.class);

    // when + then
    Long reportId = metricsReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, REPORT_RANGE, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate Metrics report with id=%d", reportId));

    metricsReport = repository.getById(metricsReport.getId());
    assertThat(metricsReport.getState()).isEqualTo(FAILED);
  }
}
