package com.silenteight.warehouse.report.rbs.v1.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.v1.generation.DeprecatedRbsReportDefinition;
import com.silenteight.warehouse.report.rbs.v1.generation.DeprecatedRbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.v1.generation.dto.CsvReportContentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedReportDefinition.DAY;
import static com.silenteight.warehouse.report.rbs.v1.generation.DeprecatedRbScorerFixtures.TEST_INDEX;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAsyncReportGenerationServiceTest {

  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final DeprecatedReportDefinition TYPE = DAY;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());
  private static final CsvReportContentDto REPORT_CONTENT = CsvReportContentDto
      .of("test", of("lines"));
  private static final List<String> INDEXES = of(TEST_INDEX);

  private final DeprecatedInMemoryRbsRepository
      rbsReportRepository = new DeprecatedInMemoryRbsRepository();
  @Mock
  private DeprecatedRbsReportGenerationService reportGenerationService;
  @Mock
  private DeprecatedRbsReportDefinition properties;
  @Mock
  private IndexesQuery indexQuery;

  private DeprecatedAsyncRbsReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedAsyncRbsReportGenerationService(
        rbsReportRepository,
        reportGenerationService,
        TIME_SOURCE,
        properties,
        properties,
        indexQuery);
  }

  @Test
  void generateReportAndReportAvailable() {
    when(properties.getIndexName()).thenReturn(TEST_INDEX);
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, properties))
        .thenReturn(REPORT_CONTENT);
    DeprecatedRbsReport rbsReport = rbsReportRepository.save(DeprecatedRbsReport.of(TYPE));
    assertThat(rbsReport.getState()).isEqualTo(DeprecatedReportState.NEW);

    underTest.generateReport(rbsReport.getId());

    rbsReport = rbsReportRepository.getById(rbsReport.getId());
    assertThat(rbsReport.getState()).isEqualTo(DeprecatedReportState.DONE);
    assertThat(rbsReport.getFile()).isEqualTo(REPORT_CONTENT.getReport());
  }

  @Test
  void shouldFailReport() {
    //given
    DeprecatedRbsReport rbsReport = rbsReportRepository.save(DeprecatedRbsReport.of(DAY));

    //when
    when(reportGenerationService.generateReport(FROM, TO, INDEXES, properties))
        .thenThrow(RuntimeException.class);

    //then
    Long reportId = rbsReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId))
        .isInstanceOf(RuntimeException.class);

    rbsReport = rbsReportRepository.getById(reportId);
    assertThat(rbsReport.getState()).isEqualTo(DeprecatedReportState.FAILED);
  }
}
