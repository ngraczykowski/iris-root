package com.silenteight.warehouse.report.reasoning.domain;

import com.silenteight.warehouse.report.reasoning.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.generation.AiReasoningReportGenerationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.PRODUCTION_REPORT_FILENAME;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.reasoning.domain.AiReasoningReport.of;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.generation.GenerationAiReasoningReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncAiReasoningReportGenerationServiceTest {

  private final InMemoryAiReasoningRepository repository = new InMemoryAiReasoningRepository();

  @Mock
  private AiReasoningReportGenerationService reportGenerationService;
  private AsyncAiReasoningReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncAiReasoningReportGenerationService(repository, reportGenerationService);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    AiReasoningReport aiReasoningReport = repository.save(of(REPORT_RANGE));
    assertThat(aiReasoningReport.getState()).isEqualTo(NEW);

    // when
    Long id = aiReasoningReport.getId();
    underTest.generateReport(id, REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    aiReasoningReport = repository.getById(id);
    assertThat(aiReasoningReport.getState()).isEqualTo(DONE);
    assertThat(aiReasoningReport.getFileStorageName()).isNotBlank();
  }

  @Test
  void shouldFailReport() {
    // given
    AiReasoningReport aiReasoningReport = repository.save(of(REPORT_RANGE));
    doThrow(RuntimeException.class)
        .when(reportGenerationService)
        .generateReport(
            REPORT_RANGE.getFrom(), REPORT_RANGE.getTo(), INDEXES, PROPERTIES,
            PRODUCTION_REPORT_FILENAME);

    // when + then
    Long id = aiReasoningReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(id, REPORT_RANGE, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate AI Reasoning report with id=%d", id));

    aiReasoningReport = repository.getById(aiReasoningReport.getId());
    assertThat(aiReasoningReport.getState()).isEqualTo(FAILED);
  }
}
