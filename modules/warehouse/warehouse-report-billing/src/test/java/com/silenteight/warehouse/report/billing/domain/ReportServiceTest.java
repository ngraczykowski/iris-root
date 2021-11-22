package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_RANGE;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  private final InMemoryBillingRepository rbsReportRepository = new InMemoryBillingRepository();

  @Mock
  private BillingReportAsyncGenerationService asyncReportGenerationService;

  private BillingReportService service;

  @BeforeEach
  void setUp() {
    service = new BillingReportService(rbsReportRepository, asyncReportGenerationService);
  }

  @Test
  void generateReportAndReportAvailable() {
    // when
    ReportInstanceReferenceDto reportInstance = service.createReportInstance(REPORT_RANGE, INDEXES);

    // then
    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(BillingReport::getState)
        .isEqualTo(ReportState.NEW);
  }

  @Test
  void removeReport() {
    // given
    ReportInstanceReferenceDto reportInstance = service.createReportInstance(REPORT_RANGE, INDEXES);

    // when
    service.removeReport(reportInstance.getInstanceReferenceId());

    // then
    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId())).isEmpty();
  }
}
