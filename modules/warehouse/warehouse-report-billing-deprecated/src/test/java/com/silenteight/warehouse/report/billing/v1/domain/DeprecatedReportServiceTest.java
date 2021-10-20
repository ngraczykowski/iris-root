package com.silenteight.warehouse.report.billing.v1.domain;

import com.silenteight.warehouse.report.billing.v1.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedReportServiceTest {

  private static final DeprecatedReportDefinition TYPE = DeprecatedReportDefinition.THIS_YEAR;

  private final DeprecatedInMemoryBillingRepository
      rbsReportRepository = new DeprecatedInMemoryBillingRepository();

  @Mock
  private DeprecatedBillingReportAsyncGenerationService asyncReportGenerationService;

  private DeprecatedBillingReportService service;
  private DeprecatedBillingReportQuery query;

  @BeforeEach
  void setUp() {
    service = new DeprecatedBillingReportService(rbsReportRepository, asyncReportGenerationService);
    query = new DeprecatedBillingReportConfiguration().deprecatedBillingReportQuery(
        rbsReportRepository);
  }

  @Test
  void generateReportAndReportAvailable() {
    ReportInstanceReferenceDto reportInstance = service.createReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());
    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(DeprecatedBillingReport::getState)
        .isEqualTo(DeprecatedReportState.NEW);
  }

  @Test
  void removeReport() {
    ReportInstanceReferenceDto reportInstance = service.createReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());

    service.removeReport(reportInstance.getInstanceReferenceId());

    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId())).isEmpty();
  }
}

