package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedAiReasoningMatchLevelReportDefinition.MONTH;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.match.v1.generation.DeprecatedGenerationAiReasoningMatchLevelReportTestFixtures.PROPERTIES;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAiReasoningMatchLevelReportServiceTest {

  private static final DeprecatedAiReasoningMatchLevelReportDefinition TYPE = MONTH;

  private final DeprecatedInMemoryMatchLevelAiReasoningRepository
      repository = new DeprecatedInMemoryMatchLevelAiReasoningRepository();

  @Mock
  private DeprecatedAsyncAiReasoningMatchLevelReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private DeprecatedAiReasoningMatchLevelReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedAiReasoningMatchLevelReportService(
        repository,
        asyncReportGenerationService,
        reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    var reportInstance =
        underTest.createReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME, of(), PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getInstanceReferenceId();
    Optional<DeprecatedAiReasoningMatchLevelReport> report =
        repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(DeprecatedAiReasoningMatchLevelReport::getState)
        .isEqualTo(NEW);

    assertThat(report)
        .isPresent()
        .get()
        .extracting(DeprecatedAiReasoningMatchLevelReport::getFileStorageName)
        .isEqualTo(instanceReferenceId + "-" + MONTH.getFilename());
  }
}