package com.silenteight.warehouse.report.reasoning.match.domain;

import com.silenteight.warehouse.report.reasoning.match.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningMatchLevelReportGenerationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures.PRODUCTION_REPORT_FILENAME;
import static com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReport.of;
import static com.silenteight.warehouse.report.reasoning.match.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.match.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.reasoning.match.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.match.generation.GenerationAiReasoningMatchLevelReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncAiReasoningMatchLevelReportGenerationServiceTest {

  private final InMemoryAiReasoningMatchLevelRepository repository =
      new InMemoryAiReasoningMatchLevelRepository();

  @Mock
  private AiReasoningMatchLevelReportGenerationService reportGenerationService;
  private AsyncAiReasoningMatchLevelReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncAiReasoningMatchLevelReportGenerationService(
        repository,
        reportGenerationService);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    AiReasoningMatchLevelReport aiReasoningMatchLevelReport = repository.save(of(REPORT_RANGE));
    assertThat(aiReasoningMatchLevelReport.getState()).isEqualTo(NEW);

    // when
    Long id = aiReasoningMatchLevelReport.getId();
    underTest.generateReport(id, REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    aiReasoningMatchLevelReport = repository.getById(id);
    assertThat(aiReasoningMatchLevelReport.getState()).isEqualTo(DONE);
    assertThat(aiReasoningMatchLevelReport.getFileStorageName()).isNotBlank();
  }

  @Test
  void shouldFailReport() {
    // given
    AiReasoningMatchLevelReport aiReasoningMatchLevelReport = repository.save(of(REPORT_RANGE));
    doThrow(RuntimeException.class)
        .when(reportGenerationService)
        .generateReport(
            REPORT_RANGE.getFrom(),
            REPORT_RANGE.getTo(),
            INDEXES,
            PROPERTIES,
            PRODUCTION_REPORT_FILENAME);

    // when + then
    Long id = aiReasoningMatchLevelReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(id, REPORT_RANGE, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(
            format("Cannot generate AI Reasoning Match Level report with id=%d", id));

    aiReasoningMatchLevelReport = repository.getById(aiReasoningMatchLevelReport.getId());
    assertThat(aiReasoningMatchLevelReport.getState()).isEqualTo(FAILED);
  }
}
