package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.warehouse.report.rbs.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.REPORT_FILENAME;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.rbs.generation.GenerationRbScorerReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncReportGenerationServiceTest {

  private static final CsvReportContentDto REPORT_CONTENT =
      CsvReportContentDto.of("test", of("lines"));

  private final InMemoryRbsRepository repository = new InMemoryRbsRepository();

  @Mock
  private RbsReportGenerationService reportGenerationService;
  private AsyncRbsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncRbsReportGenerationService(repository, reportGenerationService);
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

    RbsReport metricsReport = repository.save(RbsReport.of(REPORT_FILENAME));
    assertThat(metricsReport.getState()).isEqualTo(NEW);

    // when
    underTest.generateReport(metricsReport.getId(), REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    metricsReport = repository.getById(metricsReport.getId());
    assertThat(metricsReport.getState()).isEqualTo(DONE);
    assertThat(metricsReport.getData()).isEqualTo(REPORT_CONTENT.getReport());
  }

  @Test
  void shouldFailReport() {
    // given
    RbsReport metricsReport = repository.save(RbsReport.of(REPORT_FILENAME));
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
        .hasMessageContaining(format("Cannot generate RB Scorer report with id=%d", reportId));

    metricsReport = repository.getById(metricsReport.getId());
    assertThat(metricsReport.getState()).isEqualTo(FAILED);
  }
}
