package com.silenteight.warehouse.report.reasoning.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.reasoning.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.generation.AiReasoningReportGenerationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportDefinition.MONTH;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.generation.GenerationAiReasoningReportTestFixtures.PROPERTIES;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncAiReasoningReportGenerationServiceTest {

  private static final MockTimeSource TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  private static final AiReasoningReportDefinition TYPE = MONTH;
  private static final OffsetDateTime FROM = TYPE.getFrom(TIME_SOURCE.now());
  private static final OffsetDateTime TO = TYPE.getTo(TIME_SOURCE.now());

  private final InMemoryAiReasoningRepository repository = new InMemoryAiReasoningRepository();

  @Mock
  private AiReasoningReportGenerationService reportGenerationService;
  private AsyncAiReasoningReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AsyncAiReasoningReportGenerationService(
        repository,
        reportGenerationService,
        TIME_SOURCE);
  }

  @Test
  void generateReportAndReportAvailable() {
    // given
    AiReasoningReport aiReasoningReport =
        repository.save(AiReasoningReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    assertThat(aiReasoningReport.getState()).isEqualTo(NEW);

    // when
    Long id = aiReasoningReport.getId();
    underTest.generateReport(id, INDEXES, PROPERTIES);

    // then
    aiReasoningReport = repository.getById(id);
    assertThat(aiReasoningReport.getState()).isEqualTo(DONE);
    assertThat(aiReasoningReport.getFile()).isEqualTo(id + "-" + TYPE.getFilename());
  }

  @Test
  void shouldFailReport() {
    // given
    AiReasoningReport aiReasoningReport =
        repository.save(AiReasoningReport.of(TYPE, PRODUCTION_ANALYSIS_NAME));
    doThrow(RuntimeException.class).when(
        reportGenerationService).generateReport(FROM, TO, INDEXES, PROPERTIES, TYPE.getFilename());

    // when + then
    Long reportId = aiReasoningReport.getId();
    assertThatThrownBy(() -> underTest.generateReport(reportId, INDEXES, PROPERTIES))
        .isInstanceOf(ReportGenerationException.class)
        .hasMessageContaining(format("Cannot generate AI Reasoning report with id=%d", reportId));

    aiReasoningReport = repository.getById(aiReasoningReport.getId());
    assertThat(aiReasoningReport.getFile()).isNull();
    assertThat(aiReasoningReport.getState()).isEqualTo(FAILED);
  }
}
