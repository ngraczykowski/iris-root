package com.silenteight.warehouse.simulation.processing.mapping;

import lombok.SneakyThrows;

import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertDefinition;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.SIMULATION_ANALYSIS_NAME;
import static com.silenteight.warehouse.simulation.SimulationAlertFixtures.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class SimulationAlertMappingServiceTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final PayloadConverter payloadConverter = new PayloadConverter(objectMapper);
  private final SimulationAlertV1MappingService underTest =
      new SimulationAlertV1MappingService(payloadConverter);

  @Test
  void shouldReturnTheSameAmountOfAlerts() {
    List<SimulationAlertDefinition> simulationAlertDefinitions =
        underTest.mapFields(of(ALERT_SIM_1, ALERT_SIM_1), SIMULATION_ANALYSIS_NAME);

    assertThat(simulationAlertDefinitions.size()).isEqualTo(2);
  }

  @Test
  void shouldFilterOutBrokenAlerts() {
    List<SimulationAlertDefinition> simulationAlertDefinitions =
        underTest.mapFields(of(ALERT_EMPTY, ALERT_SIM_1), SIMULATION_ANALYSIS_NAME);

    assertThat(simulationAlertDefinitions.size()).isEqualTo(1);
  }

  @Test
  void shouldMapAlertIntoAlertDefinition() {
    SimulationAlertDefinition simulationAlertDefinition =
        underTest.mapFields(ALERT_SIM_1, SIMULATION_ANALYSIS_NAME);

    assertThat(simulationAlertDefinition.getAlertName()).isEqualTo(Values.ALERT_NAME);
    assertThat(simulationAlertDefinition.getAnalysisName()).isEqualTo(SIMULATION_ANALYSIS_NAME);
    assertThat(simulationAlertDefinition.getPayload()).isNotNull();
  }

  @Test
  void shouldHandleMultipleValuesInPayload() {
    SimulationAlertDefinition simulationAlertDefinition =
        underTest.mapFields(ALERT_SIM_1, SIMULATION_ANALYSIS_NAME);

    assertThat(simulationAlertDefinition.getPayload()).isNotNull();

    Map<String, String> parsedPayload = parsePayload(simulationAlertDefinition.getPayload());
    assertThat(parsedPayload.get(COUNTRY_KEY)).isEqualTo(COUNTRY_VALUE);
    assertThat(parsedPayload.get(RECOMMENDATION_KEY)).isEqualTo(RECOMMENDATION_VALUE);
  }

  @Test
  void shouldHandleNestedPayload() {
    SimulationAlertDefinition simulationAlertDefinition =
        underTest.mapFields(ALERT_NESTED_PAYLOAD, SIMULATION_ANALYSIS_NAME);

    assertThat(simulationAlertDefinition.getPayload()).isNotNull();

    Map<String, String> parsedPayload = parsePayload(simulationAlertDefinition.getPayload());
    assertThat(parsedPayload.get(LEVEL_1_KEY + "_" + LEVEL_2_KEY)).isEqualTo(VALUE);
  }

  @SneakyThrows
  Map<String, String> parsePayload(String payload) {
    TypeReference<Map<String, String>> typeRef = new TypeReference<>() {};
    return objectMapper.readValue(payload.getBytes(), typeRef);
  }
}
