package com.silenteight.warehouse.report.reasoning.match.domain;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.reasoning.match.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.match.generation.GenerationAiReasoningMatchLevelReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AiReasoningMatchLevelReportServiceTest {

  private final InMemoryAiReasoningMatchLevelRepository repository =
      new InMemoryAiReasoningMatchLevelRepository();

  @Mock
  private AsyncAiReasoningMatchLevelReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private AiReasoningMatchLevelReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AiReasoningMatchLevelReportService(
        repository, asyncReportGenerationService, reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    var reportInstance = underTest.createReportInstance(
        REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getInstanceReferenceId();
    Optional<AiReasoningMatchLevelReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(AiReasoningMatchLevelReport::getState)
        .isEqualTo(NEW);
  }
}