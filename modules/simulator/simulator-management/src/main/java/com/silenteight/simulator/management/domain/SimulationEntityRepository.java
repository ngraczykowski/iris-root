package com.silenteight.simulator.management.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface SimulationEntityRepository extends Repository<SimulationEntity, Long> {

  SimulationEntity save(SimulationEntity simulationEntity);

  Collection<SimulationEntity> findAll();

  Collection<SimulationEntity> findAllByModelName(String modelName);
}
