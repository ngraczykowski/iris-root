package com.silenteight.simulator.management;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.State;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.management.create.CreateSimulationRequest;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.SimulationState;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.simulator.management.domain.SimulationState.PENDING;
import static java.time.ZoneOffset.UTC;
import static java.util.Set.of;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimulationFixtures {

  public static final UUID ID = fromString("a9b45451-6fde-4832-8dc0-d17b4708d8ca");
  public static final String NAME = "simulations/" + ID;
  public static final String SIMULATION_NAME = "New simulation";
  public static final String DESCRIPTION = "Simulation description";
  public static final String MODEL_NAME = "solvingModels/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  public static final State ANALYSIS_STATE = DONE;
  public static final UUID DATASET_ID_1 = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  public static final String DATASET_NAME_1 = "datasets/" + DATASET_ID_1;
  public static final UUID DATASET_ID_2 = fromString("04e81eda-5ce7-4ce7-843c-34ee32a5182f");
  public static final String DATASET_NAME_2 = "datasets/" + DATASET_ID_2;
  public static final Set<String> DATASETS = of(DATASET_NAME_1, DATASET_NAME_2);
  public static final String USERNAME = "USERNAME";
  public static final SimulationState STATE = PENDING;
  public static final Instant NOW = Instant.ofEpochMilli(1566469674663L);
  public static final String POLICY_NAME = "policies/de1afe98-0b58-4941-9791-4e081f9b8139";
  public static final String STRATEGY_NAME = "UNSOLVED_ALERTS";
  public static final List<String> CATEGORIES = List.of("category-1", "category-2");
  public static final String ANALYSIS_NAME = "analysis/01256804-1ce1-4d52-94d4-d1876910f272";
  public static final long SOLVED_ALERTS = 3632L;
  public static final long ALL_ALERTS = 10000L;
  public static final long AI_SOLVED_AS_FALSE_POSITIVE = 3632L;
  public static final long ANALYSTS_SOLVED_AS_FALSE_POSITIVE = 3632L;

  public static final CreateSimulationRequest CREATE_SIMULATION_REQUEST =
      CreateSimulationRequest.builder()
          .id(ID)
          .simulationName(SIMULATION_NAME)
          .description(DESCRIPTION)
          .model(MODEL_NAME)
          .datasets(DATASETS)
          .createdBy(USERNAME)
          .build();

  public static final SimulationDto SIMULATION_DTO =
      SimulationDto.builder()
          .id(ID)
          .name(NAME)
          .simulationName(SIMULATION_NAME)
          .state(STATE)
          .model(MODEL_NAME)
          .datasets(DATASETS)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .build();

  public static final SolvingModel SOLVING_MODEL =
      SolvingModel.newBuilder()
          .setName(MODEL_NAME)
          .setStrategyName(STRATEGY_NAME)
          .setPolicyName(POLICY_NAME)
          .addAllCategories(CATEGORIES)
          .build();

  public static final Analysis ANALYSIS =
      Analysis.newBuilder()
          .setName(ANALYSIS_NAME)
          .setState(ANALYSIS_STATE)
          .build();

  public static final SimulationDetailsDto DETAILS_DTO =
      SimulationDetailsDto.builder()
          .id(ID)
          .name(NAME)
          .simulationName(SIMULATION_NAME)
          .description(DESCRIPTION)
          .state(STATE)
          .model(MODEL_NAME)
          .analysis(ANALYSIS_NAME)
          .datasets(DATASETS)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .build();
}
