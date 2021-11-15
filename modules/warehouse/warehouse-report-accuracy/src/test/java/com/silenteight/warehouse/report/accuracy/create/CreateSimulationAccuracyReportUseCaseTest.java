package com.silenteight.warehouse.report.accuracy.create;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.domain.AccuracyReportService;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.ANALYSIS_ID;
import static java.time.LocalDate.EPOCH;
import static java.time.LocalDate.from;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSimulationAccuracyReportUseCaseTest {

  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;

  @Mock
  private IndexesQuery simulationIndexerQuery;
  @Mock
  private AccuracyReportDefinitionProperties simulationProperties;
  @Mock
  private AccuracyReportService reportService;
  @Captor
  private ArgumentCaptor<ReportRange> reportRangeCaptor;

  private CreateSimulationAccuracyReportUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new CreateSimulationAccuracyReportUseCase(
        reportService,
        simulationProperties,
        simulationIndexerQuery,
        TIME_SOURCE);
  }

  @Test
  void shouldCallCreateReportMethodWithProperArguments() {
    // when
    underTest.createReport(ANALYSIS_ID);

    // then
    verify(reportService).createReportInstance(reportRangeCaptor.capture(), any(), any());
    assertThat(reportRangeCaptor.getValue().getFromAsLocalDate()).isEqualTo(from(EPOCH));
    assertThat(reportRangeCaptor.getValue().getToAsLocalDate()).isEqualTo(
        TIME_SOURCE.localDateTime().toLocalDate());
  }
}
