package com.silenteight.warehouse.report.reasoning.v1.domain;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.reasoning.v1.DeprecatedAiReasoningReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedAiReasoningReportDefinition.MONTH;
import static com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.v1.generation.DeprecatedGenerationAiReasoningReportTestFixtures.PROPERTIES;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAiReasoningReportServiceTest {

  private static final DeprecatedAiReasoningReportDefinition TYPE = MONTH;

  private final DeprecatedInMemoryAiReasoningRepository
      repository = new DeprecatedInMemoryAiReasoningRepository();

  @Mock
  private DeprecatedAsyncAiReasoningReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private DeprecatedAiReasoningReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedAiReasoningReportService(
            repository, asyncReportGenerationService, reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    var reportInstance =
        underTest.createReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME, of(), PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getInstanceReferenceId();
    Optional<DeprecatedAiReasoningReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(DeprecatedAiReasoningReport::getState)
        .isEqualTo(NEW);

    assertThat(report)
        .isPresent()
        .get()
        .extracting(DeprecatedAiReasoningReport::getFileStorageName)
        .isEqualTo(instanceReferenceId + "-" + MONTH.getFilename());
  }
}