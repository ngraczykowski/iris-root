package com.silenteight.warehouse.report.accuracy.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.domain.AccuracyReportService;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.OFFSET_DATE_TIME_FROM;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.OFFSET_DATE_TIME_TO;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductionAccuracyReportUseCaseTest {

  @Mock
  private IndexesQuery productionIndexerQuery;
  @Mock
  private AccuracyReportDefinitionProperties simulationProperties;
  @Mock
  private AccuracyReportService reportService;
  @Captor
  private ArgumentCaptor<ReportRange> reportRangeCaptor;

  @InjectMocks
  private CreateProductionAccuracyReportUseCase underTest;

  @Test
  void shouldCallCreateReportMethodWithProperArguments() {
    // when
    underTest.createReport(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO);

    // then
    verify(reportService).createReportInstance(reportRangeCaptor.capture(), any(), any());
    assertThat(reportRangeCaptor.getValue().getFrom()).isEqualTo(OFFSET_DATE_TIME_FROM);
    assertThat(reportRangeCaptor.getValue().getTo()).isEqualTo(OFFSET_DATE_TIME_TO);
  }
}
