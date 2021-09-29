package com.silenteight.warehouse.report.accuracy.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.accuracy.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportGenerationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReportDefinition.DAY;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.accuracy.generation.GenerationAccuracyReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncAccuracyReportGenerationServiceTest {

  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final AccuracyReportDefinition TYPE = DAY;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());

  private final InMemoryAccuracyRepository repository = new InMemoryAccuracyRepository();

  @Mock
  private AccuracyReportGenerationService reportGenerationService;
  private AsyncAccuracyReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncAccuracyReportGenerationService(
        repository,
        reportGenerationService,
        TIME_SOURCE);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    AccuracyReport accuracyReport =
        repository.save(AccuracyReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    assertThat(accuracyReport.getState()).isEqualTo(NEW);

    // when
    Long id = accuracyReport.getId();
    underTest.generateReport(id, INDEXES, PROPERTIES);

    // then
    accuracyReport = repository.getById(id);
    assertThat(accuracyReport.getState()).isEqualTo(DONE);
    assertThat(accuracyReport.getFile()).isEqualTo(id + "-" + TYPE.getFilename());
  }

  @Test
  void shouldFailReport() {
    // given
    AccuracyReport accuracyReport =
        repository.save(AccuracyReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    doThrow(RuntimeException.class).when(
        reportGenerationService).generateReport(FROM, TO, INDEXES, PROPERTIES, TYPE.getFilename());

    // when + then
    Long reportId = accuracyReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate accuracy report with id=%d", reportId));

    accuracyReport = repository.getById(accuracyReport.getId());
    assertThat(accuracyReport.getFile()).isNull();
    assertThat(accuracyReport.getState()).isEqualTo(FAILED);
  }
}
