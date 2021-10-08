package com.silenteight.warehouse.report.reasoning.domain;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportDefinition.MONTH;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.generation.GenerationAiReasoningReportTestFixtures.PROPERTIES;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AiReasoningReportServiceTest {

  private static final AiReasoningReportDefinition TYPE = MONTH;

  private final InMemoryAiReasoningRepository repository = new InMemoryAiReasoningRepository();

  @Mock
  private AsyncAiReasoningReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private AiReasoningReportService underTest;

  @BeforeEach
  void setUp() {
    underTest =
        new AiReasoningReportService(repository, asyncReportGenerationService, reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    var reportInstance =
        underTest.createReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME, of(), PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getGetInstanceReferenceId();
    Optional<AiReasoningReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(AiReasoningReport::getState)
        .isEqualTo(NEW);

    assertThat(report)
        .isPresent()
        .get()
        .extracting(AiReasoningReport::getFileName)
        .isEqualTo(instanceReferenceId + "-" + MONTH.getFilename());
  }
}