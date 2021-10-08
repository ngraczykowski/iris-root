package com.silenteight.simulator.management.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface SimulationRepository extends Repository<SimulationEntity, Long> {

  SimulationEntity save(SimulationEntity simulationEntity);

  Collection<SimulationEntity> findAllByStateIn(Collection<SimulationState> states);

  Collection<SimulationEntity> findAllByModelName(String modelName);

  Optional<SimulationEntity> findByAnalysisName(String analysisName);

  Optional<SimulationEntity> findBySimulationId(UUID simulationId);
}
