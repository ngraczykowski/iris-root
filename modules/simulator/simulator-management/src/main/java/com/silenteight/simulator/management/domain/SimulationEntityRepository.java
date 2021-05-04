package com.silenteight.simulator.management.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface SimulationEntityRepository extends Repository<SimulationEntity, Long> {

  SimulationEntity save(SimulationEntity simulationEntity);

  Collection<SimulationEntity> findAll();

  Collection<SimulationEntity> findAllByModelName(String modelName);

  Optional<SimulationEntity> findBySimulationId(UUID simulationId);
}
