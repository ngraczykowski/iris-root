package com.silenteight.warehouse.report.rbs.v1.domain;

import com.silenteight.warehouse.report.rbs.v1.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedReportDefinition.DAY;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedReportServiceTest {

  private static final DeprecatedReportDefinition TYPE = DAY;

  private final DeprecatedInMemoryRbsRepository
      rbsReportRepository = new DeprecatedInMemoryRbsRepository();

  @Mock
  private DeprecatedAsyncRbsReportGenerationService asyncReportGenerationService;

  private DeprecatedRbsReportService service;
  private DeprecatedRbsReportQuery query;

  @BeforeEach
  void setUp() {
    service = new DeprecatedRbsReportService(rbsReportRepository, asyncReportGenerationService);
    query = new DeprecatedRbsReportConfiguration().deprecatedRbsReportQuery(rbsReportRepository);
  }

  @Test
  void generateReportAndReportAvailable() {
    ReportInstanceReferenceDto reportInstance = service.createProductionReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());
    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(DeprecatedRbsReport::getState)
        .isEqualTo(DeprecatedReportState.NEW);
  }

  @Test
  void removeReport() {
    ReportInstanceReferenceDto reportInstance = service.createProductionReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());

    service.removeReport(reportInstance.getInstanceReferenceId());

    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId())).isEmpty();
  }
}

