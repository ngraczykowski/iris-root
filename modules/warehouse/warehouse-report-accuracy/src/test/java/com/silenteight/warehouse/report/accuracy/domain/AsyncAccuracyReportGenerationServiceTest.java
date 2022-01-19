package com.silenteight.warehouse.report.accuracy.domain;

import com.silenteight.warehouse.report.accuracy.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportGenerationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.PRODUCTION_REPORT_FILENAME;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReport.of;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.accuracy.generation.GenerationAccuracyReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncAccuracyReportGenerationServiceTest {

  private final InMemoryAccuracyRepository repository = new InMemoryAccuracyRepository();

  @Mock
  private AccuracyReportGenerationService reportGenerationService;
  private AsyncAccuracyReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncAccuracyReportGenerationService(repository, reportGenerationService);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    AccuracyReport accuracyReport = repository.save(of(REPORT_RANGE));
    assertThat(accuracyReport.getState()).isEqualTo(NEW);

    // when
    Long id = accuracyReport.getId();
    underTest.generateReport(id, REPORT_RANGE, INDEXES, PROPERTIES, null);

    // then
    accuracyReport = repository.getById(id);
    assertThat(accuracyReport.getState()).isEqualTo(DONE);
  }

  @Test
  void shouldFailReport() {
    // given
    AccuracyReport accuracyReport = repository.save(of(REPORT_RANGE));
    doThrow(RuntimeException.class)
        .when(reportGenerationService)
        .generateReport(
            REPORT_RANGE.getFrom(),
            REPORT_RANGE.getTo(),
            INDEXES,
            PROPERTIES,
            PRODUCTION_REPORT_FILENAME,
            null);

    // when + then
    Long id = accuracyReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(id, REPORT_RANGE, INDEXES, PROPERTIES, null))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate Accuracy report with id=%d", id));

    accuracyReport = repository.getById(accuracyReport.getId());
    assertThat(accuracyReport.getState()).isEqualTo(FAILED);
  }
}
