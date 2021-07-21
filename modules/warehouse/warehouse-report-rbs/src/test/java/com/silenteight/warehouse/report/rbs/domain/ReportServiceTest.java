package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.DAY;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  private static final ReportDefinition TYPE = DAY;

  private final InMemoryRbsRepository rbsReportRepository = new InMemoryRbsRepository();

  @Mock
  private AsyncRbsReportGenerationService asyncReportGenerationService;

  private RbsReportService service;
  private RbsReportQuery query;

  @BeforeEach
  void setUp() {
    service = new RbsReportService(rbsReportRepository, asyncReportGenerationService);
    query = new RbsReportConfiguration().rbsReportQuery(rbsReportRepository);
  }

  @Test
  void generateReportAndReportAvailable() {
    ReportInstanceReferenceDto reportInstance = service.createProductionReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getGetInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());
    assertThat(rbsReportRepository.findById(reportInstance.getGetInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(RbsReport::getState)
        .isEqualTo(ReportState.NEW);
  }

  @Test
  void removeReport() {
    ReportInstanceReferenceDto reportInstance = service.createProductionReportInstance(TYPE);

    ReportDto report = query.getReport(reportInstance.getGetInstanceReferenceId());
    assertThat(report.getFilename()).isEqualTo(TYPE.getFilename());

    service.removeReport(reportInstance.getGetInstanceReferenceId());

    assertThat(rbsReportRepository.findById(reportInstance.getGetInstanceReferenceId())).isEmpty();
  }
}

