package com.silenteight.warehouse.report.reasoning.domain;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.NEW;
import static com.silenteight.warehouse.report.reasoning.generation.GenerationAiReasoningReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AiReasoningReportServiceTest {

  private final InMemoryAiReasoningRepository repository = new InMemoryAiReasoningRepository();

  @Mock
  private AsyncAiReasoningReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private AiReasoningReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AiReasoningReportService(
        repository, asyncReportGenerationService, reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    var reportInstance = underTest.createReportInstance(
        REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getInstanceReferenceId();
    Optional<AiReasoningReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(AiReasoningReport::getState)
        .isEqualTo(NEW);
  }
}