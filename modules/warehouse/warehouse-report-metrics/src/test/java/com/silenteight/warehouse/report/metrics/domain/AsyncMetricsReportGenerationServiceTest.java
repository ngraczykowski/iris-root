package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.metrics.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition.DAY;
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

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final MetricsReportDefinition TYPE = DAY;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());
  private static final CsvReportContentDto REPORT_CONTENT = CsvReportContentDto
      .of("test", of("lines"));

  private final InMemoryMetricsRepository repository = new InMemoryMetricsRepository();

  @Mock
  private MetricsReportGenerationService reportGenerationService;
  private AsyncMetricsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncMetricsReportGenerationService(
        repository,
        reportGenerationService,
        TIME_SOURCE);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, PROPERTIES))
        .thenReturn(REPORT_CONTENT);

    MetricsReport metricsReport = repository.save(MetricsReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    assertThat(metricsReport.getState()).isEqualTo(NEW);

    // when
    underTest.generateReport(metricsReport.getId(), INDEXES, PROPERTIES);

    // then
    metricsReport = repository.getById(metricsReport.getId());
    assertThat(metricsReport.getState()).isEqualTo(DONE);
    assertThat(metricsReport.getFile()).isEqualTo(REPORT_CONTENT.getReport());
  }

  @Test
  void shouldFailReport() {
    // given
    MetricsReport metricsReport = repository.save(MetricsReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, PROPERTIES))
        .thenThrow(RuntimeException.class);

    // when + then
    Long reportId = metricsReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate report with id=%d", reportId));

    metricsReport = repository.getById(metricsReport.getId());
    assertThat(metricsReport.getState()).isEqualTo(FAILED);
  }
}
