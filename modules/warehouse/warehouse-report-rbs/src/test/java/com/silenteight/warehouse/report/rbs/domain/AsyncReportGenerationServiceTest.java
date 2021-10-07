package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.DAY;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncReportGenerationServiceTest {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final ReportDefinition TYPE = DAY;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());
  private static final CsvReportContentDto REPORT_CONTENT = CsvReportContentDto
      .of("test", of("lines"));
  private static final List<String> INDEXES = of("index123");

  private final InMemoryRbsRepository rbsReportRepository = new InMemoryRbsRepository();
  @Mock
  private RbsReportGenerationService reportGenerationService;
  @Mock
  private RbsReportDefinition properties;
  @Mock
  private IndexesQuery indexQuery;

  private AsyncRbsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncRbsReportGenerationService(
        rbsReportRepository,
        reportGenerationService,
        TIME_SOURCE,
        properties,
        properties,
        indexQuery,
        indexQuery);
  }

  @Test
  void generateReportAndReportAvailable() {
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, properties))
        .thenReturn(REPORT_CONTENT);
    when(indexQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME)).thenReturn(INDEXES);
    RbsReport rbsReport = rbsReportRepository.save(RbsReport.of(TYPE));
    assertThat(rbsReport.getState()).isEqualTo(ReportState.NEW);

    underTest.generateReport(rbsReport.getId());

    rbsReport = rbsReportRepository.getById(rbsReport.getId());
    assertThat(rbsReport.getState()).isEqualTo(ReportState.DONE);
    assertThat(rbsReport.getFile()).isEqualTo(REPORT_CONTENT.getReport());
  }

  @Test
  void shouldFailReport() {
    //given
    RbsReport rbsReport = rbsReportRepository.save(RbsReport.of(DAY));

    //when
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, properties))
        .thenThrow(RuntimeException.class);

    //then
    Long reportId = rbsReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId))
        .isInstanceOf(RuntimeException.class);

    rbsReport = rbsReportRepository.getById(reportId);
    assertThat(rbsReport.getState()).isEqualTo(ReportState.FAILED);
  }
}
