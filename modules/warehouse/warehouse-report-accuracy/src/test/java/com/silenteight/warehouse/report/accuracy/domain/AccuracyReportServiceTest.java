package com.silenteight.warehouse.report.accuracy.domain;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.INDEXES;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.accuracy.generation.GenerationAccuracyReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccuracyReportServiceTest {

  private final InMemoryAccuracyRepository repository = new InMemoryAccuracyRepository();

  @Mock
  private AsyncAccuracyReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private AccuracyReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new AccuracyReportService(repository, asyncReportGenerationService, reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    var reportInstance = underTest.createReportInstance(
        REPORT_RANGE, INDEXES, PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getInstanceReferenceId();
    Optional<AccuracyReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(AccuracyReport::getState)
        .isEqualTo(ReportState.NEW);

    assertThat(report)
        .isPresent()
        .get()
        .extracting(AccuracyReport::getFileStorageName)
        .isNotNull();
  }
}
