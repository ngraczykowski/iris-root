package com.silenteight.simulator.management.timeout;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.create.CreateSimulationRequest;
import com.silenteight.simulator.management.details.SimulationDetailsQuery;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.management.domain.SimulationTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.silenteight.simulator.management.SimulationFixtures.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { SimulationTestConfiguration.class })
class AnalysisTimeoutValidatorTest extends BaseDataJpaTest {

  @Autowired
  AnalysisTimeoutValidator underTest;

  @Autowired
  SimulationService simulationService;

  @Autowired
  SimulationDetailsQuery simulationDetailsQuery;

  @MockBean
  AnalysisService analysisService;

  @Test
  void shouldReturnTrueWhenNumberOfSolvedAlertsDidntChange() {
    String analysisName = SIMULATION_DTO_2.getAnalysis();
    given(analysisService.getAnalysis(eq(analysisName)))
        .willReturn(MAP_OF_ANALYSIS_WITH_NAMES.get(analysisName));
    // when
    boolean valid = underTest.valid(SIMULATION_DTO_2);

    // then
    assertThat(valid).isTrue();
  }

  @Test
  void shouldReturnFalseWhenAllAlertsAreSolved() {
    // given
    String analysisName = SIMULATION_DTO_3.getAnalysis();
    persistSimulation(ID, analysisName);
    given(analysisService.getAnalysis(eq(analysisName)))
        .willReturn(MAP_OF_ANALYSIS_WITH_NAMES.get(analysisName));

    // when
    boolean valid = underTest.valid(SIMULATION_DTO_3);

    // then
    assertThat(valid).isFalse();
  }

  @Test
  void shouldReturnFalseAndUpdatedNumberOfSolvedAlertsWhenSolvingInProgress() {
    //given
    String analysisName = SIMULATION_DTO.getAnalysis();
    persistSimulation(ID, analysisName);
    given(analysisService.getAnalysis(eq(analysisName)))
        .willReturn(MAP_OF_ANALYSIS_WITH_NAMES.get(analysisName));

    //when
    boolean valid = underTest.valid(SIMULATION_DTO);

    //then
    assertThat(valid).isFalse();
    long expectedSolvedAlerts = 85;
    SimulationDetailsDto simulationDetailsDto = simulationDetailsQuery.get(ANALYSIS_NAME_1);
    assertThat(simulationDetailsDto.getSolvedAlerts()).isEqualTo(expectedSolvedAlerts);
  }

  private void persistSimulation(UUID simulationID, String analysisName) {
    CreateSimulationRequest request = CreateSimulationRequest.builder()
        .id(simulationID)
        .correlationId(randomUUID())
        .createdBy(USERNAME)
        .simulationName(SIMULATION_NAME)
        .description(DESCRIPTION)
        .datasets(DATASETS)
        .model(MODEL_NAME)
        .build();

    simulationService.createSimulation(request, DATASETS, analysisName);
  }
}
