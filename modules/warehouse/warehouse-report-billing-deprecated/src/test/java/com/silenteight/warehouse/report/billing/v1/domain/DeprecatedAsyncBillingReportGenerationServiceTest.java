package com.silenteight.warehouse.report.billing.v1.domain;

import com.silenteight.warehouse.report.billing.v1.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.billing.v1.generation.DeprecatedBillingReportGenerationService;
import com.silenteight.warehouse.report.billing.v1.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedBillingReportService.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportDefinition.THIS_YEAR;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportState.DONE;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportState.FAILED;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportState.NEW;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAsyncBillingReportGenerationServiceTest {

  private static final DeprecatedReportDefinition TYPE = THIS_YEAR;
  private static final OffsetDateTime FROM = TYPE.getFrom();
  private static final OffsetDateTime TO = TYPE.getTo();
  private static final CsvReportContentDto REPORT_CONTENT = new CsvReportContentDto(
      "test", of("lines"), "checksum");

  private final DeprecatedInMemoryBillingRepository
      billingReportRepository = new DeprecatedInMemoryBillingRepository();
  @Mock
  private DeprecatedBillingReportGenerationService reportGenerationService;
  @Mock
  private DeprecatedBillingReportAsyncGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedBillingReportAsyncGenerationService(
        billingReportRepository, reportGenerationService);
  }

  @Test
  void generateReportAndReportAvailable() {
    when(reportGenerationService.generateReport(FROM, TO, PRODUCTION_ANALYSIS_NAME))
        .thenReturn(REPORT_CONTENT);
    DeprecatedBillingReport billingReport =
        billingReportRepository.save(DeprecatedBillingReport.of(TYPE));

    assertThat(billingReport.getState()).isEqualTo(NEW);

    underTest.generateReport(billingReport.getId());

    billingReport = billingReportRepository.getById(billingReport.getId());
    assertThat(billingReport.getState()).isEqualTo(DONE);
    assertThat(billingReport.getFile()).isEqualTo(REPORT_CONTENT.getReport());
  }

  @Test
  void shouldFailReport() {
    //given
    DeprecatedBillingReport billingReport =
        billingReportRepository.save(DeprecatedBillingReport.of(TYPE));

    //when
    when(reportGenerationService.generateReport(FROM, TO, PRODUCTION_ANALYSIS_NAME))
        .thenThrow(RuntimeException.class);

    //then
    Long reportId = billingReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId)).isInstanceOf(
        ReportGenerationException.class);

    billingReport = billingReportRepository.getById(reportId);
    assertThat(billingReport.getState()).isEqualTo(FAILED);
  }
}
