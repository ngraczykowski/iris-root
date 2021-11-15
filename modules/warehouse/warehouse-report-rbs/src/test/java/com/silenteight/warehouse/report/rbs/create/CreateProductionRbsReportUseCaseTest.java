package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.LOCAL_DATE_FROM;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.LOCAL_DATE_TO;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.OFFSET_DATE_TIME_FROM;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.OFFSET_DATE_TIME_TO;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductionRbsReportUseCaseTest {

  private static final String FILE_NAME = "RB_Scorer_%s_To_%s.csv";

  @Mock
  private RbsReportService reportService;
  @Mock
  private RbsReportDefinition productionProperties;
  @Captor
  private ArgumentCaptor<ReportRange> reportRangeCaptor;
  @Captor
  private ArgumentCaptor<String> fileNameCaptor;
  @InjectMocks
  private CreateProductionRbsReportUseCase underTest;

  @Test
  void shouldCallCreateReportMethodWithProperArguments() {
    // when
    underTest.createReport(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO);

    // then
    verify(reportService).createReportInstance(
        reportRangeCaptor.capture(), fileNameCaptor.capture(), any(), any());

    assertThat(fileNameCaptor.getValue()).isEqualTo(
        format(FILE_NAME, LOCAL_DATE_FROM, LOCAL_DATE_TO));

    assertThat(reportRangeCaptor.getValue().getFrom()).isEqualTo(OFFSET_DATE_TIME_FROM);
    assertThat(reportRangeCaptor.getValue().getTo()).isEqualTo(OFFSET_DATE_TIME_TO);
  }
}