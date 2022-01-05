package com.silenteight.simulator.management;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.State;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.management.archive.ArchiveSimulationRequest;
import com.silenteight.simulator.management.cancel.CancelSimulationRequest;
import com.silenteight.simulator.management.create.CreateSimulationRequest;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.SimulationState;
import com.silenteight.simulator.management.domain.dto.SimulationDto;
import com.silenteight.simulator.management.list.dto.SimulationListDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.simulator.management.domain.SimulationState.ARCHIVED;
import static com.silenteight.simulator.management.domain.SimulationState.CANCELED;
import static com.silenteight.simulator.management.domain.SimulationState.PENDING;
import static com.silenteight.simulator.management.domain.SimulationState.RUNNING;
import static java.time.OffsetDateTime.parse;
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
  public static final SimulationState PENDING_STATE = PENDING;
  public static final SimulationState CANCELED_STATE = CANCELED;
  public static final SimulationState ARCHIVED_STATE = ARCHIVED;
  public static final Instant NOW = Instant.ofEpochMilli(1566469674663L);
  public static final String POLICY_NAME = "policies/de1afe98-0b58-4941-9791-4e081f9b8139";
  public static final String STRATEGY_NAME = "UNSOLVED_ALERTS";
  public static final List<String> CATEGORIES = List.of("category-1", "category-2");
  public static final String ANALYSIS_NAME_1 = "analysis/4001";
  public static final String ANALYSIS_NAME_2 = "analysis/4002";
  public static final String ANALYSIS_NAME_3 = "analysis/4003";
  public static final long ALERTS_COUNT = 100L;
  public static final long SOLVED_ALERTS = 0L;
  public static final String PROCESSING_TIMESTAMP_1 = "2021-07-22T12:20:37.098Z";
  public static final String PROCESSING_TIMESTAMP_2 = "2021-07-22T10:00:37.098Z";
  public static final String PROCESSING_TIMESTAMP_3 = "2021-07-22T12:00:37.098Z";
  public static final CreateSimulationRequest CREATE_SIMULATION_REQUEST =
      CreateSimulationRequest.builder()
          .id(ID)
          .simulationName(SIMULATION_NAME)
          .description(DESCRIPTION)
          .model(MODEL_NAME)
          .datasets(DATASETS)
          .createdBy(USERNAME)
          .build();

  public static final CancelSimulationRequest CANCEL_SIMULATION_REQUEST =
      CancelSimulationRequest.builder()
          .id(ID)
          .canceledBy(USERNAME)
          .build();

  public static final ArchiveSimulationRequest ARCHIVE_SIMULATION_REQUEST =
      ArchiveSimulationRequest.builder()
          .id(ID)
          .archivedBy(USERNAME)
          .build();

  public static final SimulationListDto SIMULATION_LIST_DTO =
      SimulationListDto.builder()
          .id(ID)
          .name(NAME)
          .simulationName(SIMULATION_NAME)
          .state(PENDING_STATE)
          .model(MODEL_NAME)
          .datasets(DATASETS)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .build();

  public static final SimulationDto SIMULATION_DTO = SimulationDto.builder()
      .id(ID)
      .name(NAME)
      .description(DESCRIPTION)
      .simulationName(SIMULATION_NAME)
      .state(RUNNING)
      .datasets(DATASETS)
      .model(MODEL_NAME)
      .analysis(ANALYSIS_NAME_1)
      .createdBy(USERNAME)
      .solvedAlerts(0)
      .createdAt(parse(PROCESSING_TIMESTAMP_2))
      .updatedAt(parse(PROCESSING_TIMESTAMP_2))
      .build();

  public static final SimulationDto SIMULATION_DTO_2 = SimulationDto.builder()
      .id(ID)
      .name(NAME)
      .description(DESCRIPTION)
      .simulationName(SIMULATION_NAME)
      .state(RUNNING)
      .datasets(DATASETS)
      .model(MODEL_NAME)
      .analysis(ANALYSIS_NAME_2)
      .createdBy(USERNAME)
      .solvedAlerts(80)
      .createdAt(parse(PROCESSING_TIMESTAMP_2))
      .updatedAt(parse(PROCESSING_TIMESTAMP_2))
      .build();

  public static final SimulationDto SIMULATION_DTO_3 = SimulationDto.builder()
      .id(ID)
      .name(NAME)
      .description(DESCRIPTION)
      .simulationName(SIMULATION_NAME)
      .state(RUNNING)
      .datasets(DATASETS)
      .model(MODEL_NAME)
      .analysis(ANALYSIS_NAME_3)
      .createdBy(USERNAME)
      .createdAt(parse(PROCESSING_TIMESTAMP_3))
      .updatedAt(parse(PROCESSING_TIMESTAMP_3))
      .build();

  public static final SolvingModel SOLVING_MODEL =
      SolvingModel.newBuilder()
          .setName(MODEL_NAME)
          .setStrategyName(STRATEGY_NAME)
          .setPolicyName(POLICY_NAME)
          .addAllCategories(CATEGORIES)
          .build();

  public static final SimulationDetailsDto DETAILS_DTO =
      SimulationDetailsDto.builder()
          .id(ID)
          .name(NAME)
          .simulationName(SIMULATION_NAME)
          .description(DESCRIPTION)
          .state(PENDING_STATE)
          .model(MODEL_NAME)
          .analysis(ANALYSIS_NAME_1)
          .datasets(DATASETS)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .build();

  public static final Map<String, Optional<OffsetDateTime>>
      MAP_WITH_ANALYSIS_NAMES_AND_UPDATE_TIME_OPTIONALS =

      Map.of(ANALYSIS_NAME_1, Optional.empty(),
          ANALYSIS_NAME_2, Optional.of(parse("2021-07-22T11:00:37.098Z")),
          ANALYSIS_NAME_3, Optional.of(parse("2021-07-22T12:10:37.098Z")));

  public static final Map<String, Optional<OffsetDateTime>>
      MAP_WITH_ANALYSIS_NAMES_AND_CREATE_TIME_OPTIONALS =

      Map.of(ANALYSIS_NAME_1, Optional.empty(),
          ANALYSIS_NAME_2, Optional.of(parse("2021-07-22T11:00:37.098Z")),
          ANALYSIS_NAME_3, Optional.of(parse("2021-07-22T12:00:37.098Z")));

  public static final Map<String, Analysis> MAP_OF_ANALYSIS_WITH_NAMES =
      Map.of(ANALYSIS_NAME_1, createAnalysis(ANALYSIS_NAME_1, 15),
          ANALYSIS_NAME_2, createAnalysis(ANALYSIS_NAME_2, 20),
          ANALYSIS_NAME_3, createAnalysis(ANALYSIS_NAME_3, 0));

  public static Analysis createAnalysis(String analysisName, long pendingAlerts) {
    return Analysis.newBuilder()
        .setName(analysisName)
        .setState(ANALYSIS_STATE)
        .setAlertCount(ALERTS_COUNT)
        .setPendingAlerts(pendingAlerts)
        .build();
  }
}
