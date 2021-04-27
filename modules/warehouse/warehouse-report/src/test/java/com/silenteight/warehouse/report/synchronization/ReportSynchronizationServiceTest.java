package com.silenteight.warehouse.report.synchronization;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.silenteight.warehouse.report.synchronization.ReportEntityFixture.REPORT;
import static com.silenteight.warehouse.report.synchronization.ReportFixture.FILENAME;
import static com.silenteight.warehouse.report.synchronization.ReportFixture.KIBANA_REPORT_INSTANCE_ID;
import static com.silenteight.warehouse.report.synchronization.ReportFixture.REPORT_ID;
import static com.silenteight.warehouse.report.synchronization.ReportFixture.TENANT;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;

class ReportSynchronizationServiceTest {

  private final InMemoryReportRepository inMemoryReportRepository = new InMemoryReportRepository();
  private final ReportSynchronizationService underTest = new ReportSynchronizationService(
      inMemoryReportRepository);

  private static final String EXISTING_KIBANA_REPORT_INSTANCE_ID = KIBANA_REPORT_INSTANCE_ID;
  private static final String NEW_KIBANA_REPORT_INSTANCE_ID = "456";

  @Test
  void shouldStoreReportDetails() {
    underTest.markAsStored(EXISTING_KIBANA_REPORT_INSTANCE_ID, TENANT, FILENAME);

    ReportEntity reportEntity = getFirstReport(TENANT);

    assertThat(reportEntity.getTenant()).isEqualTo(TENANT);
    assertThat(reportEntity.getReportId()).isNotNull();
    assertThat(reportEntity.getFilename()).isEqualTo(FILENAME);
    assertThat(reportEntity.getKibanaReportInstanceId())
        .isEqualTo(EXISTING_KIBANA_REPORT_INSTANCE_ID);
  }

  @Test
  void shouldFilterOutExistingReports() {
    ReportEntity saved = inMemoryReportRepository.save(REPORT);
    Set<String> allReports = of(saved.getKibanaReportInstanceId(), NEW_KIBANA_REPORT_INSTANCE_ID);

    Set<String> newReports = underTest.filterNew(allReports);

    assertThat(newReports).containsExactlyInAnyOrder(NEW_KIBANA_REPORT_INSTANCE_ID);
  }

  @Test
  void shouldReturnReportsByTenant() {
    inMemoryReportRepository.save(REPORT);

    Set<ReportDto> reports = underTest.getAllReportsForTenant(TENANT);
    ReportDto[] reportDtos = reports.toArray(ReportDto[]::new);

    assertThat(reportDtos).hasSize(1);
    assertThat(reportDtos[0].getKibanaReportInstanceId()).isEqualTo(KIBANA_REPORT_INSTANCE_ID);
    assertThat(reportDtos[0].getReportId()).isEqualTo(REPORT_ID);
  }

  private ReportEntity getFirstReport(String tenant) {
    Set<ReportEntity> reports = inMemoryReportRepository.findByTenant(tenant);
    return reports.toArray(ReportEntity[]::new)[0];
  }
}
