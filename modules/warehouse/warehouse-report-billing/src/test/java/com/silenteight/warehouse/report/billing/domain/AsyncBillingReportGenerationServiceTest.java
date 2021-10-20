package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.billing.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_FILENAME;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_RANGE;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncBillingReportGenerationServiceTest {

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
    when(reportGenerationService.generateReport(
        REPORT_RANGE.getFrom(),
        REPORT_RANGE.getTo(),
        INDEXES))
        .thenReturn(REPORT_CONTENT);

    BillingReport billingReport = billingReportRepository.save(BillingReport.of(REPORT_FILENAME));
    assertThat(billingReport.getState()).isEqualTo(ReportState.NEW);

    underTest.generateReport(billingReport.getId(), REPORT_RANGE, INDEXES);

    billingReport = billingReportRepository.getById(billingReport.getId());
    assertThat(billingReport.getState()).isEqualTo(ReportState.DONE);
    assertThat(billingReport.getData()).isEqualTo(REPORT_CONTENT.getReport());
  }

  @Test
  void shouldFailReport() {
    //given
    BillingReport billingReport = billingReportRepository.save(BillingReport.of(REPORT_FILENAME));

    //when
    when(reportGenerationService.generateReport(
        REPORT_RANGE.getFrom(),
        REPORT_RANGE.getTo(),
        INDEXES))
        .thenThrow(RuntimeException.class);

    //then
    long reportId = billingReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, REPORT_RANGE, INDEXES))
        .isInstanceOf(ReportGenerationException.class);

    billingReport = billingReportRepository.getById(reportId);
    assertThat(billingReport.getState()).isEqualTo(ReportState.FAILED);
  }
}
