package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  private static final ReportDefinition TYPE = ReportDefinition.THIS_YEAR;

  private final InMemoryBillingRepository rbsReportRepository = new InMemoryBillingRepository();

  @Mock
  private BillingReportAsyncGenerationService asyncReportGenerationService;

  private BillingReportService service;
  private BillingReportQuery query;

  @BeforeEach
  void setUp() {
    service = new BillingReportService(rbsReportRepository, asyncReportGenerationService);
    query = new BillingReportConfiguration().billingReportQuery(rbsReportRepository);
  }

  @Test
  void generateReportAndReportAvailable() {
    ReportInstanceReferenceDto reportInstance = service.createReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getGetInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());
    assertThat(rbsReportRepository.findById(reportInstance.getGetInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(BillingReport::getState)
        .isEqualTo(ReportState.NEW);
  }

  @Test
  void removeReport() {
    ReportInstanceReferenceDto reportInstance = service.createReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getGetInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());

    service.removeReport(reportInstance.getGetInstanceReferenceId());

    assertThat(rbsReportRepository.findById(reportInstance.getGetInstanceReferenceId())).isEmpty();
  }
}

