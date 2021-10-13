package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.metrics.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.metrics.generation.DeprecatedMetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.metrics.DeprecatedMetricsReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.metrics.domain.DeprecatedMetricsReportDefinition.DAY;
import static com.silenteight.warehouse.report.metrics.domain.DeprecatedReportState.DONE;
import static com.silenteight.warehouse.report.metrics.domain.DeprecatedReportState.FAILED;
import static com.silenteight.warehouse.report.metrics.domain.DeprecatedReportState.NEW;
import static com.silenteight.warehouse.report.metrics.generation.DeprecatedGenerationMetricsReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAsyncMetricsReportGenerationServiceTest {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final DeprecatedMetricsReportDefinition TYPE = DAY;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());
  private static final CsvReportContentDto REPORT_CONTENT = CsvReportContentDto
      .of("test", of("lines"));

  private final DeprecatedInMemoryMetricsRepository
      repository = new DeprecatedInMemoryMetricsRepository();

  @Mock
  private DeprecatedMetricsReportGenerationService reportGenerationService;
  private DeprecatedAsyncMetricsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedAsyncMetricsReportGenerationService(
        repository,
        reportGenerationService,
        TIME_SOURCE);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, PROPERTIES))
        .thenReturn(REPORT_CONTENT);

    DeprecatedMetricsReport deprecatedMetricsReport = repository.save(
        DeprecatedMetricsReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    assertThat(deprecatedMetricsReport.getState()).isEqualTo(NEW);

    // when
    underTest.generateReport(deprecatedMetricsReport.getId(), INDEXES, PROPERTIES);

    // then
    deprecatedMetricsReport = repository.getById(deprecatedMetricsReport.getId());
    assertThat(deprecatedMetricsReport.getState()).isEqualTo(DONE);
    assertThat(deprecatedMetricsReport.getFile()).isEqualTo(REPORT_CONTENT.getReport());
  }

  @Test
  void shouldFailReport() {
    // given
    DeprecatedMetricsReport deprecatedMetricsReport = repository.save(
        DeprecatedMetricsReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, PROPERTIES))
        .thenThrow(RuntimeException.class);

    // when + then
    Long reportId = deprecatedMetricsReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate report with id=%d", reportId));

    deprecatedMetricsReport = repository.getById(deprecatedMetricsReport.getId());
    assertThat(deprecatedMetricsReport.getState()).isEqualTo(FAILED);
  }
}
