package com.silenteight.simulator.management;

import com.silenteight.simulator.management.dto.CreateSimulationRequest;
import com.silenteight.simulator.management.dto.SimulationDto;
import com.silenteight.simulator.management.dto.SimulationState;

import java.time.Instant;
import java.util.UUID;

import static com.silenteight.simulator.management.dto.SimulationState.PENDING;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

class SimulationFixture {

  static final UUID SIMULATION_ID = fromString("a9b45451-6fde-4832-8dc0-d17b4708d8ca");
  static final String NAME = "New simulation";
  static final String DESCRIPTION = "Simulation description";
  static final UUID DATASET_ID = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  static final UUID POLICY_ID = fromString("d17b4708-6fde-8dc0-4832-d17b4708d8ca");
  static final String USERNAME = "USERNAME";
  static final SimulationState STATUS = PENDING;
  static final Instant NOW = Instant.ofEpochMilli(1566469674663L);


  public static final CreateSimulationRequest CREATE_SIMULATION_REQUEST =
      CreateSimulationRequest.builder()
          .id(SIMULATION_ID)
          .name(NAME)
          .description(DESCRIPTION)
          .policyId(POLICY_ID)
          .datasetId(DATASET_ID)
          .build();

  public static final SimulationDto SIMULATION_DTO =
      SimulationDto.builder()
          .id(SIMULATION_ID)
          .name(NAME)
          .policyId(POLICY_ID)
          .datasetId(DATASET_ID)
          .createdAt(NOW.atOffset(UTC))
          .createdBy(USERNAME)
          .status(STATUS)
          .build();
}
