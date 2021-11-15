package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.ANALYSIS_ID;
import static java.time.LocalDate.EPOCH;
import static java.time.LocalDate.from;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSimulationRbsReportUseCaseTest {

  private static final String FILE_NAME = "simulation-rbscorer.csv";
  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;

  @Mock
  private IndexesQuery simulationIndexerQuery;
  @Mock
  private RbsReportDefinition simulationProperties;
  @Mock
  private RbsReportService reportService;
  @Captor
  private ArgumentCaptor<ReportRange> reportRangeCaptor;
  @Captor
  private ArgumentCaptor<String> fileNameCaptor;

  private CreateSimulationRbsReportUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new CreateSimulationRbsReportUseCase(
        reportService,
        simulationIndexerQuery,
        simulationProperties,
        TIME_SOURCE);
  }

  @Test
  void shouldCallCreateReportMethodWithProperArguments() {
    // when
    underTest.createReport(ANALYSIS_ID);

    // then
    verify(reportService).createReportInstance(
        reportRangeCaptor.capture(), fileNameCaptor.capture(), any(), any());

    assertThat(fileNameCaptor.getValue()).isEqualTo(FILE_NAME);
    assertThat(reportRangeCaptor.getValue().getFromAsLocalDate()).isEqualTo(from(EPOCH));
    assertThat(reportRangeCaptor.getValue().getToAsLocalDate()).isEqualTo(
        TIME_SOURCE.localDateTime().toLocalDate());
  }
}
