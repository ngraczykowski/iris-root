package com.silenteight.simulator.processing.alert.index.ack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.SimulationState;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.simulator.management.domain.SimulationState.PENDING;
import static java.time.ZoneOffset.UTC;
import static java.util.Set.of;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AckMessageFixtures {

  static final String REQUEST_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final String ANALYSIS_NAME = "Analysis name";
  static final UUID SIMULATION_ID = fromString("a9b45451-6fde-4832-8dc0-d17b4708d8ca");
  static final String SIMULATION_NAME = "simulations/" + SIMULATION_ID;
  static final String SIMULATION_EXTERNAL_RESOURCE_NAME = "New simulation";
  public static final String DESCRIPTION = "Simulation description";
  public static final String MODEL = "solvingModels/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  public static final UUID DATASET_ID_1 = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  public static final String DATASET_NAME_1 = "datasets/" + DATASET_ID_1;
  public static final UUID DATASET_ID_2 = fromString("04e81eda-5ce7-4ce7-843c-34ee32a5182f");
  public static final String DATASET_NAME_2 = "datasets/" + DATASET_ID_2;
  public static final Set<String> DATASETS = of(DATASET_NAME_1, DATASET_NAME_2);
  public static final String USERNAME = "USERNAME";
  public static final SimulationState SIMULATION_STATE = PENDING;
  public static final Instant NOW = Instant.ofEpochMilli(1566469674663L);

  static final DataIndexResponse INDEX_RESPONSE = DataIndexResponse
      .newBuilder()
      .setRequestId(REQUEST_ID)
      .build();

  static final Analysis ANALYSIS = Analysis
      .newBuilder()
      .setState(DONE)
      .build();

  public static final SimulationDetailsDto SIMULATION_DETAILS =
      SimulationDetailsDto.builder()
          .id(SIMULATION_ID)
          .name(SIMULATION_NAME)
          .simulationName(SIMULATION_EXTERNAL_RESOURCE_NAME)
          .description(DESCRIPTION)
          .state(SIMULATION_STATE)
          .model(MODEL)
          .analysis(ANALYSIS_NAME)
          .datasets(DATASETS)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .build();
}
