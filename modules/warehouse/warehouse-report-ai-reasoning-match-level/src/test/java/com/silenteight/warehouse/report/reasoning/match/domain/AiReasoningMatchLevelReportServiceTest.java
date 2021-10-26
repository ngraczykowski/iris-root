package com.silenteight.warehouse.report.reasoning.match.domain;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportDefinition.MONTH;
import static com.silenteight.warehouse.report.reasoning.match.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.match.generation.GenerationAiReasoningMatchLevelReportTestFixtures.PROPERTIES;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AiReasoningMatchLevelReportServiceTest {

  private static final AiReasoningMatchLevelReportDefinition TYPE = MONTH;

  private final InMemoryMatchLevelAiReasoningRepository
      repository = new InMemoryMatchLevelAiReasoningRepository();

  @Mock
  private AsyncAiReasoningMatchLevelReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private AiReasoningMatchLevelReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AiReasoningMatchLevelReportService(repository, asyncReportGenerationService,
        reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    var reportInstance =
        underTest.createReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME, of(), PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getInstanceReferenceId();
    Optional<AiReasoningMatchLevelReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(AiReasoningMatchLevelReport::getState)
        .isEqualTo(NEW);

    assertThat(report)
        .isPresent()
        .get()
        .extracting(AiReasoningMatchLevelReport::getFileStorageName)
        .isEqualTo(instanceReferenceId + "-" + MONTH.getFilename());
  }
}