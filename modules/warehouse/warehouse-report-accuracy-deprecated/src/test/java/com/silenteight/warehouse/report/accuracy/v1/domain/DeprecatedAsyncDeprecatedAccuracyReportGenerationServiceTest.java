package com.silenteight.warehouse.report.accuracy.v1.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.accuracy.v1.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.accuracy.v1.generation.DeprecatedAccuracyReportGenerationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.accuracy.v1.DeprecatedAccuracyReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.accuracy.v1.DeprecatedAccuracyReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedAccuracyReportDefinition.DAY;
import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedReportState.DONE;
import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedReportState.FAILED;
import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedReportState.NEW;
import static com.silenteight.warehouse.report.accuracy.v1.generation.DeprecatedGenerationAccuracyReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAsyncDeprecatedAccuracyReportGenerationServiceTest {

  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final DeprecatedAccuracyReportDefinition TYPE = DAY;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());

  private final DeprecatedInMemoryAccuracyRepository
      repository = new DeprecatedInMemoryAccuracyRepository();

  @Mock
  private DeprecatedAccuracyReportGenerationService reportGenerationService;
  private DeprecatedAsyncAccuracyReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedAsyncAccuracyReportGenerationService(
        repository,
        reportGenerationService,
        TIME_SOURCE);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    DeprecatedAccuracyReport accuracyReport =
        repository.save(DeprecatedAccuracyReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    assertThat(accuracyReport.getState()).isEqualTo(NEW);

    // when
    Long id = accuracyReport.getId();
    underTest.generateReport(id, INDEXES, PROPERTIES);

    // then
    accuracyReport = repository.getById(id);
    assertThat(accuracyReport.getState()).isEqualTo(DONE);
    assertThat(accuracyReport.getFileStorageName()).isEqualTo(id + "-" + TYPE.getFilename());
  }

  @Test
  void shouldFailReport() {
    // given
    DeprecatedAccuracyReport accuracyReport =
        repository.save(DeprecatedAccuracyReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    doThrow(RuntimeException.class).when(
        reportGenerationService).generateReport(FROM, TO, INDEXES, PROPERTIES, TYPE.getFilename());

    // when + then
    Long reportId = accuracyReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate accuracy report with id=%d", reportId));

    accuracyReport = repository.getById(accuracyReport.getId());
    assertThat(accuracyReport.getFileStorageName()).isNull();
    assertThat(accuracyReport.getState()).isEqualTo(FAILED);
  }
}
