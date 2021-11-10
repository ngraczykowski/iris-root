package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.reasoning.match.v1.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.match.v1.generation.DeprecatedAiReasoningMatchLevelReportGenerationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedAiReasoningMatchLevelReportDefinition.MONTH;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.FAILED;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.match.v1.generation.DeprecatedGenerationAiReasoningMatchLevelReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAsyncAiReasoningMatchLevelReportGenerationServiceTest {

  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final DeprecatedAiReasoningMatchLevelReportDefinition TYPE = MONTH;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());

  private final DeprecatedInMemoryMatchLevelAiReasoningRepository
      repository = new DeprecatedInMemoryMatchLevelAiReasoningRepository();

  @Mock
  private DeprecatedAiReasoningMatchLevelReportGenerationService reportGenerationService;
  private DeprecatedAsyncAiReasoningMatchLevelReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedAsyncAiReasoningMatchLevelReportGenerationService(
        repository,
        reportGenerationService,
        TIME_SOURCE);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    DeprecatedAiReasoningMatchLevelReport aiReasoningReport =
        repository.save(DeprecatedAiReasoningMatchLevelReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    assertThat(aiReasoningReport.getState()).isEqualTo(NEW);

    // when
    Long id = aiReasoningReport.getId();
    underTest.generateReport(id, INDEXES, PROPERTIES);

    // then
    aiReasoningReport = repository.getById(id);
    assertThat(aiReasoningReport.getState()).isEqualTo(DONE);
    assertThat(aiReasoningReport.getFileStorageName()).isEqualTo(id + "-" + TYPE.getFilename());
  }

  @Test
  void shouldFailReport() {
    // given
    DeprecatedAiReasoningMatchLevelReport aiReasoningReport =
        repository.save(DeprecatedAiReasoningMatchLevelReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    doThrow(RuntimeException.class).when(
        reportGenerationService).generateReport(FROM, TO, INDEXES, PROPERTIES, TYPE.getFilename());

    // when + then
    Long reportId = aiReasoningReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format(
            "Cannot generate AI Reasoning Match Level report with id=%d",
            reportId));

    aiReasoningReport = repository.getById(aiReasoningReport.getId());
    assertThat(aiReasoningReport.getState()).isEqualTo(FAILED);
  }
}
