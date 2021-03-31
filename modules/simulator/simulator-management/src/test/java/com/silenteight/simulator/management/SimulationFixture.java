package com.silenteight.simulator.management;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.management.dto.SimulationDto;
import com.silenteight.simulator.management.dto.SimulationState;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.silenteight.simulator.management.dto.SimulationState.PENDING;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

class SimulationFixture {

  static final UUID SIMULATION_ID = fromString("a9b45451-6fde-4832-8dc0-d17b4708d8ca");
  static final String NAME = "New simulation";
  static final String DESCRIPTION = "Simulation description";
  static final String MODEL_NAME = "solving-models/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  static final String DATASET_NAME = "datasets/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final List<String> DATASET_NAMES = List.of(DATASET_NAME);
  static final String USERNAME = "USERNAME";
  static final SimulationState STATUS = PENDING;
  static final Instant NOW = Instant.ofEpochMilli(1566469674663L);
  static final String POLICY_NAME = "policies/de1afe98-0b58-4941-9791-4e081f9b8139";
  static final String STRATEGY_NAME = "UNSOLVED_ALERTS";
  static final List<String> CATEGORIES = List.of("category-1", "category-2");
  static final String ANALYSIS_NAME = "analysis/01256804-1ce1-4d52-94d4-d1876910f272";

  static final CreateSimulationRequest CREATE_SIMULATION_REQUEST =
      CreateSimulationRequest.builder()
          .id(SIMULATION_ID)
          .name(NAME)
          .description(DESCRIPTION)
          .modelName(MODEL_NAME)
          .datasetNames(DATASET_NAMES)
          .createdBy(USERNAME)
          .build();

  static final SimulationDto SIMULATION_DTO =
      SimulationDto.builder()
          .id(SIMULATION_ID)
          .name(NAME)
          .modelName(MODEL_NAME)
          .datasetNames(DATASET_NAMES)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .status(STATUS)
          .build();

  static final SolvingModel SOLVING_MODEL =
      SolvingModel.newBuilder()
          .setStrategyName(STRATEGY_NAME)
          .setPolicyName(POLICY_NAME)
          .addAllCategories(CATEGORIES)
          .build();

  static final Analysis ANALYSIS =
      Analysis.newBuilder()
          .setName(ANALYSIS_NAME)
          .build();
}
