package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.billing.domain.BillingReportService.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.billing.domain.ReportDefinition.THIS_YEAR;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncBillingReportGenerationServiceTest {

  private static final ReportDefinition TYPE = THIS_YEAR;
  private static final OffsetDateTime FROM = TYPE.getFrom();
  private static final OffsetDateTime TO = TYPE.getTo();
  private static final CsvReportContentDto REPORT_CONTENT = new CsvReportContentDto(
      "test", of("lines"), "checksum");

  private final InMemoryBillingRepository billingReportRepository = new InMemoryBillingRepository();
  @Mock
  private BillingReportGenerationService reportGenerationService;
  @Mock
  private BillingReportAsyncGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new BillingReportAsyncGenerationService(
        billingReportRepository, reportGenerationService);
  }

  @Test
  void generateReportAndReportAvailable() {
    when(reportGenerationService.generateReport(FROM, TO, PRODUCTION_ANALYSIS_NAME))
        .thenReturn(REPORT_CONTENT);
    BillingReport rbsReport = billingReportRepository.save(BillingReport.of(TYPE));
    assertThat(rbsReport.getState()).isEqualTo(ReportState.NEW);

    underTest.generateReport(rbsReport.getId());

    rbsReport = billingReportRepository.getById(rbsReport.getId());
    assertThat(rbsReport.getState()).isEqualTo(ReportState.DONE);
    assertThat(rbsReport.getFile()).isEqualTo(REPORT_CONTENT.getReport());
  }
}
