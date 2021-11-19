package com.silenteight.simulator.model.archive;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.model.api.v1.ModelsArchived;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.management.domain.SimulationState.PENDING;
import static java.time.ZoneOffset.UTC;
import static java.util.Set.of;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ModelsArchivedFixtures {

  public static final UUID SIMULATION_ID = fromString("a9b45451-6fde-4832-8dc0-d17b4708d8ca");
  public static final String SIMULATION_RESOURCE_NAME = "simulations/" + SIMULATION_ID;
  public static final String SIMULATION_NAME = "New simulation";
  public static final UUID DATASET_ID = fromString("04e81eda-5ce7-4ce7-843c-34ee32a5182f");
  public static final String DATASET_NAME = "datasets/" + DATASET_ID;
  public static final Set<String> DATASETS = of(DATASET_NAME);
  public static final String USERNAME = "USERNAME";
  public static final Instant NOW = Instant.ofEpochMilli(1566469674663L);
  private static final String MODEL_NAME_1 = "solvingModels/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String MODEL_NAME_2 = "solvingModels/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  static final List<String> MODEL_NAMES = List.of(MODEL_NAME_1, MODEL_NAME_2);

  public static final SimulationDto SIMULATION_DTO =
      SimulationDto.builder()
          .id(SIMULATION_ID)
          .name(SIMULATION_RESOURCE_NAME)
          .simulationName(SIMULATION_NAME)
          .state(PENDING)
          .model(MODEL_NAME_1)
          .datasets(DATASETS)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .build();

  static final ModelsArchived MODELS_ARCHIVED_MESSAGE =
      ModelsArchived.newBuilder()
          .addAllModels(MODEL_NAMES)
          .build();
}
