package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.rbs.domain.RbsReportService.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.DAY;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncReportGenerationServiceTest {

  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final ReportDefinition TYPE = DAY;
  private static final OffsetDateTime FROM = OffsetDateTime.ofInstant(
      TYPE.getFrom(TIME_SOURCE.now()), TIME_SOURCE.timeZone().toZoneId());
  private static final OffsetDateTime TO = OffsetDateTime.ofInstant(
      TYPE.getTo(TIME_SOURCE.now()), TIME_SOURCE.timeZone().toZoneId());
  private static final CsvReportContentDto REPORT_CONTENT = CsvReportContentDto
      .of("test", of("lines"));

  private final InMemoryRbsRepository rbsReportRepository = new InMemoryRbsRepository();
  @Mock
  private RbsReportGenerationService reportGenerationService;
  @Mock
  private AsyncRbsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncRbsReportGenerationService(
        rbsReportRepository, reportGenerationService, TIME_SOURCE);
  }

  @Test
  void generateReportAndReportAvailable() {
    when(reportGenerationService.generateReport(FROM, TO, PRODUCTION_ANALYSIS_NAME))
        .thenReturn(REPORT_CONTENT);
    Report rbsReport = rbsReportRepository.save(Report.of(TYPE));
    assertThat(rbsReport.getState()).isEqualTo(ReportState.NEW);

    underTest.generateReport(rbsReport.getId());

    rbsReport = rbsReportRepository.getById(rbsReport.getId());
    assertThat(rbsReport.getState()).isEqualTo(ReportState.DONE);
    assertThat(rbsReport.getFile()).isEqualTo(REPORT_CONTENT.getReport());
  }
}
