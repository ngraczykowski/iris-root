package com.silenteight.warehouse.report.simulation.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationTestConstants.REPORT_DEFINITION_DTO;
import static com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationTestConstants.REPORT_TYPE;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeprecatedSimulationReportsDefinitionsUseCaseTest {

  private static final String ANALYSIS_ID = "1513fff6-02ca-45fa-9efe-2165822823ec";

  @Mock
  private DeprecatedSimulationReportsProvider accuracyProvider;

  private DeprecatedSimulationReportsDefinitionsUseCase underTest;

  @BeforeEach
  void setUp() {
    when(accuracyProvider.getReportDefinitions(ANALYSIS_ID)).thenReturn(of(REPORT_DEFINITION_DTO));
  }

  @Test
  void activateShouldReturnAllAvailableReportsDefinitions() {
    underTest = new DeprecatedSimulationReportsDefinitionsUseCase(of(accuracyProvider), of());
    assertThat(underTest.activate(ANALYSIS_ID)).containsExactly(REPORT_DEFINITION_DTO);
  }

  @Test
  void activateShouldReturnEmptyList() {
    underTest = new DeprecatedSimulationReportsDefinitionsUseCase(of(accuracyProvider),
        of(REPORT_TYPE));

    assertThat(underTest.activate(ANALYSIS_ID)).isEmpty();
  }
}
