package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.billing.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.BillingReportProperties;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.billing.domain.BillingReport.of;
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
  @Mock
  private BillingReportProperties properties;
  @Mock
  private ReportStorage reportStorage;

  @BeforeEach
  void setUp() {
    underTest = new BillingReportAsyncGenerationService(
        billingReportRepository,
        reportGenerationService,
        properties,
        reportStorage);
  }

  @Test
  void generateReportAndReportAvailable() {
    when(reportGenerationService.generateReport(
        REPORT_RANGE.getFrom(),
        REPORT_RANGE.getTo(),
        INDEXES))
        .thenReturn(REPORT_CONTENT);

    when(properties.isUseSqlReports()).thenReturn(false);

    BillingReport billingReport = billingReportRepository.save(of(REPORT_RANGE));
    assertThat(billingReport.getState()).isEqualTo(ReportState.NEW);

    underTest.generateReport(billingReport.getId(), REPORT_RANGE, INDEXES);

    billingReport = billingReportRepository.getById(billingReport.getId());
    assertThat(billingReport.getState()).isEqualTo(ReportState.DONE);
  }

  @Test
  void shouldFailReport() {
    //given
    BillingReport billingReport = billingReportRepository.save(BillingReport.of(REPORT_RANGE));

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
