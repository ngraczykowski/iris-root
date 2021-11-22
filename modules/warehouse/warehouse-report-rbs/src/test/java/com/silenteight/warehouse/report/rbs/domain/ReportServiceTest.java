package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.rbs.generation.GenerationRbScorerReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  private final InMemoryRbsRepository rbsReportRepository = new InMemoryRbsRepository();

  @Mock
  private AsyncRbsReportGenerationService asyncReportGenerationService;

  private RbsReportService service;

  @BeforeEach
  void setUp() {
    service = new RbsReportService(rbsReportRepository, asyncReportGenerationService);
  }

  @Test
  void generateReportAndReportAvailable() {
    ReportInstanceReferenceDto reportInstance =
        service.createReportInstance(REPORT_RANGE, INDEXES, PROPERTIES);

    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId()))
        .isPresent()
        .get()
        .extracting(RbsReport::getState)
        .isEqualTo(NEW);
  }

  @Test
  void removeReport() {
    ReportInstanceReferenceDto reportInstance =
        service.createReportInstance(REPORT_RANGE, INDEXES, PROPERTIES);

    service.removeReport(reportInstance.getInstanceReferenceId());

    assertThat(rbsReportRepository.findById(reportInstance.getInstanceReferenceId())).isEmpty();
  }
}
