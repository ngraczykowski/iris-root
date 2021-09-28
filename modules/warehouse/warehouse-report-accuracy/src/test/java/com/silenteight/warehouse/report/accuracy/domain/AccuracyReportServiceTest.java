package com.silenteight.warehouse.report.accuracy.domain;


import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReportDefinition.DAY;
import static com.silenteight.warehouse.report.accuracy.generation.GenerationAccuracyReportTestFixtures.PROPERTIES;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccuracyReportServiceTest {

  private static final AccuracyReportDefinition TYPE = DAY;

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
    ReportInstanceReferenceDto reportInstance =
        underTest.createReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME, of(), PROPERTIES);

    // then
    long instanceReferenceId = reportInstance.getGetInstanceReferenceId();
    Optional<AccuracyReport> report = repository.findById(instanceReferenceId);
    assertThat(report)
        .isPresent()
        .get()
        .extracting(AccuracyReport::getState)
        .isEqualTo(ReportState.NEW);

    assertThat(report)
        .isPresent()
        .get()
        .extracting(AccuracyReport::getFileName)
        .isEqualTo(instanceReferenceId + DAY.getFilename());
  }
}