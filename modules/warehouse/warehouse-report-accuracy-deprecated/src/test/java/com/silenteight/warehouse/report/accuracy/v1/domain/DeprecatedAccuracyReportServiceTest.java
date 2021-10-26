package com.silenteight.warehouse.report.accuracy.v1.domain;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.accuracy.v1.DeprecatedAccuracyReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedAccuracyReportDefinition.DAY;
import static com.silenteight.warehouse.report.accuracy.v1.generation.DeprecatedGenerationAccuracyReportTestFixtures.PROPERTIES;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedAccuracyReportServiceTest {

  private static final DeprecatedAccuracyReportDefinition TYPE = DAY;

  private final DeprecatedInMemoryAccuracyRepository
      repository = new DeprecatedInMemoryAccuracyRepository();

  @Mock
  private DeprecatedAsyncAccuracyReportGenerationService asyncReportGenerationService;
  @Mock
  private ReportStorage reportStorage;
  private DeprecatedAccuracyReportService underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeprecatedAccuracyReportService(
        repository, asyncReportGenerationService, reportStorage);
  }

  @Test
  void shouldGenerateReport() {
    // when
    ReportInstanceReferenceDto reportInstance =
        underTest.createReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME, of(), PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getInstanceReferenceId();
    Optional<DeprecatedAccuracyReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(DeprecatedAccuracyReport::getState)
        .isEqualTo(DeprecatedReportState.NEW);

    assertThat(report)
        .isPresent()
        .get()
        .extracting(DeprecatedAccuracyReport::getFileName)
        .isEqualTo(instanceReferenceId + "-" + DAY.getFilename());
  }
}
