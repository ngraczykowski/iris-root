package com.silenteight.warehouse.report.metrics.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.metrics.generation.PropertiesDefinition;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.LOCAL_DATE_FROM;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.LOCAL_DATE_TO;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.OFFSET_DATE_TIME_FROM;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.OFFSET_DATE_TIME_TO;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductionMetricsReportUseCaseTest {

  private static final String FILE_NAME = "Production_Metrics_%s_To_%s.csv";

  @Mock
  private MetricsReportService reportService;
  @Mock
  private PropertiesDefinition productionProperties;
  @Mock
  private IndexesQuery productionIndexerQuery;
  @Captor
  private ArgumentCaptor<ReportRange> reportRangeCaptor;
  @Captor
  private ArgumentCaptor<String> fileNameCaptor;
  @InjectMocks
  private CreateProductionMetricsReportUseCase underTest;

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
